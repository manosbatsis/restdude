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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "MembershipContext invitation list results", description = "MembershipContextInvitationsResult is a model reporting  the submission results (see properties).")
public class MembershipContextInvitationsResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The ID of the BusinessContext this invitation list applies to. ")
	private String contextId;

	@ApiModelProperty(value = "The recipients that were successfully invited.")
	private Set<String> invited;

	@ApiModelProperty(value = "Recipients not found or otherwise invalid.")
	private Set<String> invalid;

	@ApiModelProperty(value = "Recipients that are already members.")
	private Set<String> members;

	@ApiModelProperty(value = "Recipients that had already sent a request and where thus automatically approved.")
	private Set<String> approved;

	@ApiModelProperty(value = "Recipients that have already received a request that is still pending.")
	private Set<String> pending;

	@ApiModelProperty(value = "Recipients the system failed processing.")
	private Set<String> failed;

	public MembershipContextInvitationsResult() {
		super();
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public Set<String> getInvited() {
		return invited;
	}

	public void setInvited(Set<String> invited) {
		this.invited = invited;
	}

	public Set<String> getInvalid() {
		return invalid;
	}

	public void setInvalid(Set<String> invalid) {
		this.invalid = invalid;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}

	public Set<String> getApproved() {
		return approved;
	}

	public void setApproved(Set<String> approved) {
		this.approved = approved;
	}

	public Set<String> getPending() {
		return pending;
	}

	public void setPending(Set<String> pending) {
		this.pending = pending;
	}

	public Set<String> getFailed() {
		return failed;
	}

	public void setFailed(Set<String> failed) {
		this.failed = failed;
	}

	public static class Builder {
		private String contextId;
		private Set<String> invited;
		private Set<String> invalid;
		private Set<String> members;
		private Set<String> approved;
		private Set<String> pending;
		private Set<String> failed;


		public Builder businessContextId(String businessContextId) {
			this.contextId = businessContextId;
			return this;
		}

		public Builder invited(String ccItem) {
			if(this.invited == null){
				this.invited = new HashSet<String>();
			}
			this.invited.add(ccItem);
			return this;
		}

		public Builder invalid(String ccItem) {
			if(this.invalid == null){
				this.invalid = new HashSet<String>();
			}
			this.invalid.add(ccItem);
			return this;
		}

		public Builder members(String ccItem) {
			if(this.members == null){
				this.members = new HashSet<String>();
			}
			this.members.add(ccItem);
			return this;
		}

		public Builder approved(String ccItem) {
			if(this.approved == null){
				this.approved = new HashSet<String>();
			}
			this.approved.add(ccItem);
			return this;
		}

		public Builder pending(String ccItem) {
			if(this.pending == null){
				this.pending = new HashSet<String>();
			}
			this.pending.add(ccItem);
			return this;
		}

		public Builder failed(String ccItem) {
			if(this.failed == null){
				this.failed = new HashSet<String>();
			}
			this.failed.add(ccItem);
			return this;
		}

		public MembershipContextInvitationsResult build() {
			return new MembershipContextInvitationsResult(this);
		}
	}

	private MembershipContextInvitationsResult(Builder builder) {
		this.contextId = builder.contextId;
		this.invited = builder.invited;
		this.invalid = builder.invalid;
		this.members = builder.members;
		this.approved = builder.approved;
		this.pending = builder.pending;
		this.failed = builder.failed;
	}
}
