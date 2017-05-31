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
package com.restdude.auth.userAccount.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.PersistableModel;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import java.io.Serializable;

@ApiModel(value = "EmailConfirmationOrPasswordResetRequest", description = "Submitting a password reset request triggers a reset token and email if resetPasswordToken is null, updates the password if resetPasswordToken is not null and valid, ")
public class EmailConfirmationOrPasswordResetRequest implements PersistableModel<String> {

    private static final long serialVersionUID = 5206010308112791343L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConfirmationOrPasswordResetRequest.class);

    private String username;
    private String email;
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String currentPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String resetPasswordToken;

    /**
     * Default constructor
     */
    public EmailConfirmationOrPasswordResetRequest() {

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("username", username).append("email", email).toString();
    }

    public String getEmailOrUsername() {
        return email != null ? email : this.username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    /**
     * Equivalent of a method annotated with @{@link PrePersist} and/or
     *
     * @{@link javax.persistence.PreUpdate}, only applied before validation
     */
    @Override
    public void preSave() {

    }

    /**
     * Equivalent of {@link }org.springframework.data.domain.Persistable#isNew()}
     */
    @Override
    public boolean isNew() {
        return false;
    }

    /**
     * Get the entity's primary key. Functionally equivalent to {@linke org.springframework.data.domain.Persistable#getId()}
     * only without conflict with {@link ResourceSupport#getId()}
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * Set the entity's primary key. Functionally equivalent to {@linke org.springframework.data.domain.Persistable#setId()}
     * only without conflict with the getter {@link ResourceSupport#getId()}
     *
     * @param id the id to set
     */
    @Override
    public void setId(String id) {

    }
}
