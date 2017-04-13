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
package com.restdude.domain.cases.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restdude.domain.cases.model.SpaceContext;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.websocket.message.MessageResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApiModel(description = "A lightweight DTO version of BusinessContext used for websocket messaging")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceContextInfo extends MessageResource<String> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SpaceContextInfo.class);

	public static SpaceContextInfo from(SpaceContext resource) {
		return new SpaceContextInfo(resource);
	}

	private String description;
	private String avatarUrl;
	private String bannerUrl;
	@ApiModelProperty(value = "BusinessContext owner details")
	private UserDTO owner;
	private LocalDateTime lastModifiedDate;

	private ContextVisibilityType visibility;
	
	public SpaceContextInfo() {
		super();
	}


	public SpaceContextInfo(String id, String name, String description, String avatarUrl, String bannerUrl, UserDTO owner, ContextVisibilityType visibility, LocalDateTime lastModifiedDate) {
		super(id, name);
		this.description = description;
		this.avatarUrl = avatarUrl;
		this.bannerUrl = bannerUrl;
		this.owner = owner;
		this.lastModifiedDate = lastModifiedDate;
		this.visibility = visibility;
	}

	public SpaceContextInfo(String id, String name, String description, String avatarUrl, String bannerUrl,
								 String ownerId, String ownerFirstName, String ownerLastName, String ownerUsername, String ownerEmail, String ownerEmailHash, String ownerAvatarUrl,
								 String ownerBannerUrl, Integer ownerStompSessionCount, ContextVisibilityType visibility) {
		this(id, name, description, avatarUrl, bannerUrl,
				ownerId, ownerFirstName, ownerLastName, ownerUsername, ownerEmail, ownerEmailHash, ownerAvatarUrl,
				ownerBannerUrl, ownerStompSessionCount, visibility, null);
	}

	public SpaceContextInfo(String id, String name, String description, String avatarUrl, String bannerUrl,
								 String ownerId, String ownerFirstName, String ownerLastName, String ownerUsername, String ownerEmail, String ownerEmailHash, String ownerAvatarUrl,
								 String ownerBannerUrl, Integer ownerStompSessionCount, ContextVisibilityType visibility, LocalDateTime lastModifiedDate) {
		this(id, name, description, avatarUrl, bannerUrl,
				new UserDTO(ownerId, ownerFirstName, ownerLastName, ownerUsername, ownerEmail, ownerEmailHash, ownerAvatarUrl, ownerBannerUrl, ownerStompSessionCount), visibility, lastModifiedDate);
	}

	private SpaceContextInfo(SpaceContext resource) {
		this(resource.getPk(), resource.getName(), resource.getDescription(), resource.getAvatarUrl(), resource.getBannerUrl(),  UserDTO.fromUser(resource.getOwner()), resource.getVisibility(), null);
	}
	

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("id", this.id)
				.append("name", this.name)
				.append("description", this.description)
				.append("avatarUrl", this.avatarUrl)
				.append("bannerUrl", this.bannerUrl)
				.append("owner", this.owner)
				.append("visibility", this.visibility).toString();
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(description)
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj){
		if(obj instanceof SpaceContextInfo){
			final SpaceContextInfo other = (SpaceContextInfo) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.append(description, other.description)
					.isEquals();
		} else{
			return false;
		}
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}


	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}


}
