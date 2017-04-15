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
import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.websocket.message.MessageResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Slf4j
@ApiModel(description = "A lightweight DTO version of BaseContextfor http/websocket JSON serialization")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseContextInfo extends MessageResource<String> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseContextInfo.class);

	public static BaseContextInfo from(BaseContext resource) {
		return resource != null ? new BaseContextInfo(resource) : null;
	}

	@Getter @Setter
	private String title;
	@Getter @Setter
	private String description;
	@Getter @Setter
	private String avatarUrl;
	@Getter @Setter
	private String bannerUrl;
	@Getter @Setter
	@ApiModelProperty(value = "BusinessContext owner details")
	private UserDTO owner;
	@Getter @Setter
	private LocalDateTime lastModifiedDate;
	@Getter @Setter
	private ContextVisibilityType visibility;
	
	public BaseContextInfo() {
		super();
	}


	public BaseContextInfo(String id, String name, String title, String description, String avatarUrl, String bannerUrl, UserDTO owner,
						   ContextVisibilityType visibility, LocalDateTime lastModifiedDate) {
		super(id, name);
		this.description = description;
		this.title = title;
		this.avatarUrl = avatarUrl;
		this.bannerUrl = bannerUrl;
		this.owner = owner;
		this.lastModifiedDate = lastModifiedDate;
		this.visibility = visibility;
	}

	public BaseContextInfo(String id, String name, String title, String description, String avatarUrl, String bannerUrl,
						   String ownerId, String ownerFirstName, String ownerLastName, String ownerUsername, String ownerEmail, String ownerEmailHash, String ownerAvatarUrl,
						   String ownerBannerUrl, ContextVisibilityType visibility) {
		this(id, name, title, description, avatarUrl, bannerUrl,
				ownerId, ownerFirstName, ownerLastName, ownerUsername, ownerEmail, ownerEmailHash, ownerAvatarUrl,
				ownerBannerUrl, visibility, null);
	}

	public BaseContextInfo(String id, String name, String title, String description, String avatarUrl, String bannerUrl,
						   String ownerId, String ownerFirstName, String ownerLastName, String ownerUsername, String ownerEmail, String ownerEmailHash, String ownerAvatarUrl,
						   String ownerBannerUrl, ContextVisibilityType visibility, LocalDateTime lastModifiedDate) {
		this(id, name, title, description, avatarUrl, bannerUrl,
				new UserDTO(ownerId, ownerFirstName, ownerLastName, ownerUsername, ownerEmail, ownerEmailHash, ownerAvatarUrl, ownerBannerUrl, null),
				visibility, lastModifiedDate);
	}

	private BaseContextInfo(BaseContext resource) {
		this(resource.getPk(), resource.getName(), resource.getTitle(), resource.getDescription(), resource.getAvatarUrl(), resource.getBannerUrl(), UserDTO.fromUser(resource.getOwner()),
				resource.getVisibility(), null);
	}
	

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("id", this.id)
				.append("name", this.name)
				.append("title", this.title)
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
		if(obj instanceof BaseContextInfo){
			final BaseContextInfo other = (BaseContextInfo) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.append(description, other.description)
					.isEquals();
		} else{
			return false;
		}
	}



}
