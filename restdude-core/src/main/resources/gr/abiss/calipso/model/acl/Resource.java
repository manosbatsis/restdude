/**
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Calipso, a software platform by www.Abiss.gr.
 *
 * Calipso is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Calipso is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Calipso. If not, see http://www.gnu.org/licenses/agpl.html
 */
package gr.abiss.calipso.model.acl;

import gr.abiss.calipso.model.entities.AbstractAuditable;
import com.restdude.domain.users.model.User;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public abstract class Resource extends AbstractAuditable<User> {

	/**
	 * Get a human and URL friendly id such as a username, a filename etc.
	 * 
	 * @return the friendly id.
	 */
	public abstract String getBusinessKey();

	/**
	 * The API base path e.g. "/resource" or "/users". This should return a
	 * string if the resource is retreivable from the REST API (e.g.
	 * "/users/{businessKey|id}") or null otherwise.
	 * 
	 * @return the base path string
	 */
	public abstract String getApiBasePath();

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Resource)) {
			return false;
		}
		Resource that = (Resource) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.appendSuper(super.hashCode());
		builder.append(getBusinessKey());
		return builder.toHashCode();
	}

}
