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
package com.restdude.domain.cases.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * {@value #API_MODEL_DESCRIPTION}
 */
@Entity
@Table(name = "context_app")
@ModelResource(pathFragment = SpaceApp.API_PATH_FRAGMENT,
		apiName = "ApplicationContext",
		apiDescription = "Space applications")
@ApiModel(description = SpaceApp.API_MODEL_DESCRIPTION)
public class SpaceApp extends BaseContext {


	public static final String API_PATH_FRAGMENT = "spaceApps";
	public static final String API_MODEL_DESCRIPTION = "A model representing an application context, such as case management or other process type.";


	@ApiModelProperty(hidden = true)
	@ManyToOne
	private Space space;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	public Space getSpace() {
		return space;
	}

	@ApiModelProperty(hidden = true)
	@JsonGetter("space")
	public BaseContextInfo getSpaceDTO() {
		return BaseContextInfo.from(this.space);
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public SpaceApp() {
		super();
	}


	public SpaceApp(String name, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner, Set<Membership> memberships, List<MembershipRequest> membershipRequests) {
		super(name, title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preSave() {
		if(Objects.isNull(this.getSpace())){
			this.setSpace((Space) this.getParent());
		}
		if(Objects.isNull(this.getParent())){
			this.setParent(this.getSpace());
		}
		super.preSave();
	}

	public static class Builder {
		private String name;
		private String title;
		private String description;
		private User owner;
		private Space space;
		private ContextVisibilityType visibility;
		private Set<Membership> memberships;

		public Builder space(Space space) {
			this.space = space;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder owner(User owner) {
			this.owner = owner;
			return this;
		}

		public Builder visibility(ContextVisibilityType visibility) {
			this.visibility = visibility;
			return this;
		}

		public SpaceApp build() {
			return new SpaceApp(this);
		}
	}

	private SpaceApp(Builder builder) {
		this.setParent(builder.space);
		this.setName(builder.name);
		this.setTitle(builder.title);
		this.setDetail(builder.description);
		this.setOwner(builder.owner);
		this.setVisibility(builder.visibility);
	}

}
