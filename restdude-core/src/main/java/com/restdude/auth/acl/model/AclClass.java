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
package com.restdude.auth.acl.model;

import com.restdude.domain.base.model.CalipsoPersistable;

import javax.persistence.*;

@Entity
@Table(name = "acl_class")
public class AclClass implements CalipsoPersistable<Long> {

	private static final long serialVersionUID = -8538893700776925998L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "class", unique = true)
	private String className;

	public AclClass() {
		super();
	}

	public AclClass(Long id, String clazz) {
		super();
		this.id = id;
		this.className = clazz;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public Class getClassObject() {
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clazz;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public boolean isNew() {
		return this.getId() == null;
	}

}
