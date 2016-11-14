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
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 */
@ModelResource(path = "roles", controllerSuperClass = AbstractModelController.class, 
	apiName = "Roles", apiDescription = "Operations about roles")
@Entity
@Table(name = "role")
@Inheritance(strategy = InheritanceType.JOINED)
@ApiModel(value = "Role", description = "User principal roles. Roles are principals themselves and can be assigned to users.")
public class Role extends AbstractSystemUuidPersistable implements GrantedAuthority {

	private static final long serialVersionUID = 3558291745762331656L;
	
	public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
	public static final String PRE_AUTHORIZE_CREATE = "hasRole('ROLE_ADMIN')";
	public static final String PRE_AUTHORIZE_UPDATE = "hasRole('ROLE_ADMIN')";
	public static final String PRE_AUTHORIZE_PATCH = "hasRole('ROLE_ADMIN')";
	public static final String PRE_AUTHORIZE_VIEW = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
	public static final String PRE_AUTHORIZE_DELETE = "denyAll";

	public static final String PRE_AUTHORIZE_DELETE_BY_ID = "denyAll";
	public static final String PRE_AUTHORIZE_DELETE_ALL = "denyAll";
	public static final String PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
	public static final String PRE_AUTHORIZE_FIND_BY_IDS = "denyAll";
	public static final String PRE_AUTHORIZE_FIND_ALL = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
	public static final String PRE_AUTHORIZE_COUNT = "denyAll";

	// global roles
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_SITE_OPERATOR = "ROLE_SITE_OPERATOR";
	public static final String ROLE_USER = "ROLE_USER";


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
