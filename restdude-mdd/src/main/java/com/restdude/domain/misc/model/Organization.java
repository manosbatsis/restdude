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
package com.restdude.domain.misc.model;

import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractPersistableModelController;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Organization entries")
@ModelResource(pathFragment = "organizations", controllerSuperClass = AbstractPersistableModelController.class, apiName = "Organization", apiDescription = "Operations about organization")
@Entity
@Table(name = "organization")
public class Organization extends AbstractSystemUuidPersistableModel {


    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter
    private String name;

    @NotNull
    @Column(name = "description", length = 500, nullable = false)
    @Getter @Setter
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner", referencedColumnName = "pk")
    @Getter @Setter
    private User owner;

    public Organization() {
        super();
    }

    public Organization(String name) {
        this();
        this.name = name;
    }

    public Organization(String name, String description, User owner) {
        this(name);
        this.description = description;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Organization)) {
            return false;
        }
        Organization that = (Organization) obj;
        return null == this.getPk() ? false : this.getPk().equals(that.getPk());
    }

}