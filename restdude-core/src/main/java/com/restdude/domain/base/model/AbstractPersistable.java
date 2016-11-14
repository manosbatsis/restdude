/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.model;

import com.restdude.domain.users.model.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract base class for all persistent entities.
 * @param <ID> The id Serializable
 */
@MappedSuperclass
public abstract class AbstractPersistable<ID extends Serializable> implements CalipsoPersistable<ID> {

	private static final long serialVersionUID = -6009587976502456848L;

    public static interface ItemView {}
    public static interface CollectionView {}

	public AbstractPersistable() {
		super();
	}
	
	public AbstractPersistable(ID id) {
		this.setId(id);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.getId()).toString();
	}

	/**
	 * Get the entity's primary key 
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public abstract ID getId();

	/**
	 * Set the entity's primary key
	 * @param id the id to set
	 */
	public abstract void setId(ID id);

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
		if (!(obj instanceof User)) {
			return false;
		}
		AbstractPersistable other = (AbstractPersistable) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.getId(), other.getId());
		return builder.isEquals();
	}

	/**
	 *  @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getId() ? 0 : getId().hashCode() * 31;
		return hashCode;
	}
}