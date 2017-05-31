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
package com.restdude.mdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract base class for persistent entities with a System UUID primary key
 */
@MappedSuperclass
public abstract class AbstractSystemUuidPersistableModel extends AbstractPersistableModel<String> {

	private static final long serialVersionUID = -5418849804520876406L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	/**
     * {@inheritDoc}
     */
	@Override
	public String getId() {
		return id;
	}

	/**
     * {@inheritDoc}
     */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
    @JsonIgnore
    public boolean isNew() {
		return null == this.getId();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void preSave() {

    }
}