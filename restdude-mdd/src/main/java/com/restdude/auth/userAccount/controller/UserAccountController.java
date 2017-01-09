/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
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
package com.restdude.auth.userAccount.controller;

import com.restdude.auth.userAccount.model.EmailConfirmationOrPasswordResetRequest;
import com.restdude.auth.userAccount.model.UserAccountRegistration;
import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.exception.http.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "AuthAccount", description = "User account operations")
@RequestMapping(value = {"/api/auth/account", "/api/auth/accounts"}, produces = {"application/json", "application/xml"})
public class UserAccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountController.class);

	private UserService service;

    private UserDetailsService userDetailsService;
    protected UserDetailsConfig userDetailsConfig = new SimpleUserDetailsConfig();

	@Inject
	@Qualifier("userService")
	public void setService(UserService service) {
		this.service = service;
	}

    @Inject
    @Qualifier("userDetailsService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired(required = false)
    public void setUserDetailsConfig(UserDetailsConfig userDetailsConfig) {
        this.userDetailsConfig = userDetailsConfig;
    }

	@RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register new account", notes = "Register a new user")
    public User create(@RequestBody UserAccountRegistration resource) {
        LOGGER.debug("create, resource: {}", resource);

		// get config
		Configuration config = ConfigurationFactory.getConfiguration();
		boolean forceCodes = config.getBoolean(ConfigurationFactory.FORCE_CODES, false);

		// require email
        if (StringUtils.isBlank(resource.getEmail())) {
            throw new BadRequestException("Email is required");
		}
		// force registration codes?
		if (forceCodes && StringUtils.isBlank(resource.getRegistrationCode())) {
			throw new BadRequestException("Registration code is required");
		}
		// passwords match?
		if (StringUtils.isNotBlank(resource.getPassword())
				&& StringUtils.isNotBlank(resource.getPasswordConfirmation())
				&& !resource.getPassword().equals(resource.getPasswordConfirmation())) {
			throw new BadRequestException("Password and password confirmation do not match");
		}

		// create user
        User u = this.service.create(resource.asUser());
        LOGGER.debug("created, user: {}", u);
        return u;

	}

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "Confirm registration email and/or update account password", notes = "Confirm registration email and/or update, reset, or request to reset an account password. The operation handles three cases. 1) When logged-in, provide " +
            "currentPassword, password and passwordConfirmation to immediately change password. 2) when anonymous, provide resetPasswordToken, password and passwordConfirmation to immediately" +
            "change password. 3) when anonymous, provide email or username to have a password reset token and link sent to your inbox.")
    public ICalipsoUserDetails update(@RequestBody EmailConfirmationOrPasswordResetRequest resource, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("update, resource: {}", resource);

        ICalipsoUserDetails userDetails = this.userDetailsService.resetPassword(resource);

		// (re)login if appropriate
		if (userDetails == null) {
			userDetails = new UserDetails();
		} else if (userDetails.getId() != null) {
			//userDetails = this.userDetailsService.create(userDetails);
			//userDetails.setPassword(resource.getPassword());
			LOGGER.debug("update, loggin-in userDetails: {}", userDetails);
			SecurityUtil.login(request, response, userDetails, userDetailsConfig, this.userDetailsService);
		}
		return userDetails;
    }


	@RequestMapping(value = "username", method = RequestMethod.PUT)
	@ApiOperation(value = "Update username", notes = "Updates the username of the curent user and updates the auth token cookie.")
	public ICalipsoUserDetails updateUsername(@RequestBody UsernameChangeRequest resource, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("updateUsername, resource: {}", resource);

		ICalipsoUserDetails userDetails = this.userDetailsService.updateUsername(resource);
		if (userDetails.getUsername().equals(resource.getUsername())) {
			LOGGER.debug("updateUsername updated, updating login userDetails: {}, pw: {}", userDetails, userDetails.getPassword());
			SecurityUtil.login(request, response, userDetails, userDetailsConfig, this.userDetailsService);
		} else {
			LOGGER.warn("updateUsername not updated, userDetails: {}", userDetails);
		}
		return userDetails;

	}


}
