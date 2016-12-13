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

import com.restdude.auth.spel.annotations.PreAuthorizeFindAll;
import com.restdude.auth.spel.annotations.PreAuthorizeFindById;
import com.restdude.auth.spel.annotations.PreAuthorizeFindByIds;
import com.restdude.auth.spel.annotations.PreAuthorizeFindPaginated;
import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Class to represent a country, including ISO 3166-1 alpha-2 code, name,
 * languages, capital and currency, native name, calling codes.
 */
@Entity
@Table(name = "country")
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(unique = true, nullable = false, length = 50)),
})
@ModelResource(path = "countries", apiName = "Countries", apiDescription = "Operations about countries")
@ApiModel(value = "Country", description = "A model representing a country, meaning a region that is identified as a distinct entity in political geography.")

@PreAuthorizeFindPaginated(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindByIds(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindById(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindAll(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
public class Country extends AbstractFormalRegion<Continent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    @Id
    @Column(unique = true, nullable = false, length = 2)
    private String id;

    @Formula(" (id) ")
    private String savedId;

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

    /**
     * Get the entity's primary key
     *
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the entity's primary key
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    private String getSavedId() {
        return savedId;
    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getSavedId();
    }

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Country) {
			final Country other = (Country) obj;
			return new EqualsBuilder().appendSuper(super.equals(other))
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(Country.class)
				.toHashCode();
	}
}