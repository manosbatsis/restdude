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
package com.restdude.domain.geography.model;

import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

/**
 * Class to represent a country, including ISO 3166-1 alpha-2 code, name,
 * languages, capital and currency, native name, calling codes.
 */
@Entity
@Table(name = "country")
@AttributeOverrides({ 
	@AttributeOverride(name = "id", column = @Column(unique = true, nullable = false, length = 2)),
	@AttributeOverride(name = "name", column = @Column(unique = true, nullable = false, length = 50)), 
})
@ModelResource(path = "countries", apiName = "Countries", apiDescription = "Operations about countries")
@ApiModel(value = "Country", description = "A model representing a country, meaning a region that is identified as a distinct entity in political geography.")
public class Country extends AbstractFormalRegion<Continent> {

	private static final long serialVersionUID = 3723330183409907891L;


	public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('ROLE_USER')";
	public static final String PRE_AUTHORIZE_CREATE = "hasRole('ROLE_ADMIN')";
	public static final String PRE_AUTHORIZE_UPDATE = "hasRole('ROLE_ADMIN')";
	public static final String PRE_AUTHORIZE_PATCH = "hasRole('ROLE_ADMIN')";
	public static final String PRE_AUTHORIZE_VIEW = "hasAnyRole('ROLE_USER')";
	public static final String PRE_AUTHORIZE_DELETE = "denyAll";

	public static final String PRE_AUTHORIZE_DELETE_BY_ID = "denyAll";
	public static final String PRE_AUTHORIZE_DELETE_ALL = "denyAll";
	public static final String PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
	public static final String PRE_AUTHORIZE_FIND_BY_IDS = "denyAll";
	public static final String PRE_AUTHORIZE_FIND_ALL = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
	public static final String PRE_AUTHORIZE_COUNT = "denyAll";

    @Column(name = "native_name", unique = true, nullable = true, length = 50)
    private String nativeName;

    @Column(name = "calling_code", unique = false, nullable = true, length = 15)
    private String callingCode;

	@Column(unique = false, nullable = true, length = 50)
	private String capital;

	@Column(unique = false, nullable = true, length = 30)
	private String currency;

	@Column(unique = false, nullable = true, length = 30)
	private String languages;

	public Country() {
		super();
	}

	public Country(String id) {
		this.setId(id);
	}

	public Country(String id, String name, String nativeName, String callingCode, Continent continent, String capital,
			String currency, String languages) {
		super(id, name, continent);
		this.nativeName = nativeName;
		this.callingCode = callingCode;
		this.capital = capital;
		this.currency = currency;
		this.languages = languages;
	}

	public String getNativeName() {
		return nativeName;
	}

	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

}