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

import org.hibernate.annotations.Formula;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract base class for persistent entities with assigned id
 * @param <ID> The id Serializable
 */
@MappedSuperclass
public abstract class AbstractAssignedidPersistable<ID extends Serializable>  implements CalipsoPersistable<ID>{

	private static final long serialVersionUID = 4340156130534111231L;

	@Id
	private ID id;

	@Formula(" (id) ")
	private ID savedId;

	public AbstractAssignedidPersistable(){
		
	}

	public AbstractAssignedidPersistable(ID id){
		this.id = id;
	}
	
	/**
	 * Get the entity's primary key 
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public ID getId() {
		return id;
	}
	
	/**
	 * Set the entity's primary key
	 * @param id the id to set
	 */
	public void setId(ID id) {
		this.id = id;
	}
	
	private ID getSavedId() {
		return savedId;
	}

	/**
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return null == getSavedId();
	}


}