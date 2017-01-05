/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
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
import org.hibernate.validator.constraints.Email;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ShallowReference
@Entity
@ApiModel(description = "CellphoneDetail")
@Table(name = "detail_contact_cellphone")
@Inheritance(strategy = InheritanceType.JOINED)
public class CellphoneDetail extends AbstractContactDetail {

    @Email
    @NotNull
    @Column(name = "number", unique = true, nullable = false, updatable = false)
    private String number;

    public CellphoneDetail() {
    }

    @Override
    public String getValue() {
        return number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
