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

import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.enums.SpaceActivity;
import com.restdude.domain.event.EntityCreatedEvent;
import com.restdude.domain.users.model.User;
import com.restdude.websocket.message.MessageResource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Creates a "personal" {@link Space} for a newly persisted {@link User}
 */
@Slf4j
//@Component
public class MembershipCreatedEventListener extends AbstractEventListener<Membership>  implements ApplicationListener<EntityCreatedEvent<Membership>> {


	/**
	 * Handle an application event.

	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(EntityCreatedEvent<Membership> event) {

		Membership model = event.getModel();
		log.debug("onApplicationEvent membership: {}", model);
		User user = model.getUser();
		BaseContext context = model.getContext();
		Enum predicate = SpaceActivity.BECAME_MEMBER_OF;
		MessageResource objectMessageResource = BaseContextInfo.from(context);

		createLog(context, user, context, predicate, objectMessageResource);

	}
}
