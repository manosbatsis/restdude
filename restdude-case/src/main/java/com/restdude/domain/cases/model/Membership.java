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

import com.restdude.domain.audit.model.AbstractBasicAuditedModel;
import com.restdude.domain.cases.IMembership;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractReadOnlyPersistableModelController;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import com.restdude.mdd.util.EntityUtil;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * {@value #API_MODEL_DESCRIPTION}
 */
@Entity
@Table(name = "context_membership")
@ModelResource(pathFragment = Membership.API_PATH_FRAGMENT,
	controllerSuperClass = AbstractReadOnlyPersistableModelController.class,
	apiName = "Context Memberships",
	apiDescription = "Operations about context memberships")
@ApiModel(description = Membership.API_MODEL_DESCRIPTION)
public class Membership extends AbstractBasicAuditedModel implements IMembership {

	public static final String API_PATH_FRAGMENT = "contextMemberships";
	public static final String API_MODEL_DESCRIPTION = "An entity model representing a membership to some context";

	@NotNull
	@Column(name = "is_admin", nullable = false)
	@Getter
	@Setter
	private Boolean admin = Boolean.FALSE;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "context", nullable = false, updatable = false)
	@Getter @Setter
	private BaseContext context;

	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	@Getter
	@Setter
	private User user;

	public Membership(){
		super();
	}
	
	public Membership(Space context, User user) {
		super();
		this.context = context;
		this.user = user;
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 13).appendSuper(super.hashCode()).append(EntityUtil.idOrNEmpty(this.getContext())).append(EntityUtil.idOrNEmpty(this.getUser())).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!Membership.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		Membership other = (Membership) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.appendSuper(super.equals(obj));
		builder.append(EntityUtil.idOrNEmpty(this.getContext()), EntityUtil.idOrNEmpty(this.getContext()));
		builder.append(EntityUtil.idOrNEmpty(this.getUser()), EntityUtil.idOrNEmpty(other.getUser()));
		return builder.isEquals();
	}


	@Override
	public String toString() {

		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("user", this.getUser())
				.append("admin", this.getAdmin()).toString();
	}

	public static class Builder {
		private Space context1;
		private User user;

		public Builder context(Space context1) {
			this.context1 = context1;
			return this;
		}

		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public Membership build() {
			return new Membership(this);
		}
	}

	private Membership(Builder builder) {
		this.context = builder.context1;
		this.user = builder.user;
	}
}
