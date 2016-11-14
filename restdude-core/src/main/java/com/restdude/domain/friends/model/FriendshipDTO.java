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
package com.restdude.domain.friends.model;

import com.restdude.domain.users.model.UserDTO;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "FriendshipDTO", description = "FriendshipDTO is a lightweight DTO version of Friendship")
public class FriendshipDTO implements Serializable {


	private static final long serialVersionUID = 1L;
	private String id;
	private UserDTO owner;
	private UserDTO friend;
	private FriendshipStatus status = FriendshipStatus.PENDING;
	
	public FriendshipDTO() {
	}
	
	public FriendshipDTO(Friendship friendship){
		FriendshipId id = friendship.getId();
		if(id != null){
			this.id = id.toStringRepresentation();
			this.owner = UserDTO.fromUser(id.getOwner());
			this.friend = UserDTO.fromUser(id.getFriend());
		}
		this.status = friendship.getStatus();
	}

	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}

	public UserDTO getFriend() {
		return friend;
	}

	public void setFriend(UserDTO friend) {
		this.friend = friend;
	}

	public FriendshipStatus getStatus() {
		return status;
	}

	public void setStatus(FriendshipStatus status) {
		this.status = status;
	}
	

}