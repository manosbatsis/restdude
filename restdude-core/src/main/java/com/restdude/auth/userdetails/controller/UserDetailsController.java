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
package com.restdude.auth.userdetails.controller;

import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.model.LoginSubmission;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "Auth", description = "Authentication operations")
@RequestMapping(value = {"/apiauth/userDetails", "/api/auth/userDetails"}, produces = {"application/json", "application/xml"})
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
    @ResponseBody
    public ICalipsoUserDetails create(@RequestBody LoginSubmission resource) {
        ICalipsoUserDetails userDetails = new UserDetails(resource);
        LOGGER.debug("create, LoginSubmission: {}", resource);
        userDetails = this.service.create(userDetails);
        if (userDetails != null && userDetails.getId() != null) {

            userDetails.setPassword(resource.getPassword());
            SecurityUtil.login(request, response, userDetails, userDetailsConfig, this.service);
        } else {

            LOGGER.info("login failed, logging out: " + userDetails);
            SecurityUtil.logout(request, response, userDetailsConfig);
            userDetails = new UserDetails();
        }
        return userDetails;
    }

    @ApiOperation(value = "Remember",
            notes = "Login remembered user")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ICalipsoUserDetails remember() {
        ICalipsoUserDetails userDetails = this.service.getPrincipal();
        if (userDetails == null) {
            userDetails = new UserDetails();
        }
        return userDetails;

    }

    @ApiOperation(value = "Logout",
            notes = "Logout and forget user")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ICalipsoUserDetails delete() {
        // logout
        SecurityUtil.logout(request, response, userDetailsConfig);
        return new UserDetails();
    }

    @RequestMapping(value = "verification", method = RequestMethod.POST)
    @ApiOperation(value = "Verify",
            notes = "Validation utility operation, used to verify the user based on current password.")
    @ResponseBody
    public ICalipsoUserDetails verify(@RequestBody LoginSubmission resource) {
        ICalipsoUserDetails userDetails = new UserDetails(resource);
        return this.service.create(userDetails);
    }


}
