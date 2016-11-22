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
package gr.abiss.calipso.model.attrs;


import gr.abiss.calipso.model.entities.AbstractAuditable;
import com.restdude.domain.users.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "custom_attribute")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CustomAttributeDefinition extends
 AbstractAuditable<User> {

	private static final long serialVersionUID = 4008152148196369725L;

	@NotNull
	@Column(name = "name", nullable = false, updatable = false)
	private String name;

	@NotNull
	@Column(name = "className", nullable = false, updatable = false)
	private String className;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "subject", referencedColumnName = "id", nullable = false)
	private BaseCustomAttributesSchema subject;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BaseCustomAttributesSchema getSubject() {
		return subject;
	}

	public void setSubject(BaseCustomAttributesSchema subject) {
		this.subject = subject;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
