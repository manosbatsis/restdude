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
package com.restdude.domain.metadata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;

/**
 * Abstract base persistent class for metadata entries. Implementations can
 * override relational specifics via javax.persistence.AssociationOverride
 * annotations
 */
@MappedSuperclass
@Table(uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "subject", "predicate" }) 
	})
public abstract class AbstractMetadatum<S extends MetadataSubject>
		extends AbstractSystemUuidPersistable implements Metadatum<S> {

	private static final long serialVersionUID = -1468517690700208260L;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "subject", nullable = false)
	private S subject;

	@Column(name = "predicate", nullable = false, insertable = true, updatable = false)
	private String predicate;

	@Column(name = "object")
	private String object;

	public AbstractMetadatum() {
		super();
	}

	public AbstractMetadatum(S subject, String predicate, String object) {
		super();
		this.predicate = predicate;
		this.object = object;
		this.subject = subject;
		// this.subject.addMetadatum(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!AbstractMetadatum.class.isInstance(obj)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		AbstractMetadatum that = (AbstractMetadatum) obj;
		return new EqualsBuilder()
				.append(this.getSubject(), that.getSubject())
				.append(this.getPredicate(), that.getPredicate()).isEquals();
	}
	
	@Override
	public S getSubject() {
		return subject;
	}

	@Override
	public void setSubject(S subject) {
		this.subject = subject;
	}

	@Override
	public String getPredicate() {
		return predicate;
	}

	@Override
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	@Override
	public String getObject() {
		return object;
	}

	@Override
	public void setObject(String object) {
		this.object = object;
	}

}