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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import java.util.Date;

@ShallowReference
@Entity
@ApiModel(description = "UserCredentials")
@Table(name = "user_credentials")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserCredentials extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final long serialVersionUID = 1L;


    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "active")
    private Boolean active = false;

    @Column(name = "inactivation_reason")
    private String inactivationReason;

    @Column(name = "inactivation_date")
    private Date inactivationDate;

    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = SkipPropertySerializer.class)
    @Column(name = "user_password")
    private String password;

    @JsonIgnore
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @JsonIgnore
    @Column(name = "reset_password_token_date")
    private Date resetPasswordTokenCreated;

    @Column(name = "password_changed")
    private Date lastPassWordChangeDate;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "login_attempts")
    private Short loginAttempts = 0;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OneToOne(mappedBy = "credentials", fetch = FetchType.LAZY)
    private UserRegistrationCode registrationCode;

    public UserCredentials() {
    }

    public UserCredentials(String username, Boolean active, String inactivationReason, Date inactivationDate,
                           String password, String resetPasswordToken, Date resetPasswordTokenCreated,
                           Date lastPassWordChangeDate, Date lastLogin, Short loginAttempts, UserRegistrationCode registrationCode) {
        super();
        this.username = username;
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

    @PreUpdate
    @PrePersist
    public void onBeforeSave() {

        // fallback username
        if (!StringUtils.isNotBlank(this.getUsername())) {
            String username = this.getUser().getEmail();
            if (StringUtils.isNotBlank(username)) {
                username = username.replace("@", "_").replaceAll("\\.", "_");
            } else {
                username = this.getUser().getId();
            }
            this.setUsername(username);
        }

        // clear or set the token creation date  if needed
        if (this.getResetPasswordToken() == null) {
            this.setResetPasswordTokenCreated(null);
        } else if (this.getResetPasswordTokenCreated() == null) {
            this.setResetPasswordTokenCreated(new Date());
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("username", this.getUsername())
                .append("active", this.getActive())
                .append("inactivationReason", this.getInactivationReason())
                .append("inactivationDate", this.getInactivationDate())
                .append("resetPasswordTokenCreated", this.resetPasswordTokenCreated)
                .append("lastPassWordChangeDate", this.lastPassWordChangeDate)
                .append("lastLogin", this.lastLogin)
                .append("loginAttempts", this.loginAttempts)
                .toString();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

    public Date getInactivationDate() {
        return inactivationDate;
    }

    public void setInactivationDate(Date inactivationDate) {
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

    public Date getResetPasswordTokenCreated() {
        return resetPasswordTokenCreated;
    }

    public void setResetPasswordTokenCreated(Date resetPasswordTokenCreated) {
        this.resetPasswordTokenCreated = resetPasswordTokenCreated;
    }

    public Date getLastPassWordChangeDate() {
        return lastPassWordChangeDate;
    }

    public void setLastPassWordChangeDate(Date lastPassWordChangeDate) {
        this.lastPassWordChangeDate = lastPassWordChangeDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
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
        private String username;
        private Boolean active;
        private String inactivationReason;
        private Date inactivationDate;
        private String password;
        private String resetPasswordToken;
        private Date resetPasswordTokenCreated;
        private Date lastPassWordChangeDate;
        private Date lastLogin;
        private Short loginAttempts;
        private User user;
        private UserRegistrationCode registrationCode;

        public Builder username(String username) {
            this.username = username;
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

        public Builder inactivationDate(Date inactivationDate) {
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

        public Builder resetPasswordTokenCreated(Date resetPasswordTokenCreated) {
            this.resetPasswordTokenCreated = resetPasswordTokenCreated;
            return this;
        }

        public Builder lastPassWordChangeDate(Date lastPassWordChangeDate) {
            this.lastPassWordChangeDate = lastPassWordChangeDate;
            return this;
        }

        public Builder lastLogin(Date lastLogin) {
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
        this.username = builder.username;
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