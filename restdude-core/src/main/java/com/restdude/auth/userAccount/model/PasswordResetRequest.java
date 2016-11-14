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
package com.restdude.auth.userAccount.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.restdude.domain.base.binding.SkipPropertySerializer;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@ApiModel(value = "PasswordResetRequest", description = "Submitting a password reset request triggers a reset token and email if resetPasswordToken is null, updates the password if resetPasswordToken is not null and valid, ")
public class PasswordResetRequest implements Serializable {

    private static final long serialVersionUID = 5206010308112791343L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetRequest.class);

    private String username;
    private String email;
    private String password;

    @JsonSerialize(using = SkipPropertySerializer.class)
    private String passwordConfirmation;

    @JsonSerialize(using = SkipPropertySerializer.class)
    private String currentPassword;

    @JsonSerialize(using = SkipPropertySerializer.class)
    private String resetPasswordToken;

    /**
     * Default constructor
     */
    public PasswordResetRequest() {

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
}
