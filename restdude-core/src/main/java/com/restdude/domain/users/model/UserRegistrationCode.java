/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;

@ShallowReference
@Entity
@ApiModel(description = "UserRegistrationCode")
@ModelResource(path = "userRegistrationCodes", controllerSuperClass = AbstractReadOnlyModelController.class,
        apiName = "UserRegistrationCode", apiDescription = "User registration codes (read-only)")
@Table(name = "registration_code")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserRegistrationCode extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final long serialVersionUID = 1L;

    public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('" + Role.ROLE_ADMIN + "', '" + Role.ROLE_SITE_OPERATOR + "')";
    public static final String PRE_AUTHORIZE_VIEW = PRE_AUTHORIZE_SEARCH;

    @Formula(" (credentials_id IS NULL) ")
    private Boolean available;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credentials_id", unique = true)
    private UserCredentials credentials;

    @ManyToOne
    @JoinColumn(name = "batch_id", referencedColumnName = "id", nullable = false, updatable = false)
    private UserRegistrationCodeBatch batch;

    public UserRegistrationCode() {
    }

    public UserRegistrationCode(String id) {
        this.setId(id);
    }

    public UserRegistrationCode(UserRegistrationCodeBatch batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
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