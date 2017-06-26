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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "context_cases_app")
public class SpaceCasesApp extends SpaceApp {

	public static final String API_PATH_FRAGMENT = "caseApplications";
	public static final String API_MODEL_DESCRIPTION = "A model representing an case management application of a some context";


	@Formula(" (select count(*) from case_base where case_base.parent_app = id ) ")
	@ApiModelProperty(value = "The number of open cases")
	@Getter @Setter
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer openCasesCount = 0;

	@NotNull
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
	@Getter @Setter
	@ApiModelProperty(value = "The workflow for this business context")
	private CaseWorkflow workflow;


	@JsonIgnore
	@OneToMany(mappedBy = "parentApplication", orphanRemoval = true)
	@ApiModelProperty(value = "The case entries for this app")
	@Getter @Setter
	private List<BaseCase> cases;

	public SpaceCasesApp(){
		super();
	}

	public SpaceCasesApp(String name, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner,
						 Set<Membership> memberships, List<MembershipRequest> membershipRequests, CaseWorkflow workflow) {
		super(name, title, description, avatarUrl, bannerUrl, visibility, owner, memberships, membershipRequests);
		this.workflow = workflow;
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
		this.setDetail(builder.description);
		this.setOwner(builder.owner);
		this.setVisibility(builder.visibility);

	}

}
