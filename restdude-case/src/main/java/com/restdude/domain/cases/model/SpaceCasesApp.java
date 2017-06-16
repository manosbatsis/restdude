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
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "context_cases_app")
public class SpaceCasesApp<C extends AbstractCase> extends SpaceApp {

	public static final String API_PATH_FRAGMENT = "caseApplications";
	public static final String API_MODEL_DESCRIPTION = "A model representing an case management application of a some context";

	@NotNull
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
	@Getter @Setter
	@ApiModelProperty(value = "The workflow for this business context")
	private CaseWorkflow workflow;

	@JsonIgnore
	@Getter @Setter
	@ApiModelProperty(value = "The cases owned by this business application")
	@OneToMany(mappedBy="application")
	@OrderBy("entryIndex ASC")
	private List<C> cases;

	public List<C> getCases() {
		return cases;
	}

	public void setCases(List<C> cases) {
		this.cases = cases;
	}

	public SpaceCasesApp(){
	}

	public SpaceCasesApp(String name, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner,
						 Set<Membership> memberships, List<MembershipRequest> membershipRequests, CaseWorkflow workflow, List<C> cases) {
		super(name, title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
		this.workflow = workflow;
		this.cases = cases;
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

		public Builder memberships(Set<Membership> memberships) {
			this.memberships = memberships;
			return this;
		}

		public SpaceCasesApp build() {
			return new SpaceCasesApp(this);
		}
	}

	private SpaceCasesApp(Builder builder) {
		this.setName(builder.name);
		this.setTitle(builder.title);
		this.setDescription(builder.description);
		this.setOwner(builder.owner);
		this.setVisibility(builder.visibility);

	}

}
