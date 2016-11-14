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
package com.restdude.domain.misc.model;

import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.geography.model.Country;
import com.restdude.mdd.annotation.ModelResource;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 */
@ShallowReference
@ModelResource(path = "hosts", controllerSuperClass = AbstractModelController.class, apiName = "Hosts", apiDescription = "Operations about hosts")
@Entity
@Table(name = "host")
public class Host extends AbstractSystemUuidPersistable  {

	private static final long serialVersionUID = -7942906897981646998L;

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

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@NotNull
	@Column(name = "description", length = 500, nullable = false)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id", referencedColumnName = "id", nullable = true)
	private Country country;

	@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "host_aliases", joinColumns = @JoinColumn(name = "host_id"))
    //, uniqueConstraints = {	@UniqueConstraint(columnNames = { "host_alias" }) }
            Set<String> aliases = new HashSet<String>();

	public Host() {
		super();
	}

	public Host(String name) {
		this();
		this.name = name;
	}
	
	public Host(String name, String description, Country country) {
		this(name);
		this.description = description;
		this.country = country;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Host)) {
			return false;
		}
		Host that = (Host) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	public Set<String> getAliases() {
		return aliases;
	}

	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}

	public void addAlias(String alias) {
		if (this.aliases == null) {
			this.aliases = new HashSet<String>();
		}
		this.aliases.add(alias);
	}

	public static class Builder {
		private String name;
		private Country country;
		private Set<String> aliases = new HashSet<String>();

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder country(Country country) {
			this.country = country;
			return this;
		}

		public Builder aliases(Set<String> aliases) {
			this.aliases.addAll(aliases);
			return this;
		}
		
		public Builder aliase(String alias) {
			this.aliases.add(alias);
			return this;
		}

		public Host build() {
			return new Host(this);
		}
	}

	private Host(Builder builder) {
		this.name = builder.name;
		this.country = builder.country;
		this.aliases = builder.aliases;
	}
}