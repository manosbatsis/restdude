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
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.controller.AbstractPersistableModelController;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.hateoas.core.Relation;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 */
@ModelResource(pathFragment = "roles", controllerSuperClass = AbstractPersistableModelController.class,
	apiName = "Roles", apiDescription = "Operations about roles")
@Entity
@Table(name = "role")
@Inheritance(strategy = InheritanceType.JOINED)
@Relation(value = "role", collectionRelation = "roles")
@ApiModel(value = "Role", description = "User principal roles. Roles are principals themselves and can be assigned to users.")
public class Role extends AbstractSystemUuidPersistableModel implements GrantedAuthority {

	private static final long serialVersionUID = 3558291745762331656L;


	@NotNull
    @Column(unique = true, nullable = false)
	private String name;
	
	@Column(length = 510)
	private String description;

	@JsonIgnore
	@DiffIgnore 
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Collection<User> members = new ArrayList<User>(0);


	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public Role(String name, String description) {
		this(name);
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		Role that = (Role) obj;
		return null == this.getName() ? false : this.getName().equals(that.getName());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", this.getName()).toString();
	}

	/** 
	 *  {@inheritDoc}
	 */
	@Override
	public String getAuthority() {
		return this.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<User> getMembers() {
		return members;
	}

	public void setMembers(Collection<User> members) {
		this.members = members;
	}

}
