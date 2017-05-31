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
package com.restdude.auth.userdetails.controller;

import com.restdude.auth.model.BasicUserDetailsImpl;
import com.restdude.auth.model.LoginRequest;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.domain.UserDetails;
import com.restdude.util.exception.http.InvalidCredentialsException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "Auth", description = "Authentication operations")
@RestController
@RequestMapping(value = {"/apiauth/userDetails", "/api/auth/userDetails"}, produces = {"application/json"})
public class UserDetailsController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UserDetailsController.class);

    private UserDetailsService service;

    protected UserDetailsConfig userDetailsConfig = new SimpleUserDetailsConfig();

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired(required = false)
    public void setUserDetailsConfig(UserDetailsConfig userDetailsConfig) {
        this.userDetailsConfig = userDetailsConfig;
    }

    @Inject
    @Qualifier("userDetailsService")
    public void setService(UserDetailsService service) {
        this.service = service;
    }

    //
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Login",
            notes = "Login using a JSON object with email/password properties.")
    public UserDetails create(@RequestBody LoginRequest resource) {
        LOGGER.debug("#create, LoginRequest: {}", resource);
        UserDetails userDetails = new BasicUserDetailsImpl(resource);
        LOGGER.debug("#create, userDetails: {}", userDetails);
        userDetails = this.service.create(userDetails);
        if (userDetails != null && userDetails.getId() != null) {

            userDetails.setPassword(resource.getPassword());
            SecurityUtil.login(request, response, userDetails, userDetailsConfig, this.service);
        } else {

            LOGGER.debug("#create, login failed, logging out");
            SecurityUtil.logout(request, response, userDetailsConfig);
        }
        return userDetails;
    }

    @ApiOperation(value = "Remember",
            notes = "Login user if remembered")
    @RequestMapping(method = RequestMethod.GET)
    public UserDetails remember() {
        UserDetails userDetails = this.service.getPrincipal();
        LOGGER.debug("#remember userDetails: {}", userDetails);
        if (userDetails == null) {
            LOGGER.debug("#remember failed, logging out");
            SecurityUtil.logout(request, response, userDetailsConfig);
            throw new InvalidCredentialsException("Not authenticated");
        } else {
            return userDetails;
        }

    }

    @ApiOperation(value = "Logout",
            notes = "Logout and forget user")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        // logout
        SecurityUtil.logout(request, response, userDetailsConfig);
    }

    @RequestMapping(value = "verification", method = RequestMethod.POST)
    @ApiOperation(value = "Verify",
            notes = "Validation utility operation, used to verify the user based on current password.")
    public UserDetails verify(@RequestBody LoginRequest resource) {
        UserDetails userDetails = new BasicUserDetailsImpl(resource);
        return this.service.create(userDetails);
    }


}
