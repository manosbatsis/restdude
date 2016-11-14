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
package com.restdude.domain.users.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@ApiModel(value = "UserInvitationsDTO", description = "Data transfer object for user invitations")
public class UserInvitationsDTO implements Serializable {

	@ApiModelProperty(value = "A line and/or comma delimeted string of RFC 2822 email addresses.")
	private String addressLines;

	@ApiModelProperty(value = "A list of UserDTO instances.")
	private List<UserDTO> recepients;

	public UserInvitationsDTO() {
	}

	public String getAddressLines() {
		return addressLines;
	}

	public void setAddressLines(String addressLines) {
		this.addressLines = addressLines;
	}

	public List<UserDTO> getRecepients() {
		return recepients;
	}

	public void setRecepients(List<UserDTO> recepients) {
		this.recepients = recepients;
	}

	public static class Builder {
		private String addressLines;
		private List<UserDTO> recepients = new LinkedList<UserDTO>();

		public Builder addressLines(String addressLines) {
			this.addressLines = addressLines;
			return this;
		}

		public Builder recepients(List<UserDTO> recepients) {
			this.recepients.addAll(recepients);
			return this;
		}
		
		public Builder recepient(UserDTO recepient) {
			this.recepients.add(recepient);
			return this;
		}

		public UserInvitationsDTO build() {
			return new UserInvitationsDTO(this);
		}
	}

	private UserInvitationsDTO(Builder builder) {
		this.addressLines = builder.addressLines;
		this.recepients = builder.recepients;
	}
}