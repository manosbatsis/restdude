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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "access_mode")
public class AccessMode implements Persistable<Short> {

	private static final long serialVersionUID = 6007713619677213125L;

	/**
	 * The primary key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "weight", unique = true, nullable = false)
	private Short weight;

	/**
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public Short getId() {
		return this.id;
	}

	/**
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Override
	public boolean isNew() {

		return null == getId();
	}
	// READ, WRITE, WRITE_MANDATORY, WRITE_MANDATORY_IF_EMPTY, APPEND,
	// APPEND_MANDATORY, APPEND_MANDATORY_IF_EMPTY, CONTROL;
}
