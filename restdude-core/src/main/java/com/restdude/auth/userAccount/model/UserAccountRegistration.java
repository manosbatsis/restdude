/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.userAccount.model;

import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.model.UserRegistrationCode;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "UserAccountRegistration", description = "User registration")
public class UserAccountRegistration implements Serializable {

	private static final long serialVersionUID = 5206010308112791343L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountRegistration.class);

	private String registrationCode;
	private String username;
	private String password;
	private String passwordConfirmation;
    private String registrationEmail;
    private String firstName;
	private String lastName;
	private String telephone;
	private String cellphone;
	private String locale = "en";
	private Date birthDay;
	private String redirectUrl = null;

	/**
	 * Default constructor
	 */
	public UserAccountRegistration() {

	}

	public User asUser() {
		User newUser = new User.Builder()
				.credentials(new UserCredentials.Builder().username(this.username).password(this.password)
						.registrationCode(new UserRegistrationCode(this.registrationCode)).build())
                .email(this.registrationEmail).firstName(this.firstName).lastName(this.lastName).telephone(this.telephone)
                .cellphone(this.cellphone).locale(this.locale).birthDay(this.birthDay).build();
		return newUser;
	}

	@Override
	public String toString() {
        return new ToStringBuilder(this).append("username", username).append("registrationEmail", registrationEmail).toString();
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

    public String getRegistrationEmail() {
        return registrationEmail;
    }

    public void setRegistrationEmail(String registrationEmail) {
        this.registrationEmail = registrationEmail;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
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
        private String registrationEmail;
        private String firstName;
		private String lastName;
		private String telephone;
		private String cellphone;
		private String locale;
		private Date birthDay;
		private String redirectUrl;

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

        public Builder registrationEmail(String registrationEmail) {
            this.registrationEmail = registrationEmail;
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

		public Builder telephone(String telephone) {
			this.telephone = telephone;
			return this;
		}

		public Builder cellphone(String cellphone) {
			this.cellphone = cellphone;
			return this;
		}

		public Builder locale(String locale) {
			this.locale = locale;
			return this;
		}

		public Builder birthDay(Date birthDay) {
			this.birthDay = birthDay;
			return this;
		}

		public Builder redirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
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
        this.registrationEmail = builder.registrationEmail;
        this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.telephone = builder.telephone;
		this.cellphone = builder.cellphone;
		this.locale = builder.locale;
		this.birthDay = builder.birthDay;
		this.redirectUrl = builder.redirectUrl;
	}
}
