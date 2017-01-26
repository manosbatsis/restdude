/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.base.model;

import org.hibernate.annotations.Formula;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract base class for persistent entities with assigned pk
 * @param <ID> The pk Serializable
 */
@MappedSuperclass
public abstract class AbstractAssignedIdPersistable<ID extends Serializable> extends AbstractPersistable<ID> {

	private static final long serialVersionUID = 4340156130534111231L;

	@Id
	private ID pk;

	@Formula(" (pk) ")
	private ID savedPk;

    public AbstractAssignedIdPersistable() {

    }

	public AbstractAssignedIdPersistable(ID pk) {
		this.pk = pk;
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public ID getPk() {
		return pk;
	}

	/**
	 *{@inheritDoc}
	 */
    @Override
    public void setPk(ID pk) {
		this.pk = pk;
	}


    private ID getSavedPk() {
		return savedPk;
	}

	/**
     * @inheritDoc}
     */
	@Override
	public boolean isNew() {
		return null == getSavedPk();
	}

	/**
	 * {@inheritDoc}
	 */
	public void preSave() {

	}
}