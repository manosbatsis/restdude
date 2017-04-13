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
package com.restdude.domain.cases.service;

import com.restdude.domain.cases.model.SpaceContext;
import com.restdude.domain.cases.repository.MembershipRepository;
import com.restdude.websocket.message.IActivityNotificationMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default interface implementation for sending messages to BusinessContext members
 */
public interface BusinessContextMembersActivityMessanger {

	public void sendStompActivityMessage(IActivityNotificationMessage msg, Iterable<String> useernames);


	/**
	 * Sent the given activity message to online members of the given BusinessContext
	 * @param msg the message to send
	 * @param businessContext the BusinessContext whose online members should receive the message
	 */
	default void sendStompActivityMessageToMembers(IActivityNotificationMessage msg, SpaceContext businessContext) {
		this.sendStompActivityMessageToMembers(msg, businessContext, null);
	}

	/**
	 * Sent the given activity message to online members of the given BusinessContext and optionally, to additional recipients
	 * @param msg the message to send
	 * @param businessContext the BusinessContext whose online members should receive the message
	 * @param cc any additional recipients user names to be treated as recipients
	 */
	default void sendStompActivityMessageToMembers(IActivityNotificationMessage msg, SpaceContext businessContext, List<String> cc) {
		// build recipients collection
		Set<String> usernames = new HashSet<String>();

		// add any additional usernames given
		if(cc != null){
			usernames.addAll(cc);
		}

		// add online BusinessContext members
		usernames.addAll(this.getBusinessContextMembershipRepository().findOnlineMemberUsernames(businessContext));

		this.sendStompActivityMessage(msg, usernames);
	}


	public MembershipRepository getBusinessContextMembershipRepository();
	
}