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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.annotation.model.ModelResource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "continent")
@AttributeOverrides({
        @AttributeOverride(name = "pk", column = @Column(unique = true, nullable = false, length = 2)),
})
@ModelResource(pathFragment = "continents", apiName = "Continents", apiDescription = "Operations about continent")
public class Continent extends AbstractFormalRegionModel<Continent, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Continent.class);

	@JsonIgnore @Getter @Setter
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Country> countries;

	public Continent() {
		super();
	}

    public Continent(String pk, String name) {
        super(pk, name, null);
    }

	@Override
	public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Continent) {
			final Continent other = (Continent) obj;
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
				.append(Continent.class)
				.toHashCode();
	}

}