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

import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.enums.MembershipRequestStatus;
import com.restdude.websocket.message.IMessageResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Slf4j
@ApiModel(value = "BusinessContext Membership Request Info ", description = "BusinessContextMembershipRequestInfo is a lightweight DTO version of MembershipRequest used for websocket messaging")
public class BusinessContextMembershipRequestInfo implements IMessageResource<String>{

	private static final long serialVersionUID = 1L;

	public static BusinessContextMembershipRequestInfo from(MembershipRequest request){
		return new BusinessContextMembershipRequestInfo(request);
	}

	@ApiModelProperty(value = "The membership request ID")
	@Getter @Setter
	private String pk;

	@ApiModelProperty(value = "The ID of the BusinessContext this stream is inbound to.")
	@Getter @Setter
	private String businessContextId;

	@ApiModelProperty(value = "The title of the BusinessContext this request refers to.")
	@Getter @Setter
	private String name;

	@ApiModelProperty(value = "The description of the BusinessContext this request refers to.")
	@Getter @Setter
	private String businessContextDescription;

	@ApiModelProperty(value = "The ID of the user this request refers to.")
	@Getter @Setter
	private String userId;

	@ApiModelProperty(value = "The status of the request.")
	@Getter @Setter
	private MembershipRequestStatus status;

	public BusinessContextMembershipRequestInfo() {
		super();
	}

	public BusinessContextMembershipRequestInfo(String id, String businessContextId, String businessContextTitle, String businessContextDescription, String userId, MembershipRequestStatus status) {
		super();
		this.pk = id;
		this.businessContextId = businessContextId;
		this.name = businessContextTitle;
		this.businessContextDescription = businessContextDescription;
		this.userId = userId;
		this.status = status;
	}

	private BusinessContextMembershipRequestInfo(MembershipRequest request) {
		this(request.getPk(), request.getContext().getPk(), request.getContext().getTitle(), request.getContext().getDescription(), request.getUser().getPk(), request.getStatus());
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("businessContextId", this.businessContextId)
				.append("userId", this.userId)
				.append("status", this.status)
				.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessContextId == null) ? 0 : businessContextId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessContextMembershipRequestInfo other = (BusinessContextMembershipRequestInfo) obj;
		if (businessContextId == null) {
			if (other.businessContextId != null)
				return false;
		} else if (!businessContextId.equals(other.businessContextId))
			return false;

		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;

		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


}
