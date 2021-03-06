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

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restdude.domain.cases.model.BaseCase;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.websocket.message.MessageResource;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@ApiModel(description = "A lightweight DTO version of CaseStatus for  http/websocket JSON serialization")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseInfo extends MessageResource<String> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CaseInfo.class);


	public static CaseInfo from(BaseCase resource) {
		return resource != null ? new CaseInfo(resource) : null;
	}


	@Getter @Setter
	private String title;
	@Getter @Setter
	private String detail;
	@Getter @Setter
	private LocalDateTime createdDate;
	@Getter @Setter
	private UserDTO author;


	public CaseInfo() {
		super();
	}


	public CaseInfo(BaseCase resource) {
		this(resource.getId(), resource.getName(), resource.getTitle(), resource.getDetail(), resource.getCreatedDate(), UserDTO.fromUser(resource.getCreatedBy()));
	}

	public CaseInfo(String id, String name, String title, String detail, LocalDateTime createdDate, UserDTO user) {
		super(id, name);
		this.setTitle(title);
		this.setDetail(detail);
		this.setCreatedDate(createdDate);
		this.setAuthor(user);

	}
	

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("id", this.id)
				.append("name", this.name).toString();
	}

	@Override
	public int hashCode(){
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj){
		if(obj instanceof CaseInfo){
			final CaseInfo other = (CaseInfo) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.isEquals();
		} else{
			return false;
		}
	}



}
