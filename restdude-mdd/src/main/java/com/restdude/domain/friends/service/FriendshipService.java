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
package com.restdude.domain.friends.service;

import java.util.List;

import com.restdude.domain.friends.model.FriendshipIdentifier;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.websocket.message.StompActivityNotificationMessage;

public interface FriendshipService extends PersistableModelService<Friendship, FriendshipIdentifier> {

	public static final String BEAN_ID = "friendshipService";

	List<String> findAllStompOnlineFriendUsernames(String userId);

	public Iterable<UserDTO> findAllMyFriends();

//	public Page<UserDTO> findAllMyFriendsPaginated(Pageable pageRequest);

	public void sendStompActivityMessageToOnlineFriends(StompActivityNotificationMessage msg);

    Friendship createTest(Friendship resource);

    /**
     * Check if a friendship between the given parties already exists
     */
    public Boolean exists(Friendship resource);
}