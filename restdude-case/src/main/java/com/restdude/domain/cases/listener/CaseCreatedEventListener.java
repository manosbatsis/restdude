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

import com.restdude.domain.cases.model.AbstractCase;
import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.CaseInfo;
import com.restdude.domain.cases.model.enums.CasesActivity;
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
@Component
public class CaseCreatedEventListener extends AbstractEventListener<AbstractCase> {//implements ApplicationListener<EntityCreatedEvent<AbstractCase>> {


	@EventListener
	public void onApplicationEvent(EntityCreatedEvent<AbstractCase> event) {

		AbstractCase model = event.getModel();
		User user = model.getCreatedBy();
		BaseContext context = model.getApplication();
		Enum predicate = CasesActivity.UPDATED;
		MessageResource objectMessageResource = CaseInfo.from(model);
		
		createLog(model, user, context, predicate, objectMessageResource);

	}

}
