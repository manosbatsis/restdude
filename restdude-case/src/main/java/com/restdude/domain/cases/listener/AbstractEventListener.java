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
package com.restdude.domain.cases.listener;

import java.util.Set;

import com.restdude.domain.PersistableModel;
import com.restdude.domain.cases.model.ActivityLog;
import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.service.ActivityLogService;
import com.restdude.domain.cases.service.MembershipService;
import com.restdude.domain.friends.service.FriendshipService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.model.AbstractPersistableModel;
import com.restdude.mdd.model.AbstractPersistableNamedModel;
import com.restdude.websocket.message.MessageResource;
import com.restdude.websocket.message.StompActivityNotificationMessage;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class AbstractEventListener<T extends PersistableModel> {

	protected FriendshipService friendshipService;
	protected ActivityLogService activityLogService;
	protected MembershipService membershipService;
	protected ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public void setFriendshipService(FriendshipService friendshipService) {
		this.friendshipService = friendshipService;
	}

	@Autowired
	public void setActivityLogService(ActivityLogService activityLogService) {
		this.activityLogService = activityLogService;
	}

	@Autowired
	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	@Autowired
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	protected void createLog(AbstractPersistableModel model, User user, BaseContext context, Enum predicate, MessageResource objectMessageResource) {


		ActivityLog activityLog = this.activityLogService.create(ActivityLog.builder()
				.context(context)
				.subject(user)
				.predicate(predicate.name())
				.object(model).build());
		log.debug("createLog: {}", activityLog);
		activityLog = this.activityLogService.create(activityLog);

		// create and send message to context members
		StompActivityNotificationMessage<UserDTO, BaseContextInfo> msg =
				new StompActivityNotificationMessage<UserDTO, BaseContextInfo>(
						UserDTO.fromUser(activityLog.getSubject()),
						activityLog.getPredicate(),
						BaseContextInfo.from(context));

		// send notification to BusinessContext members
		Set<String> recepients = this.membershipService.findOnlineMemberUsernames(context);
		// if BusinessContext is public send a notification to user's friends about the activity
		if (ContextVisibilityType.PUBLIC.equals(context.getVisibility())) {
			recepients.addAll(friendshipService.findAllStompOnlineFriendUsernames(context.getId()));
		}

		// send the message
		membershipService.sendStompActivityMessage(msg, recepients);

	}
}
