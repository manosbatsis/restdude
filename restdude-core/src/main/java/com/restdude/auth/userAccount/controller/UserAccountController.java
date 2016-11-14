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
package com.restdude.auth.userAccount.controller;

import com.restdude.auth.userAccount.model.PasswordResetRequest;
import com.restdude.auth.userAccount.model.UserAccountRegistration;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
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
    @ApiOperation(value = "Register new account", notes = "Register a new user")
    @ResponseBody
	public User create(@RequestBody UserAccountRegistration resource) {
		LOGGER.debug("create, resource: {}", resource);
		// get config
		Configuration config = ConfigurationFactory.getConfiguration();
		boolean forceCodes = config.getBoolean(ConfigurationFactory.FORCE_CODES, false);

		// require email
		if (StringUtils.isBlank(resource.getRegistrationEmail())) {
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
		User u = resource.asUser();
		LOGGER.debug("create, user: {}", u);
		return this.service.create(u);

	}

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "Update account password", notes = "Update, reset, or request to reset an account password. The operation handles three cases. 1) When logged-in, provide " +
            "currentPassword, password and passwordConfirmation to immediately change password. 2) when anonymous, provide resetPasswordToken, password and passwordConfirmation to immediately" +
            "change password. 3) when anonymous, provide email or username to have a password reset token and link sent to your inbox.")
    @ResponseBody
    public ICalipsoUserDetails update(@RequestBody PasswordResetRequest resource, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("update, resource: {}", resource);

        ICalipsoUserDetails userDetails = this.userDetailsService.resetPassword(resource);

		// (re)login if appropriate
		if (userDetails == null) {
			userDetails = new UserDetails();
		} else if (userDetails.getId() != null) {
			userDetails.setPassword(resource.getPassword());
			//userDetails = this.userDetailsService.create(userDetails);
			//userDetails.setPassword(resource.getPassword());
			LOGGER.debug("update, loggin-in userDetails: {}", userDetails);
			SecurityUtil.login(request, response, userDetails, userDetailsConfig, this.userDetailsService);
		}
		return userDetails;

    }



}
