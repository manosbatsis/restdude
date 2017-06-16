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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restdude.domain.cases.model.AbstractCaseComment;
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

import java.time.LocalDateTime;

@Slf4j
@ApiModel(description = "A lightweight DTO version of CaseStatus for  http/websocket JSON serialization")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseCommenttInfo extends MessageResource<String> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CaseCommenttInfo.class);



	public static CaseCommenttInfo from(AbstractCaseComment resource) {
		return resource != null ? new CaseCommenttInfo(resource) : null;
	}

	@Getter @Setter
	private String content;
	@Getter @Setter
	private LocalDateTime createdDate;
	@Getter @Setter
	private UserDTO author;


	public CaseCommenttInfo() {
		super();
	}

	public CaseCommenttInfo(AbstractCaseComment resource) {
		this(resource.getId(), resource.getContent(), resource.getCreatedDate(), UserDTO.fromUser(resource.getCreatedBy()));
	}

	public CaseCommenttInfo(
			String id, String content, LocalDateTime createdDate,
			String authorId, String authorFirstName, String authorLastName, String authorUsername, String authorEmailHash, String authorAvatarUrl) {
		super(id, null);
		this.setContent(content);
		this.setCreatedDate(createdDate);
		this.setAuthor(new UserDTO(authorId, authorFirstName, authorLastName, authorUsername, authorEmailHash, authorAvatarUrl));

	}

	public CaseCommenttInfo(
			String id, String content, LocalDateTime createdDate,
			UserDTO author) {
		super(id, null);
		this.setContent(content);
		this.setCreatedDate(createdDate);
		this.setAuthor(author);

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
		if(obj instanceof CaseCommenttInfo){
			final CaseCommenttInfo other = (CaseCommenttInfo) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.isEquals();
		} else{
			return false;
		}
	}



}
