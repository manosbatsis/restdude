/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.details.contact.model;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ShallowReference
@Entity
@ApiModel(description = "EmailDetail")
@Table(name = "detail_contact_email")
@Inheritance(strategy = InheritanceType.JOINED)
public class EmailDetail extends AbstractContactDetail {

    @NotNull
    @Email
    @Column(name = "email", unique = true, nullable = false, updatable = false)
    private String email;

    public EmailDetail() {
        super();
    }

    public EmailDetail(String email) {
        this();
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        if (!EmailDetail.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        EmailDetail other = (EmailDetail) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(obj));
        builder.append(StringUtils.lowerCase(getEmail()), StringUtils.lowerCase(other.getEmail()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 13).appendSuper(super.hashCode()).append(StringUtils.lowerCase(getEmail())).toHashCode();
    }

    @Override
    public void preSave() {
        if (StringUtils.isNoneEmpty(this.getEmail())) {
            this.setEmail(this.getEmail().toLowerCase());
        }
    }

    @Override
    public String getValue() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
