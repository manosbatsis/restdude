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

import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * A base class for path-like resource entities: files, folders, categories etc.
 */
@MappedSuperclass
public abstract class AbstractNamedResource extends AbstractSystemUuidPersistable {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", length = 500, nullable = false)
	private String name;

	public AbstractNamedResource() {
		super();
	}

	public AbstractNamedResource(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!AbstractNamedResource.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		AbstractNamedResource other = (AbstractNamedResource) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.appendSuper(super.equals(obj));
		builder.append(this.getName(), other.getName());
		return builder.isEquals();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}