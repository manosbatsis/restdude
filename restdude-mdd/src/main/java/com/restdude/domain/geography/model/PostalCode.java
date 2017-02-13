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

import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class to represent  a postal code
 */
@Entity
@Table(name = "geo_pocode")
@ModelResource(pathFragment = "postalCodes", apiName = "PostalCodes", apiDescription = "Postal code operations")
@ApiModel(value = "PostalCode", description = "A model representing apostal code")
public class PostalCode extends AbstractFormalRegion<Locality, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostalCode.class);

    public PostalCode() {
        super();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PostalCode) {
            final PostalCode other = (PostalCode) obj;
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
                .append(PostalCode.class)
                .toHashCode();
    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getPk();
    }
}