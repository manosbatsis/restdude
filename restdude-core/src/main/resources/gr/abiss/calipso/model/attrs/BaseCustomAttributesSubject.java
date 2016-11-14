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

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract base class for custom attribute implementations
 */
@Entity
@Table(name = "base_custom_attributes_entity")
public abstract class BaseCustomAttributesSubject extends
		AbstractAuditable<User> {

	private static final long serialVersionUID = -1036398460892888717L;

	@JsonIgnore
	@OneToMany(mappedBy = "subject", targetEntity = CustomAttribute.class, fetch = FetchType.LAZY)
	@MapKey(name = "name")
	private Map<String, CustomAttribute<? extends Serializable>> customAttributes;

	public Map<String, CustomAttribute<? extends Serializable>> getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(
			Map<String, CustomAttribute<? extends Serializable>> customAttributes) {
		this.customAttributes = customAttributes;
	}

}
