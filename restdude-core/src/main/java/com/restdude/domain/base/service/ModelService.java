/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.service;


import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.domain.users.model.User;
import com.restdude.websocket.message.IActivityNotificationMessage;
import com.restdude.websocket.message.IMessageResource;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Provides SCRUD and utility operations for {@link T} entities
 * @author manos
 *
 * @param <T> the entity type
 * @param <ID> the entity ID type
 */
@Service
public interface ModelService <T extends Persistable<ID>, ID extends Serializable>
extends GenericService<T, ID>{

	/**
	 * Get the current user's details
	 * @return
	 */
	public ICalipsoUserDetails getPrincipal();

	/**
	 * Get the current user's details from the DB
	 * @return
	 */
	public User getPrincipalLocalUser();

	public void sendStompActivityMessage(IActivityNotificationMessage msg, String useername);

	public void sendStompActivityMessage(IActivityNotificationMessage msg, Iterable<String> useernames);

	public void sendStompStateChangeMessage(IMessageResource msg, String useername);

	public void sendStompStateChangeMessage(IMessageResource msg, Iterable<String> useernames);
}