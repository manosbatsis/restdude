/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.cases.service.impl;

import com.restdude.domain.UserDetails;
import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.MembershipContext;
import com.restdude.domain.cases.model.dto.MembershipContextInfo;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.model.enums.MembershipContextActivity;
import com.restdude.domain.cases.repository.MembershipRepository;
import com.restdude.domain.cases.service.BusinessContextMembersActivityMessanger;
import com.restdude.domain.cases.service.MembershipService;
import com.restdude.domain.friends.repository.FriendshipRepository;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.util.exception.http.BadRequestException;
import com.restdude.util.exception.http.UnauthorizedException;
import com.restdude.websocket.message.ActivityNotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.util.Set;

@Named("membershipService")
public class MembershipServiceImpl
		extends AbstractPersistableModelServiceImpl<Membership, String, MembershipRepository>
		implements MembershipService, BusinessContextMembersActivityMessanger {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);

	private FriendshipRepository friendshipRepository;

	@Autowired
	public void setFriendshipRepository(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}


	/**
	 * @see BusinessContextMembersActivityMessanger#getBusinessContextMembershipRepository()
	 */
	@Override
	public MembershipRepository getBusinessContextMembershipRepository() {
		return this.repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> findOnlineMemberUsernames(MembershipContext businessContext){
		return this.repository.findOnlineMemberUsernames(businessContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership getMembership(MembershipContext businessContext){
		User user = new User(this.getPrincipal().getPk());
		return getMembership(businessContext, user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Membership getMembership(MembershipContext businessContext, User user){
		// check for membership
		LOGGER.debug("getMembership, context: {}, user: {}", businessContext, user);
		Membership membership = this.repository.findOneByBusinessContextAndUser(businessContext, user).orElse(null);

		return membership;
	}

	/**
	 * Create the membership and send an activity notification message
	 * to BusinessContext members and, if BusinessContext is public, friends of the new member
	 */

	@Override
	@Transactional(readOnly = false)
	public Membership create(Membership resource){
		return this.create(resource, false);
	}

	@Override
	@Transactional(readOnly = false)
	public Membership createTest(Membership resource){
		resource = this.create(resource, false);
		this.repository.flush();
		return resource;
	}

	private Membership create(Membership resource, boolean skipAuthorizationCheck){
		if(this.repository.exists(resource.getContext(), resource.getUser())){
			throw new BadRequestException("Resource already exists: " + resource);
		}

		resource = super.create(resource);


		// create and send message to context members
		ActivityNotificationMessage<UserDTO, MembershipContextActivity, MembershipContextInfo> msg =
				new ActivityNotificationMessage<UserDTO, MembershipContextActivity, MembershipContextInfo>(
						UserDTO.fromUser(resource.getUser()), MembershipContextActivity.BECAME_MEMBER_OF, MembershipContextInfo.from(resource.getContext()));

		// send notification to BusinessContext members
		Set<String> recepients = this.repository.findOnlineMemberUsernames(resource.getContext());
		// if BusinessContext is public send a notification to user's friends about the activity
		if(ContextVisibilityType.PUBLIC.equals(resource.getContext().getVisibility())){
			recepients.addAll(this.friendshipRepository.findAllStompOnlineFriendUsernames(resource.getUser().getPk()));
		}

		// send the message
		this.sendStompActivityMessage(msg, recepients);

		return resource;
	}


	/**
	 * {@inheritDoc}
	 * [5.1.17] EH-138 Requests / Invites may not have established membership as they were not confirmed
	 * 			adding check for existing invitations [jB]
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(MembershipContext businessContext, User user){
		Membership membership = this.getMembership(businessContext, user);
		if(membership != null){
			this.delete(membership);
		}
	}

	/**
	 * Delete the membership and send an activity notification message
	 * to BusinessContext members
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(String id) {
		Membership m = new Membership();
		m.setPk(id);
		this.delete(this.repository.getOne(id));
	}

	/**
	 * Delete the membership and send an activity notification message
	 * to BusinessContext members
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(@P("resource") Membership resource) {
		// get current principal
		UserDetails userDetails = this.getPrincipal();

		// ensure principal is either BusinessContext owner or target user but not both
		boolean isRequestUser = userDetails.getPk().equals(resource.getUser().getPk());
		boolean isBusinessContextOwner = userDetails.getPk().equals(resource.getContext().getOwner().getPk());

		// ensure principal is either BusinessContext owner or target user but not both
		if( (!isRequestUser && !isBusinessContextOwner) || (isRequestUser && isBusinessContextOwner)){
			throw new UnauthorizedException("Evaluation of provided request could not be authorized");
		}

		// delete
		super.delete(resource);

		// send notifications
		notifyDeletion(resource.getContext(), resource.getUser());
	}

	private void notifyDeletion(MembershipContext businessContext, User user) {
		// create and send message to context members
		ActivityNotificationMessage<UserDTO, MembershipContextActivity, MembershipContextInfo> msg =
				new ActivityNotificationMessage<UserDTO, MembershipContextActivity, MembershipContextInfo>(
						UserDTO.fromUser(user), MembershipContextActivity.STOPPED_BEING_MEMBER_OF, MembershipContextInfo.from(businessContext));

		// send the message
		this.sendStompActivityMessageToMembers(msg, businessContext);
	}




}