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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.validation.Unique;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.CurrentPrincipal;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Abstract entity class with basic auditing.
 * @param <ID> The id Serializable
 */
@XmlRootElement
@MappedSuperclass
@Unique
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractPersistable<ID extends Serializable> implements CalipsoPersistable<ID> {

	private static final long serialVersionUID = -6009587976502456848L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistable.class);

    public static interface ItemView {}
    public static interface CollectionView {}

	@CreatedDate
	@DiffIgnore
	@ApiModelProperty(value = "Date created")
	@Column(name = "date_created", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@DiffIgnore
	@ApiModelProperty(value = "Date last modified")
	@Column(name = "date_last_modified", nullable = false)
	private LocalDateTime lastModifiedDate;

	@CreatedBy
	@DiffIgnore
	@JsonIgnore
	@CurrentPrincipal
	@ApiModelProperty(value = "Created by", readOnly = true, hidden = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdby_id", referencedColumnName = "id", updatable = false)
	private User createdBy;

	@LastModifiedBy
	@DiffIgnore
	@JsonIgnore
	@ApiModelProperty(value = "Last modified by", readOnly = true, hidden = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifiedby_id", referencedColumnName = "id")
	private User lastModifiedBy;


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
		if (!(obj instanceof AbstractPersistable)) {
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

	/**
	 * {@inheritDoc}
	 */
	public void preSave() {

	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}