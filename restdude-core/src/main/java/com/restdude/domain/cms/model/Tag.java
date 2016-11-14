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
package com.restdude.domain.cms.model;

import com.restdude.domain.base.model.AbstractCategory;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Tags are used for content categorization. They are hierarchical and can have aliases
 */
@Entity
@Table(name = "content_tag")
public class Tag extends AbstractCategory<Tag> {

	public Tag(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -3020367940457381316L;

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tag)) {
			return false;
		}
		Tag that = (Tag) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

}
