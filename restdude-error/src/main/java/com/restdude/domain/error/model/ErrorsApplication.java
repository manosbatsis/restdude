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
package com.restdude.domain.error.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.SpaceCasesApp;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;

/**
 * {@value #API_MODEL_DESCRIPTION}
 */

@Entity
@Table(name = "errors_app")
@ApiModel(description = ErrorsApplication.API_MODEL_DESCRIPTION)
@ModelResource(pathFragment = ErrorsApplication.API_PATH_FRAGMENT,
		apiName = "ErrorsApplication",
		apiDescription = "ErrorsApplication management")
public class ErrorsApplication extends SpaceCasesApp {

	public static final String API_PATH_FRAGMENT = "errorApplications";
	public static final String API_MODEL_DESCRIPTION = "A model representing an Errors Management process";


	public ErrorsApplication(){
	}




	public static class Builder {
		private String name;
		private String title;
		private String description;
		private User owner;
		private ContextVisibilityType visibility = ContextVisibilityType.CLOSED;
		private Space space;
		private CaseWorkflow workflow;

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

		public Builder space(Space space) {
			this.space = space;
			return this;
		}

		public Builder visibility(ContextVisibilityType visibility) {
			this.visibility = visibility;
			return this;
		}

		public Builder workflow(CaseWorkflow workflow) {
			this.workflow = workflow;
			return this;
		}

		public ErrorsApplication build() {
			return new ErrorsApplication(this);
		}
	}

	private ErrorsApplication(Builder builder) {
		this.setName(builder.name);
		this.setTitle(builder.title);
		this.setDetail(builder.description);
		this.setOwner(builder.owner);
		this.setParent(builder.space);
		this.setVisibility(builder.visibility);
		this.setWorkflow(builder.workflow);

	}

}
