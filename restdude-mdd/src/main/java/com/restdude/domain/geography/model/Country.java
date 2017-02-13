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
package com.restdude.domain.geography.model;

import com.restdude.auth.spel.annotations.PreAuthorizeFindAll;
import com.restdude.auth.spel.annotations.PreAuthorizeFindById;
import com.restdude.auth.spel.annotations.PreAuthorizeFindByIds;
import com.restdude.auth.spel.annotations.PreAuthorizeFindPaginated;
import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.mdd.annotation.model.ModelResource;
import com.yahoo.elide.annotation.Include;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;

/**
 * Class to represent a country, including ISO 3166-1 alpha-2 code, name,
 * languages, capital and currency, native name, calling codes.
 */
@Entity
@Table(name = "country")
@Include(type = "country", rootLevel = true)
@AttributeOverrides({
        @AttributeOverride(name = "pk", column = @Column(unique = true, nullable = false, length = 2)),
        @AttributeOverride(name = "name", column = @Column(unique = true, nullable = false, length = 50)),
})
@ModelResource(pathFragment = "countries", apiName = "Countries", apiDescription = "Operations about countries")
@ApiModel(value = "Country", description = "A model representing a country, meaning a region that is identified as a distinct entity in political geography.")
@Relation(value = "country", collectionRelation = "countries")
@PreAuthorizeFindPaginated(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindByIds(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindById(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindAll(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
public class Country extends AbstractFormalRegion<Continent, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

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

	public Country(String pk) {
		this.setPk(pk);
	}

	public Country(String pk, String name, String nativeName, String callingCode, Continent continent, String capital,
				   String currency, String languages) {
		super(pk, name, continent);
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

	@Override
	public boolean equals(final Object obj) {
        if (Country.class.isAssignableFrom(obj.getClass())) {
            final Country other = (Country) obj;
            return new EqualsBuilder()
                    .appendSuper(super.equals(other))
                    .append(this.getName(), other.getName())
                    .isEquals();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
                .append(this.getName())
                .toHashCode();
	}
}