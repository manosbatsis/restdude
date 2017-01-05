/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.friends.service.impl;

import com.restdude.domain.base.service.impl.AbstractModelServiceImpl;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.friends.model.FriendshipDTO;
import com.restdude.domain.friends.model.FriendshipId;
import com.restdude.domain.friends.model.FriendshipStatus;
import com.restdude.domain.friends.repository.FriendshipRepository;
import com.restdude.domain.friends.service.FriendshipService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.util.exception.http.BadRequestException;
import com.restdude.websocket.Destinations;
import com.restdude.websocket.message.ActivityNotificationMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named(FriendshipService.BEAN_ID)
public class FriendshipServiceImpl extends AbstractModelServiceImpl<Friendship, FriendshipId, FriendshipRepository>
		implements FriendshipService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean exists(Friendship resource) {
		return this.repository.exists(resource.getId());
	}

	/**
	 * Create a friendship request
	 */
	@Override
	@Transactional(readOnly = false)
	public Friendship create(Friendship resource) {
		LOGGER.debug("create: {}", resource);
		return this.saveRelationship(resource);
	}

	/**
	 * Create a friendship request
	 */
	@Override
	@Transactional(readOnly = false)
	public Friendship createTest(Friendship resource) {
		LOGGER.debug("createTest, resource: {}, left: {}, right: {}", resource, resource.getId().getLeft().getUsername(), resource.getId().getRight().getUsername());
		if (this.repository.exists(resource.getId())) {
			throw new RuntimeException("Friendship id already exists: " + resource.getId());
		}
		resource.setStatus(FriendshipStatus.CONFIRMED);
		FriendshipId InverseId = resource.getInverseId();
		if (InverseId == null) {
			throw new RuntimeException("Could not inverse friendship: " + resource);
		}
		if (resource.getId().getLeft().getId().equals(resource.getId().getRight().getId())) {
			throw new RuntimeException("Friendships with oneself is not allowed");
		}
		resource = this.repository.persist(resource);
		Friendship inverse = new Friendship(InverseId);
		LOGGER.debug("createAsConfirmed, inverse: {}, left: {}, right: {}", inverse, inverse.getId().getLeft().getUsername(), inverse.getId().getRight().getUsername());
		inverse.setStatus(FriendshipStatus.CONFIRMED);
		inverse = this.repository.persist(inverse);
		this.repository.flush();
		return resource;
	}

	/**
	 * Approve or reject a friendship request
	 */
	@Override
	@Transactional(readOnly = false)
	public Friendship patch(Friendship resource) {
		return this.update(resource);
	}


	/**
	 * Approve or reject a friendship request
	 */
	@Override
	@Transactional(readOnly = false)
	public Friendship update(Friendship resource) {
		LOGGER.debug("update: {}", resource);
		return this.saveRelationship(resource);
	}

	/**
	 * Delete the friendship and it's inverse
	 */
	@Override
	@Transactional(readOnly = false)
    public void delete(Friendship resource) {
        resource.setStatus(FriendshipStatus.DELETE);
		this.saveRelationship(resource);
	}


    protected void validateSender(Friendship resource) {

		LOGGER.debug("validateSender resource: {}", resource);

		// get current principal
		String userDetailsId = this.getPrincipal().getId();

		// set current user as sender if the latter is empty
        if (resource.getId().getLeft() == null) {
            resource.getId().setLeft(new User(userDetailsId));
        }

		// verify principal == owner
        if (!userDetailsId.equals(resource.getId().getLeft().getId())) {
            throw new BadRequestException("Invalid friendship owner.");
        }

		// verify friend is set
        User friend = resource.getId().getRight();
        if (friend == null || !StringUtils.isNotBlank(friend.getId())) {
            throw new BadRequestException("A (friend) id is required");
		}

		// verify not friend-to-self
		if (resource.getId().getLeft().getId().equals(resource.getId().getRight().getId())) {
			throw new BadRequestException("Friendships with oneself are not allowed");
		}
		LOGGER.debug("validateSender returns resource: {}", resource);
	}

	protected Friendship saveRelationship(Friendship resource) {

		validateSender(resource);

		// get the persisted record, if any, null otherwise
		FriendshipStatus currentStatus = this.repository.getCurrentStatus(resource.getId());

		// validate next status
		boolean allowedNext = FriendshipStatus.isAllowedNext(currentStatus, resource.getStatus());
		LOGGER.debug("saveRelationship, allowedNext: {}", allowedNext);
		if (!allowedNext) {
			throw new IllegalArgumentException("Cannot save with given status: " + resource.getStatus());
		}

		resource = saveSingle(resource);

		// update inverse if needed
		FriendshipStatus inverseStatus = FriendshipStatus.getApplicableInverse(resource.getStatus());
		if (inverseStatus != null) {
			Friendship inverse = new Friendship(resource.getInverseId(), inverseStatus);
			saveSingle(inverse);
		}

		return resource;
	}

	protected Friendship saveSingle(Friendship resource) {
		LOGGER.debug("saveSingle: {}", resource);
		// if delete
		if (FriendshipStatus.DELETE.equals(resource.getStatus())) {
			this.repository.delete(resource);
		} else {
			// persist changes
			resource = this.repository.exists(resource.getId()) ? this.repository.save(resource) : this.repository.persist(resource);

		}

		// notify this side's owner of appropriae statuses
		if (resource.getStatus().equals(FriendshipStatus.PENDING)
				|| resource.getStatus().equals(FriendshipStatus.CONFIRMED)) {
			// notify this side of pending request
            String username = this.userRepository.findUsernameById(
                    resource
                            .getId()
                            .getLeft()
                            .getId());
            LOGGER.debug("Sending friendship DTO to " + username);
			this.messagingTemplate.convertAndSendToUser(username, Destinations.USERQUEUE_FRIENDSHIPS,
					new FriendshipDTO(resource));
		}
		return resource;
	}

	@Override
	public Iterable<UserDTO> findAllMyFriends() {
		return repository.findAllFriends(this.getPrincipal().getId());
	}

	@Override
	public void sendStompActivityMessageToOnlineFriends(ActivityNotificationMessage msg) {

		// get online friends
		Iterable<String> useernames = this.repository.findAllStompOnlineFriendUsernames(this.getPrincipal().getId());

		this.sendStompActivityMessage(msg, useernames);
	}

}