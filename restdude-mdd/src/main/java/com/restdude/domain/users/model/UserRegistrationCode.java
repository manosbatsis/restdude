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
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.controller.AbstractReadOnlyPersistableModelController;
import com.restdude.mdd.model.AbstractSystemUuidPersistableResource;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;

@ShallowReference
@Entity
@ApiModel(description = "UserRegistrationCode")
@ModelResource(pathFragment = "userRegistrationCodes", controllerSuperClass = AbstractReadOnlyPersistableModelController.class,
        apiName = "UserRegistrationCode", apiDescription = "User registration codes (read-only)")
@Table(name = "registration_code")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserRegistrationCode extends AbstractSystemUuidPersistableResource {

    private static final long serialVersionUID = 1L;

    @Formula(" (credentials_id IS NULL) ")
    private Boolean available;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credentials_id", unique = true)
    private UserCredentials credentials;

    @ManyToOne
    @JoinColumn(name = "batch_id", referencedColumnName = "pk", nullable = false, updatable = false)
    private UserRegistrationCodeBatch batch;

    public UserRegistrationCode() {
    }

    public UserRegistrationCode(String id) {
        this.setPk(id);
    }

    public UserRegistrationCode(UserRegistrationCodeBatch batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pk", this.getPk())
                .append("batch", this.getBatch())
                .toString();
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

    public UserRegistrationCodeBatch getBatch() {
        return batch;
    }

    public void setBatch(UserRegistrationCodeBatch batch) {
        this.batch = batch;
    }
}