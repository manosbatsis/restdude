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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.User;
import com.restdude.util.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

/**
 * {@value #API_MODEL_DESCRIPTION}
 */
@Entity
@Table(name = "business_context")
/*@ModelResource(pathFragment = BusinessContext.API_PATH_FRAGMENT,
		apiName = "BusinessContext",
		apiDescription = "BusinessContext management")
@ApiModel(description = BusinessContext.API_MODEL_DESCRIPTION)*/
public class BusinessContext extends MembershipContext {

	public static final String API_PATH_FRAGMENT = "businessContexts";
	public static final String API_MODEL_DESCRIPTION = "A model representing a business context, such as an organization, team, or process type.";

	@OneToMany(mappedBy = "maintainerContext")
	@JsonIgnore
	@Getter @Setter
	@ApiModelProperty(value = "The workflows owned by this business context")
	private List<CaseWorkflow> workflows;
/*
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	@JsonIgnore
	@Getter @Setter
	@ApiModelProperty(value = "The applications owned by this business context")
	private List<CasesApplication> applications;

	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	@JsonIgnore
	@Getter @Setter
	@ApiModelProperty(value = "The business contexts owned by this business context")
	private List<BusinessContext> subContexts;
*/
	public BusinessContext() {
	}

	public BusinessContext(String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner, Set<Membership> memberships, List<MembershipRequest> membershipRequests, List<CaseWorkflow> workflows) {
		super(title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
		this.workflows = workflows;
	}

	public BusinessContext(String name, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner, Set<Membership> memberships, List<MembershipRequest> membershipRequests, List<CaseWorkflow> workflows) {
		super(name, title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
		this.workflows = workflows;
	}

	public BusinessContext(String name, BusinessContext parent, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner, Set<Membership> memberships, List<MembershipRequest> membershipRequests, List<CaseWorkflow> workflows) {
		super(name, parent, title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
		this.workflows = workflows;
	}

	public static class Builder {
		private String name;
		private String title;
		private String description;
		private String avatarUrl = Constants.DEFAULT_AVATAR_URL;
		private String bannerUrl = Constants.DEFAULT_BANNER_URL;
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

		public BusinessContext build() {
			return new BusinessContext(this);
		}
	}

	private BusinessContext(Builder builder) {
		this.setName(builder.name);
		this.setTitle(builder.title);
		this.setDescription(builder.description);
		this.setAvatarUrl(builder.avatarUrl);
		this.setBannerUrl(builder.bannerUrl);
		this.setOwner(builder.owner);
		this.setVisibility(builder.visibility);
	}

}
