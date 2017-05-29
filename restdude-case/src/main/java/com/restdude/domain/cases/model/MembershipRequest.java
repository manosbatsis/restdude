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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.cases.IMembershipRequest;
import com.restdude.domain.cases.model.enums.MembershipRequestStatus;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import com.restdude.mdd.util.EntityUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
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
@Table(name = "membership_request")
@ModelResource(pathFragment = MembershipRequest.API_PATH_FRAGMENT,
		apiName = "Membership Requests",
		apiDescription = "Requests to join a BusinessContext")
@ApiModel(description = MembershipRequest.API_MODEL_DESCRIPTION)
public class MembershipRequest extends AbstractSystemUuidPersistableModel implements IMembershipRequest {

	public static final String API_PATH_FRAGMENT = "contextMembershipsRequests";
	public static final String API_MODEL_DESCRIPTION = "A model representing a user request or invitation to join a Website.";

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@ApiModelProperty(value = "The membership status", allowableValues = "SENT_REQUEST, SENT_INVITE, CONFIRMED, BLOCK_REQUEST, BLOCK_INVITE, DELETE", required = true)
	@Getter @Setter
	private MembershipRequestStatus status;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "context", nullable = false, updatable = false)
	@ApiModelProperty(value = "The BusinessContext this membership request is for", required = true)
	@Getter @Setter
	private Space context;

	@NotNull
	@ManyToOne
	@JoinColumn(referencedColumnName = "pk", nullable = false, updatable = false)
	@ApiModelProperty(value = "The User this membership is appointed to", required = true)
	private User user;

	public MembershipRequest() {
		super();
	}


	@JsonIgnore
	public User getUser() {
		return user;
	}

	@JsonGetter("user")
	public UserDTO getUserDTO() {
		return UserDTO.fromUser(this.user);
	}

	@JsonProperty
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("user", this.getUser())
				.append("status", this.getStatus()).toString();
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
		if (!MembershipRequest.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		MembershipRequest other = (MembershipRequest) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.appendSuper(super.equals(obj));
		builder.append(EntityUtil.idOrNEmpty(this.getContext()), EntityUtil.idOrNEmpty(other.getContext()));
		builder.append(EntityUtil.idOrNEmpty(this.getUser()), EntityUtil.idOrNEmpty(other.getUser()));
		return builder.isEquals();
	}


	public static class Builder {
		private String id;
		private MembershipRequestStatus status;
		private Space context;
		private User user;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder status(@NonNull MembershipRequestStatus status) {
			this.status = status;
			return this;
		}

		public Builder context(@NonNull Space context) {
			this.context = context;
			return this;
		}

		public Builder user(@NonNull User user) {
			this.user = user;
			return this;
		}

		public MembershipRequest build() {
			return new MembershipRequest(this);
		}
	}

	private MembershipRequest(Builder builder) {
		this.setPk(builder.id);
		this.status = builder.status;
		this.context = builder.context;
		this.user = builder.user;
	}

}
