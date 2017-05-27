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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

/**
 * {@value #API_MODEL_DESCRIPTION}
 */
@Entity
@Table(name = "context_space")
@ModelResource(pathFragment = Space.API_PATH_FRAGMENT,
		apiName = "Space",
		apiDescription = "Spaces management")
@ApiModel(description = Space.API_MODEL_DESCRIPTION)
public class Space extends BaseContext {

	public static final String API_PATH_FRAGMENT = "spaces";
	public static final String API_MODEL_DESCRIPTION = "A model representing a space context, such as an organization or team";

	@ManyToOne
	private Space parent;


	@OneToMany(mappedBy = "space", orphanRemoval = true)
	@JsonIgnore
	@Getter
	@Setter
	@ApiModelProperty(value = "The apps owned by this space")
	private List<SpaceApp> applications;

	@OneToMany(mappedBy = "parent")
	@JsonIgnore
	@Getter
	@Setter
	@ApiModelProperty(value = "The sub-spaces owned by this space")
	private List<Space> children;

	@JsonIgnore
	public Space getParent() {
		return parent;
	}

	@JsonGetter("parent")
	public BaseContextInfo getParentDTO() {
		return BaseContextInfo.from(this.parent);
	}

	public void setParent(Space parent) {
		this.parent = parent;
	}

	public Space() {
	}

	public Space(String name, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner, Set<Membership> memberships, List<MembershipRequest> membershipRequests) {
		super(name, title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
	}

//sudo ember generate scaffold space name:string title:string description:string avatarUrl:string visibility:string
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("name", this.getName())
				.append("visibility", this.getVisibility())
				.append("owner", this.getOwner())
				.toString();
	}

	public static class Builder {
		private String name;
		private String title;
		private String description;
		private String avatarUrl;
		private String bannerUrl;
		private User owner;
		private ContextVisibilityType visibility;
		private Set<Membership> memberships;

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

		public Builder avatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
			return this;
		}

		public Builder bannerUrl(String bannerUrl) {
			this.bannerUrl = bannerUrl;
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

		public Space build() {
			return new Space(this);
		}
	}

	private Space(Builder builder) {
		this.setName(builder.name);
		this.setTitle(builder.title);
		this.setDescription(builder.description);
		this.setAvatarUrl(builder.avatarUrl);
		this.setBannerUrl(builder.bannerUrl);
		this.setOwner(builder.owner);
		this.setVisibility(builder.visibility);
	}

}
