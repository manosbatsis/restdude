/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-websocket, https://manosbatsis.github.io/restdude/restdude-websocket
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.websocket.service.impl;


import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.domain.base.service.impl.AbstractModelServiceImpl;
import com.restdude.domain.friends.repository.FriendshipRepository;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.websocket.Destinations;
import com.restdude.websocket.message.StateUpdateMessage;
import com.restdude.websocket.model.StompSession;
import com.restdude.websocket.repository.StompSessionRepository;
import com.restdude.websocket.service.StompSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.*;

@Service(StompSessionService.BEAN_ID)
public class StompSessionServiceImpl extends AbstractModelServiceImpl<StompSession, String, StompSessionRepository> implements StompSessionService {


    private static final Logger LOGGER = LoggerFactory.getLogger(StompSessionServiceImpl.class);

    private FriendshipRepository friendshipRepository;

    @Autowired
    public void setFriendshipRepository(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }


    //	@EventListener({ SessionConnectEvent.class })
//	@Transactional(readOnly = false)
    public void onSessionConnectEvent(SessionConnectEvent event) {
    }

    @EventListener({SessionConnectedEvent.class})
    @Transactional(readOnly = false)
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) event.getUser();
        UserDetails ud = (UserDetails) auth.getPrincipal();
        // persist STOMP session
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        StompSession stompSession = this.create(new StompSession.Builder().id(sha.getSessionId()).user(new User(ud.getPk())).build());
        LOGGER.debug("Persisted STOMP session: {}:{}", event.getUser().getName(), stompSession.getPk());
    }

    //	@EventListener({ SessionSubscribeEvent.class })
//	@Transactional(readOnly = false)
    public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
    }

    //	@EventListener({ SessionUnsubscribeEvent.class })
//	@Transactional(readOnly = false)
    public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
    }

    @EventListener({SessionDisconnectEvent.class})
    @Transactional(readOnly = false)
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        this.delete(event.getSessionId());
        LOGGER.debug("Deleted  STOMP session: {}:{}", event.getUser().getName(), event.getSessionId());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(" hasRole('ROLE_USER') ")
    public StompSession create(StompSession resource) {
        validateUser(resource);

        // get user pk
        String userId = resource.getUser().getPk();
        // count active stom sessions for user
        long count = this.repository.countForUser(userId).longValue();
        // create the stomp session
        resource = super.create(resource);

        // update user status and notify accordignly
        if (count == 0) {
            long stompSessionCount = count + 1;

            sendStomSessionStatusUpdateToFriends(userId, stompSessionCount);
        }
        return resource;
    }


    public void sendStomSessionStatusUpdateToFriends(String userId, long stompSessionCount) {

        // get online friends
        Iterable<String> useernames = this.friendshipRepository.findAllStompOnlineFriendUsernames(userId);

        // create state update message
        StateUpdateMessage<String> msg = new StateUpdateMessage<String>();
        msg.setId(userId);
        msg.setResourceClass(UserDTO.class.getCanonicalName());
        msg.addModification("stompSessionCount", stompSessionCount);

        // notify friends
        for (String useername : useernames) {

            this.messagingTemplate.convertAndSendToUser(useername, Destinations.USERQUEUE_UPDATES_STATE, msg);

        }
    }


    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(" hasRole('ROLE_USER') ")
    public StompSession update(StompSession resource) {
        validateUser(resource);
        return super.update(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(" hasRole('ROLE_USER') ")
    public void delete(@P("resource") StompSession sess) {
        sess = this.repository.getOne(sess.getPk());
        long count = this.repository.countForUser(sess.getUser().getPk()).longValue();
        super.delete(sess);
        // notify friends if user has gone offline
        if (count <= 1) {
            sendStomSessionStatusUpdateToFriends(sess.getUser().getPk(), count - 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(" hasRole('ROLE_USER') ")
    public void delete(String id) {
        StompSession sess = this.repository.findOne(id);
        if (sess != null) {
            long count = this.repository.countForUser(sess.getUser().getPk()).longValue();
            super.delete(sess);
            // notify friends if user has gone offline
//			if(count <= 1){
            sendStomSessionStatusUpdateToFriends(sess.getUser().getPk(), count - 1);
//			}

        }
    }


    public void validateUser(StompSession resource) {
        ICalipsoUserDetails ud = this.getPrincipal();
        LOGGER.info("validateUser userDetails: {}", ud);
        if (resource.getUser() == null) {
            User user = this.userRepository.getOne(ud.getPk());
            LOGGER.info("validateUser adding current user: {} ", user);
            resource.setUser(user);
        }
//		else if(!ud.getPk().equals(resource.getUser().getPk())){
//			throw new IllegalArgumentException("Session user does not match current principal");
//		}
    }

}

	

