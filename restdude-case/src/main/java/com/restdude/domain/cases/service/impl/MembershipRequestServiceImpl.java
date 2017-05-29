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
import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.MembershipRequestInfo;
import com.restdude.domain.cases.model.dto.SpaceInvitations;
import com.restdude.domain.cases.model.dto.SpaceInvitationsResult;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.model.enums.MembershipRequestStatus;
import com.restdude.domain.cases.repository.MembershipRequestRepository;
import com.restdude.domain.cases.repository.SpaceRepository;
import com.restdude.domain.cases.service.MembershipRequestService;
import com.restdude.domain.cases.service.MembershipService;
import com.restdude.domain.friends.model.FriendshipIdentifier;
import com.restdude.domain.friends.repository.FriendshipRepository;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.util.exception.http.UnauthorizedException;
import com.restdude.websocket.message.ActivityNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.util.Optional;

@Slf4j
@Named("membershipRequestService")
public class MembershipRequestServiceImpl
		extends AbstractPersistableModelServiceImpl<MembershipRequest, String, MembershipRequestRepository>
		implements MembershipRequestService {
	
	protected FriendshipRepository friendshipRepository;
	
	protected MembershipService businessContextMembershipService;

	protected SpaceRepository spaceContextRepository;

	protected UserRepository userRepository;
	
	@Autowired
	public void setFriendshipRepository(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}

	@Autowired
	public void setSpaceRepository(SpaceRepository spaceContextRepository) {
		this.spaceContextRepository = spaceContextRepository;
	}

	@Autowired
	public void setBusinessContextMembershipService(MembershipService businessContextMembershipService) {
		this.businessContextMembershipService = businessContextMembershipService;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<MembershipRequest> findOneByContextAndUser(BaseContext context, User user) {
		return this.repository.findOneByContextAndUser(context, user);
	}

	/**
	 * Persist a list of BusinessContext invitations made by the BusinessContext owner.
	 * @param resource
	 * @return
	 */
	@Override
	@Transactional(readOnly = false)
	public SpaceInvitationsResult create(SpaceInvitations resource){

		// get current principal
		UserDetails userDetails = this.getPrincipal();

		/// get the BusinessContext
		Space businessContext = this.spaceContextRepository.getOne(resource.getSpaceId());

		// verify principal is the context owner
		if (!businessContext.getOwner().getPk().equals(userDetails.getPk())) {
			throw new IllegalArgumentException("User not authorized to send invitations");
		}

		// init report
		SpaceInvitationsResult.Builder results = new SpaceInvitationsResult.Builder().businessContextId(businessContext.getPk());

		// process recipients
		for(String userHandle : resource.getCc()){
			try{

				// get the invited user
				User user = this.userRepository.findActiveByIdOrUsernameOrEmail(userHandle);
				if(user != null){
					// make sure a membership for this user does not already exist
					if(this.businessContextMembershipService.getMembership(businessContext, user) == null){

						// check for existing request
						MembershipRequest existingRequest = this.repository.findOneByContextAndUser(businessContext, user).orElse(null);
						if(existingRequest == null){
							// create invitation
							this.create(new MembershipRequest.Builder()
									.context(businessContext)
									.user(user)
									.status(MembershipRequestStatus.SENT_INVITE)
									.build());
							// note invited
							results.invited(userHandle);
						}
						else{
							// if invitation already pending
							if(MembershipRequestStatus.SENT_INVITE.equals(existingRequest.getStatus())){
								results.pending(userHandle);
							}
							// if invitations are blocked by user mark as invalid
							else if(MembershipRequestStatus.BLOCK_INVITE.equals(existingRequest.getStatus())){
								results.invalid(userHandle);
							}
							// if user is blocked also handle as invalid
							else if(MembershipRequestStatus.BLOCK_REQUEST.equals(existingRequest.getStatus())){
								results.invalid(userHandle);
							}
							// if user has already sent a request, confirm it
							else if(MembershipRequestStatus.SENT_REQUEST.equals(existingRequest.getStatus())){
								existingRequest.setStatus(MembershipRequestStatus.CONFIRMED);
								this.update(existingRequest);
								results.approved(userHandle);
							}
						}
					}
					else{
						// note already member
						results.members(userHandle);
					}
				}
				else{
					// note not found as invalid
					results.invalid(userHandle);
				}
			}
			catch (Exception e){
				log.error("Failed processing invitation recipient: " + userHandle, e);
				results.failed(userHandle);
			}


		}
		
		// return results
		return results.build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean exists(BaseContext context, User user) {
		Boolean exists = this.repository.exists(context, user);
		log.debug("exists: {}, context: {}, user: {}", exists, context, user);
		return exists;
	}

	/**
	 * Persist a new instance. If created by a BusinessContext member it's an invitation, otherwise it is a request to join.
	 */
	@Transactional(readOnly = false)
	@Override
	public MembershipRequest create(MembershipRequest resource) {
		return create(resource, false);
	}


	@Transactional(readOnly = false)
	@Override
	public MembershipRequest createConfirmed(MembershipRequest resource) {
		resource = create(resource, true);
		this.repository.flush();
		return resource;


	}

	private MembershipRequest create(MembershipRequest resource, boolean skipValidation) {
		// get current principal and target BusinessContext
		UserDetails userDetails = this.getPrincipal();
		resource.setContext(this.spaceContextRepository.getOne(resource.getContext().getPk()));
		log.debug("create, userDetails: {}, ", userDetails);
		log.debug("create, resource context owner: {}", resource.getContext().getOwner());
		log.debug("create, resource context: {}}", resource.getContext());
		log.debug("create, resource user: {}, ", resource.getUser());


		// set current user as target if none was given
		if(resource.getUser() == null || resource.getUser().getPk() == null){
			resource.setUser(new User(userDetails.getPk()));
		}

		log.debug("create, userDetails: {}, isOwner: {}", userDetails, this.spaceContextRepository.isOwner(resource.getContext(), new User(userDetails.getPk())));
		log.debug("create, resource context owner: {}", resource.getContext().getOwner());
		log.debug("create, resource context: {}}", resource.getContext());
		log.debug("create, resource user: {}, ", resource.getUser());

		FriendshipIdentifier id = new FriendshipIdentifier();
		id.init(userDetails.getPk(), resource.getUser().getPk());
		FriendshipIdentifier idInv = new FriendshipIdentifier();
		idInv.init(resource.getUser().getPk(), userDetails.getPk());
		boolean friends = this.friendshipRepository.exists(id);
		boolean friendsInv = this.friendshipRepository.exists(idInv);

		log.debug("create, friends: {}, inverse: {}", friends, friendsInv);
		log.debug("create, memberships:");
		if(CollectionUtils.isNotEmpty(resource.getContext().getMemberships())){

			for(Membership m : resource.getContext().getMemberships()){
				log.debug("create, context membership: {}, ", m);
			}
		}
		log.debug("create, memberships requests:");
		if(CollectionUtils.isNotEmpty(resource.getContext().getMembershipRequests())) {
			for (MembershipRequest m : resource.getContext().getMembershipRequests()) {
				log.debug("create, context membership request: {}, ", m);
			}
		}
		if (!skipValidation) {
			// request mode
			if (userDetails.getPk().equals(resource.getUser().getPk())) {
				// make sure context is visible if user is requesting to join
				if(!this.spaceContextRepository.isVisible(resource.getContext().getPk())) {
					throw new UnauthorizedException("Cannot complete request to join, BusinessContext does not exist or is not visible");
				}
			}
			// invitation mode
			else{
				// make sure current principal can invite the target user
				if(!this.spaceContextRepository.canApproveRequestToJoinSpace(resource.getContext())) {
					throw new UnauthorizedException("Cannot complete invitation, BusinessContext does not exist or user is unauthorized or is not friend of target user");
				}
			}
		}

		// barf if a request has already been made
		if( this.repository.exists(resource.getContext(), resource.getUser()) ){
			throw new IllegalArgumentException("A resource for the given BusinessContext and user already exists");
		}

		// validate request workflow
		if(!skipValidation) {
			validateRequest(userDetails, resource, null);
		}
		// persist and return
		resource = super.create(resource);

		// handle changes
		this.handleNewStatus(resource);

		return resource;
	}


	/**
	 *  Update a membership request.
	 */
	@Transactional(readOnly = false)
	@Override
	public MembershipRequest update(MembershipRequest resource) {
		return this.patch(resource);
	}

	/**
	 *  Update a membership request.
	 */
	@Transactional(readOnly = false)
	@Override
	public MembershipRequest patch(MembershipRequest resource) {

		// get current principal
		UserDetails userDetails = this.getPrincipal();
		
		// load the persisted record
		MembershipRequest businessContextMembershipRequest = this.repository.findOne(resource.getPk());
		resource.setContext(businessContextMembershipRequest.getContext());

		// validate request
		validateRequest(userDetails, resource, businessContextMembershipRequest);

		// update the status
		businessContextMembershipRequest.setStatus(resource.getStatus());

        // if delete
		if(MembershipRequestStatus.DELETE.equals(businessContextMembershipRequest.getStatus())){
            super.delete(businessContextMembershipRequest);
        }
        else{
            businessContextMembershipRequest = super.update(businessContextMembershipRequest);
        }

		// handle changes
		this.handleNewStatus(businessContextMembershipRequest);


		// return the updated request
		return businessContextMembershipRequest;

	}

	/**
	 * Validate incoming request
	 * @param userDetails
	 * @param requestResource
	 * @param persisted
	 */
	private void validateRequest(UserDetails userDetails, MembershipRequest requestResource, MembershipRequest persisted) {
		log.debug("validateRequest, userDetails: {}, requestResource: {}", userDetails, requestResource);
		// check if principal is join handler or target
		MembershipRequest reference = persisted != null ? persisted : requestResource;
		boolean isRequestUser = userDetails.getPk().equals(reference.getUser().getPk());
		log.debug("validateRequest, isRequestUser: {}, reference: {}", isRequestUser, reference);
		boolean isJoinRequestHandler = this.spaceContextRepository.canInviteUserToSpace(reference.getContext());
		boolean isBusinessContextOwner = userDetails.getPk().equals(requestResource.getContext().getOwner().getPk());
		log.debug("validateRequest, isJoinRequestHandler: {}, isBusinessContextOwner: {}", isJoinRequestHandler, isBusinessContextOwner);

		// ensure principal is either join handler or target user but not both
		if( (!isRequestUser && !isJoinRequestHandler) || (isRequestUser && isJoinRequestHandler)){
			throw new UnauthorizedException("Evaluation of provided request could not be authorized");
		}

		boolean preConfirmedForPublic = false;

		// set default status for new entity if empty
		if(persisted == null){
			// automatically approve request to join public BusinessContext
			if(isRequestUser && ContextVisibilityType.PUBLIC.equals(requestResource.getContext().getVisibility())){
				requestResource.setStatus(MembershipRequestStatus.CONFIRMED);
				preConfirmedForPublic = true;
			}
			// else set default initial status per mode
			else{
				requestResource.setStatus(isJoinRequestHandler ? MembershipRequestStatus.SENT_INVITE : MembershipRequestStatus.SENT_REQUEST);
			}
		}

		// block requests by owner only
		if(MembershipRequestStatus.BLOCK_REQUEST.equals(requestResource.getStatus()) && !isBusinessContextOwner){
			throw new UnauthorizedException("Only owner can block requests to join");
		}

		// get persisted status, if any
		MembershipRequestStatus currentStatus = persisted != null ? persisted.getStatus() : null;

		// if not valid "next" status, throw a 401
		if(!preConfirmedForPublic && !MembershipRequestStatus.isValidChange(currentStatus, requestResource.getStatus(), isJoinRequestHandler)){
            throw new UnauthorizedException("Invalid candidate status could not be authorized");
        }
	}

	/**
     * Delete the record
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(MembershipRequest resource) {
        resource.setStatus(MembershipRequestStatus.DELETE);
        this.update(resource);
    }

    /**
     * Delete the record
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(String id) {
        MembershipRequest resource = new MembershipRequest();
        resource.setPk(id);
        this.delete(resource);
    }


	/**
	 * Create/delete memberships and send notifications according to persisted state
	 * @param businessContextMembershipRequest
	 */
	protected void handleNewStatus(MembershipRequest businessContextMembershipRequest) {

        // create the membership if approved
        if(MembershipRequestStatus.CONFIRMED.equals(businessContextMembershipRequest.getStatus())){
            this.businessContextMembershipService.create(new Membership.Builder()
                    .context(businessContextMembershipRequest.getContext())
                    .user(businessContextMembershipRequest.getUser())
                    .build());
        }

		// delete any existing membership if any and new status is blocked or deleted
		else if(MembershipRequestStatus.DELETE.equals(businessContextMembershipRequest.getStatus())
				|| MembershipRequestStatus.BLOCK_INVITE.equals(businessContextMembershipRequest.getStatus())
				|| MembershipRequestStatus.BLOCK_REQUEST.equals(businessContextMembershipRequest.getStatus())){
			// delete any membership matching the BusinessContext and user
			this.businessContextMembershipService.delete(
					businessContextMembershipRequest.getContext(), businessContextMembershipRequest.getUser()
			);
		}

		// send notifications for new requests to owner
		else if(MembershipRequestStatus.SENT_REQUEST.equals(businessContextMembershipRequest.getStatus())){
			// create and send message to context owner
			ActivityNotificationMessage<UserDTO, MembershipRequestStatus, MembershipRequestInfo> msg =
					new ActivityNotificationMessage<>(
							UserDTO.fromUser(businessContextMembershipRequest.getUser()), MembershipRequestStatus.SENT_REQUEST,
							MembershipRequestInfo.from(businessContextMembershipRequest));
			// notify according to BusinessContext type
			if(ContextVisibilityType.OPEN.equals(businessContextMembershipRequest.getContext().getVisibility())){
				this.sendStompActivityMessage(msg, this.businessContextMembershipService.findOnlineMemberUsernames(businessContextMembershipRequest.getContext()));
			}
			else{
				this.sendStompActivityMessage(msg, this.userRepository.findUsernameById(businessContextMembershipRequest.getContext().getOwner().getPk()));
			}
		}

		// send notifications for new invitations to recipient user
		else if(MembershipRequestStatus.SENT_INVITE.equals(businessContextMembershipRequest.getStatus())){
			// create and send message to context owner
			ActivityNotificationMessage<UserDTO, MembershipRequestStatus, MembershipRequestInfo> msg =
					new ActivityNotificationMessage<>(
							UserDTO.fromUser(businessContextMembershipRequest.getContext().getOwner()), MembershipRequestStatus.SENT_INVITE,
							MembershipRequestInfo.from(businessContextMembershipRequest));
			this.sendStompActivityMessage(msg, this.userRepository.findUsernameById(businessContextMembershipRequest.getUser().getPk()));
		}

	}

}