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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.restdude.domain.base.binding.SkipPropertySerializer;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ShallowReference
@Entity
@ApiModel(description = "UserCredentials")
@Table(name = "user_credentials")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserCredentials extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @Column(name = "inactivation_reason")
    private String inactivationReason;

    @Column(name = "inactivation_date")
    private LocalDateTime inactivationDate;

    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = SkipPropertySerializer.class)
    @Column(name = "user_password")
    private String password;

    @JsonIgnore
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @JsonIgnore
    @Column(name = "reset_password_token_date")
    private LocalDateTime resetPasswordTokenCreated;

    @Column(name = "password_changed")
    private LocalDateTime lastPassWordChangeDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "login_attempts")
    private Short loginAttempts = 0;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OneToOne(mappedBy = "credentials", fetch = FetchType.LAZY)
    private UserRegistrationCode registrationCode;

    public UserCredentials() {
    }

    public UserCredentials(String email, Boolean active, String inactivationReason, LocalDateTime inactivationDate,
                           String password, String resetPasswordToken, LocalDateTime resetPasswordTokenCreated,
                           LocalDateTime lastPassWordChangeDate, LocalDateTime lastLogin, Short loginAttempts, UserRegistrationCode registrationCode) {
        super();
        this.email = email;
        this.active = active;
        this.inactivationReason = inactivationReason;
        this.inactivationDate = inactivationDate;
        this.password = password;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenCreated = resetPasswordTokenCreated;
        this.lastPassWordChangeDate = lastPassWordChangeDate;
        this.lastLogin = lastLogin;
        this.loginAttempts = loginAttempts;
        this.registrationCode = registrationCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preSave() {

        // clear or set the token creation date  if needed
        if (this.getResetPasswordToken() == null) {
            this.setResetPasswordTokenCreated(null);
        } else if (this.getResetPasswordTokenCreated() == null) {
            this.setResetPasswordTokenCreated(LocalDateTime.now());
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("email", this.getEmail())
                .append("active", this.getActive())
                .append("inactivationReason", this.getInactivationReason())
                .append("inactivationDate", this.getInactivationDate())
                .append("resetPasswordTokenCreated", this.resetPasswordTokenCreated)
                .append("lastPassWordChangeDate", this.lastPassWordChangeDate)
                .append("lastLogin", this.lastLogin)
                .append("loginAttempts", this.loginAttempts)
                .toString();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public String getInactivationReason() {
        return inactivationReason;
    }

    public void setInactivationReason(String inactivationReason) {
        this.inactivationReason = inactivationReason;
    }

    public LocalDateTime getInactivationDate() {
        return inactivationDate;
    }

    public void setInactivationDate(LocalDateTime inactivationDate) {
        this.inactivationDate = inactivationDate;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getResetPasswordTokenCreated() {
        return resetPasswordTokenCreated;
    }

    public void setResetPasswordTokenCreated(LocalDateTime resetPasswordTokenCreated) {
        this.resetPasswordTokenCreated = resetPasswordTokenCreated;
    }

    public LocalDateTime getLastPassWordChangeDate() {
        return lastPassWordChangeDate;
    }

    public void setLastPassWordChangeDate(LocalDateTime lastPassWordChangeDate) {
        this.lastPassWordChangeDate = lastPassWordChangeDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Short getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Short loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    public UserRegistrationCode getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(UserRegistrationCode registrationCode) {
        this.registrationCode = registrationCode;
    }

    public static class Builder {
        private String email;
        private Boolean active;
        private String inactivationReason;
        private LocalDateTime inactivationDate;
        private String password;
        private String resetPasswordToken;
        private LocalDateTime resetPasswordTokenCreated;
        private LocalDateTime lastPassWordChangeDate;
        private LocalDateTime lastLogin;
        private Short loginAttempts;
        private User user;
        private UserRegistrationCode registrationCode;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Builder inactivationReason(String inactivationReason) {
            this.inactivationReason = inactivationReason;
            return this;
        }

        public Builder inactivationDate(LocalDateTime inactivationDate) {
            this.inactivationDate = inactivationDate;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder resetPasswordToken(String resetPasswordToken) {
            this.resetPasswordToken = resetPasswordToken;
            return this;
        }

        public Builder resetPasswordTokenCreated(LocalDateTime resetPasswordTokenCreated) {
            this.resetPasswordTokenCreated = resetPasswordTokenCreated;
            return this;
        }

        public Builder lastPassWordChangeDate(LocalDateTime lastPassWordChangeDate) {
            this.lastPassWordChangeDate = lastPassWordChangeDate;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public Builder loginAttempts(Short loginAttempts) {
            this.loginAttempts = loginAttempts;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder registrationCode(UserRegistrationCode registrationCode) {
            this.registrationCode = registrationCode;
            return this;
        }

        public UserCredentials build() {
            return new UserCredentials(this);
        }
    }

    private UserCredentials(Builder builder) {
        this.email = builder.email;
        this.active = builder.active;
        this.inactivationReason = builder.inactivationReason;
        this.inactivationDate = builder.inactivationDate;
        this.password = builder.password;
        this.resetPasswordToken = builder.resetPasswordToken;
        this.resetPasswordTokenCreated = builder.resetPasswordTokenCreated;
        this.lastPassWordChangeDate = builder.lastPassWordChangeDate;
        this.lastLogin = builder.lastLogin;
        this.loginAttempts = builder.loginAttempts;
        this.user = builder.user;
        this.registrationCode = builder.registrationCode;
    }
}