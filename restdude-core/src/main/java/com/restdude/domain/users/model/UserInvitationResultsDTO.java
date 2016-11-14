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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@ApiModel(value = "UserInvitationResultsDTO", description = "Data transfer object for user invitation resultss")
public class UserInvitationResultsDTO implements Serializable {


	@ApiModelProperty(value = "A list of invited addressess.")
	private Set<String> invited = new HashSet<String>();

	@ApiModelProperty(value = "A list of ignored duplicate addressess.")
	private List<String> duplicate = new LinkedList<String>();

	@ApiModelProperty(value = "A list of ignored, pre-existing addressess.")
	private List<String> existing = new LinkedList<String>();
	
	@ApiModelProperty(value = "A list of invalid addressess.")
	private List<String> invalid = new LinkedList<String>();
	
	public UserInvitationResultsDTO() {
		super();
	}

	public Set<String> getInvited() {
		return invited;
	}

	public void setInvited(Set<String> invited) {
		this.invited = invited;
	}

	public List<String> getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(List<String> duplicate) {
		this.duplicate = duplicate;
	}

	public List<String> getExisting() {
		return existing;
	}

	public void setExisting(List<String> existing) {
		this.existing = existing;
	}

	public List<String> getInvalid() {
		return invalid;
	}

	public void setInvalid(List<String> invalid) {
		this.invalid = invalid;
	}
	
	

}