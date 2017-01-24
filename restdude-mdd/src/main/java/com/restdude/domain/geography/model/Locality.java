/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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

import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class to represent  an incorporated city or town political entity
 */
@Entity
@Table(name = "geo_locality")
@ModelResource(path = "localities", apiName = "Localities", apiDescription = "Locality operations")
@ApiModel(value = "Locality", description = "A model representing an incorporated city or town political entity")
public class Locality extends AbstractFormalRegion<Country> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Locality.class);


    public static String SERVICE_PRE_AUTHORIZE_SEARCH = "hasRole('ROLE_USER')";
    public static String SERVICE_PRE_AUTHORIZE_CREATE = "hasRole('ROLE_ADMIN')";
    public static String SERVICE_PRE_AUTHORIZE_UPDATE = "hasRole('ROLE_ADMIN')";
    public static String SERVICE_PRE_AUTHORIZE_PATCH = "hasRole('ROLE_ADMIN')";
    public static String SERVICE_PRE_AUTHORIZE_VIEW = "hasAnyRole('ROLE_USER')";
    public static String SERVICE_PRE_AUTHORIZE_DELETE = "denyAll";

    public static String SERVICE_PRE_AUTHORIZE_DELETE_BY_ID = "denyAll";
    public static String SERVICE_PRE_AUTHORIZE_DELETE_ALL = "denyAll";
    public static String SERVICE_PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
    public static String SERVICE_PRE_AUTHORIZE_FIND_BY_IDS = "denyAll";
    public static String SERVICE_PRE_AUTHORIZE_FIND_ALL = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_COUNT = "denyAll";

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    public Locality() {
        super();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Locality) {
            final Locality other = (Locality) obj;
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
                .append(Locality.class)
                .toHashCode();
    }


    /**
     * Get the entity's primary key
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public String getPk() {
        return id;
    }

    /**
     * Set the entity's primary key
     * @param id the pk to set
     */
    public void setPk(String id) {
        this.id = id;
    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getPk();
    }
}