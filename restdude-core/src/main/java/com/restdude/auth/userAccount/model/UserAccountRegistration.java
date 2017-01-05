/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
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
package com.restdude.auth.userAccount.model;

import com.restdude.auth.userAccount.validation.RegistrationCodeConstraint;
import com.restdude.domain.details.contact.model.ContactDetails;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.model.UserRegistrationCode;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "UserAccountRegistration", description = "User registration")
public class UserAccountRegistration implements Serializable {

	private static final long serialVersionUID = 5206010308112791343L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountRegistration.class);

	@RegistrationCodeConstraint
	private String registrationCode;
	private String username;
	private String password;
	private String passwordConfirmation;
    @Email
    @NotNull
    private String email;
    private String firstName;
	private String lastName;
	private String locale = "en";
    private String redirectUrl = null;

	/**
	 * Default constructor
	 */
	public UserAccountRegistration() {

	}

	public User asUser() {
        EmailDetail email = null;
        if (StringUtils.isNotBlank(this.getEmail())) {
            email = new EmailDetail();
            email.setEmail(this.getEmail());
        }
        User newUser = new User.Builder()
                .contactDetails(new ContactDetails.Builder().primaryEmail(email).build())
                .credentials(
                        new UserCredentials.Builder().password(this.password)
                                .registrationCode(new UserRegistrationCode(this.registrationCode)).build())
                .username(this.username).firstName(this.firstName).lastName(this.lastName)
                .locale(this.locale).build();
        return newUser;
	}

	@Override
	public String toString() {
        return new ToStringBuilder(this).append("username", username).append("email", email).toString();
    }

	public String getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public static class Builder {
		private String registrationCode;
		private String username;
		private String password;
		private String passwordConfirmation;
        private String email;
        private String firstName;
		private String lastName;
		private String locale;

		public Builder registrationCode(String registrationCode) {
			this.registrationCode = registrationCode;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder passwordConfirmation(String passwordConfirmation) {
			this.passwordConfirmation = passwordConfirmation;
			return this;
		}

        public Builder email(String registrationEmail) {
            this.email = registrationEmail;
            return this;
		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder locale(String locale) {
			this.locale = locale;
			return this;
		}


		public UserAccountRegistration build() {
			return new UserAccountRegistration(this);
		}
	}

	private UserAccountRegistration(Builder builder) {
		this.registrationCode = builder.registrationCode;
		this.username = builder.username;
		this.password = builder.password;
		this.passwordConfirmation = builder.passwordConfirmation;
        this.email = builder.email;
        this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.locale = builder.locale;
	}
}
