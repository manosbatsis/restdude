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

import java.util.Objects;
import java.util.Set;

import com.restdude.domain.cases.model.BaseCase;
import com.restdude.domain.cases.model.AbstractCaseComment;
import com.restdude.domain.cases.model.ActivityLog;
import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.dto.CaseCommenttInfo;
import com.restdude.domain.cases.model.dto.CaseInfo;
import com.restdude.domain.cases.model.enums.CasesActivity;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.model.enums.SpaceActivity;
import com.restdude.domain.cases.service.ActivityLogService;
import com.restdude.domain.cases.service.MembershipService;
import com.restdude.domain.cases.service.SpaceService;
import com.restdude.domain.event.EntityCreatedEvent;
import com.restdude.domain.event.EntityUpdatedEvent;
import com.restdude.domain.friends.service.FriendshipService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.model.AbstractPersistableModel;
import com.restdude.websocket.message.MessageResource;
import com.restdude.websocket.message.StompActivityNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

/**
 * Created by manos on 16/6/2017.
 */
@Slf4j
public class AbstractEntityEventsHandler {


	protected SpaceService spaceService;
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

	@Autowired
	public void setSpaceService(SpaceService spaceService) {
		this.spaceService = spaceService;
	}




	@EventListener
	public final void onUserCreatedListener(EntityCreatedEvent<User> event) {
		this.onUserCreated(event);
	}

	protected void onUserCreated(EntityCreatedEvent<User> event) {

		User user = event.getModel();
		log.debug("onUserCreated user: {}", user.getUsername());
//		log.debug("onApplicationEvent event: {}", event.getSource());
		String name = user.getUsername();

		String title = user.getName();
		if (StringUtils.isBlank(title)) {
			title = name;
		}

		String description = user.getDescription();
		if (StringUtils.isBlank(description)) {
			description = "Space for " + name;
		}
		Space space = new Space.Builder()
				.owner(user)
				.name(name)
				.title(title)
				.description(description)
				.visibility(ContextVisibilityType.CLOSED)
				.build();
		log.debug("onUserCreated creating user space: {}", space);
		space = this.spaceService.create(space);
	}


	@EventListener
	public final void onMembershipCreatedListener(EntityCreatedEvent<Membership> event) {
		this.onMembershipCreated(event);
	}

	public void onMembershipCreated(EntityCreatedEvent<Membership> event) {

		Membership model = event.getModel();
		log.debug("onApplicationEvent membership: {}", model);
		User user = model.getUser();
		BaseContext context = model.getContext();
		Enum predicate = SpaceActivity.BECAME_MEMBER_OF;
		MessageResource objectMessageResource = BaseContextInfo.from(context);

		createLog(context, user, context, predicate, objectMessageResource);

	}



	@EventListener
	public final void onCaseCreatedListener(EntityCreatedEvent<BaseCase> event) {
		this.onCaseCreated(event);
	}

	public void onCaseCreated(EntityCreatedEvent<BaseCase> event) {

		BaseCase model = event.getModel();
		User user = model.getCreatedBy();
		BaseContext context = model.getApplication();
		Enum predicate = CasesActivity.CREATED;
		MessageResource objectMessageResource = CaseInfo.from(model);

		createLog(model, user, context, predicate, objectMessageResource);

	}

	@EventListener
	public final void onCaseUpdatedListener(EntityUpdatedEvent<BaseCase> event) {
		this.onCaseUpdated(event);
	}

	public void onCaseUpdated(EntityUpdatedEvent<BaseCase> event) {

		BaseCase model = event.getModel();
		User user = model.getCreatedBy();
		BaseContext context = model.getApplication();
		Enum predicate = CasesActivity.UPDATED;
		MessageResource objectMessageResource = CaseInfo.from(model);

		createLog(model, user, context, predicate, objectMessageResource);

	}

	@EventListener
	public <C extends AbstractCaseComment> void onCaseCommentCreated(EntityCreatedEvent<C> event) {

		AbstractCaseComment model = event.getModel();
		User user = model.getCreatedBy();
		BaseContext context = model.getSubject().getApplication();
		Enum predicate = CasesActivity.COMMENTED;
		MessageResource objectMessageResource = CaseCommenttInfo.from(model);

	}

	protected void createLog(AbstractPersistableModel model, User user, BaseContext context, Enum predicate, MessageResource objectMessageResource) {


		ActivityLog activityLog = this.activityLogService.create(ActivityLog.builder()
				.context(context)
				.subject(user)
				.predicate(predicate.name())
				.object(model).build());
		// handle base case subclasses
		if(Objects.nonNull(model) && BaseCase.class.isAssignableFrom(model.getClass())){
			activityLog.setDiscriminator("3");
		}

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
