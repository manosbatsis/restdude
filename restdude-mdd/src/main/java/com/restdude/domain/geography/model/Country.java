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
import com.restdude.domain.geography.controller.CountryController;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;

/**
 * Class to represent a country, including ISO 3166-1 alpha-2 code, name,
 * languages, capital and currency, native name, calling codes.
 */

@DiffIgnore
@Entity
@Table(name = "country")
@AttributeOverrides({
        @AttributeOverride(name = "pk", column = @Column(unique = true, nullable = false, length = 2)),
        @AttributeOverride(name = "name", column = @Column(unique = true, nullable = false, length = 50)),
})
@ModelResource(pathFragment = "countries", apiName = "Countries", apiDescription = "Operations about countries", controllerSuperClass = CountryController.class)
@ApiModel(value = "Country", description = "A model representing a country, meaning a region that is identified as a distinct entity in political geography.")
@Relation(value = "country", collectionRelation = "countries")
@PreAuthorizeFindPaginated(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindByIds(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindById(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindAll(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
public class Country extends AbstractFormalRegionModel<Continent, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

	@Getter @Setter
    @Column(name = "native_name", unique = true, nullable = true, length = 50)
    private String nativeName;

	@Getter @Setter
    @Column(name = "calling_code", unique = false, nullable = true, length = 15)
    private String callingCode;

	@Getter @Setter
	@Column(unique = false, nullable = true, length = 50)
	private String capital;

	@Getter @Setter
	@Column(unique = false, nullable = true, length = 30)
	private String currency;

	@Getter @Setter
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