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
package com.restdude.domain.users.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.domain.base.annotation.controller.ModelController;
import com.restdude.mdd.controller.AbstractNoDeleteModelController;
import com.restdude.mdd.model.AbstractSystemUuidPersistableResource;
import com.restdude.domain.metadata.model.MetadatumDTO;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.util.exception.http.NotImplementedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;

@ModelController
@ExposesResourceFor(User.class)
@Api(tags = "Users", description = "User management operations")
public class UserController extends AbstractNoDeleteModelController<User, String, UserService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "byUserNameOrEmail/{userNameOrEmail}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one by username or email", notes = "Get the single user with the given username or email.")
    public User getByUserNameOrEmail(@PathVariable String userNameOrEmail) {
        return this.service.findOneByUserNameOrEmail(userNameOrEmail);
    }

    @RequestMapping(value = "{subjectId}/metadata", method = RequestMethod.PUT)
    @ApiOperation(value = "Add metadatum", notes = "Add or update a resource metadatum")
    public void addMetadatum(@PathVariable String subjectId,
                             @RequestBody MetadatumDTO dto) {
        service.addMetadatum(subjectId, dto);
    }

    /*
     * Disallow complete PUT as clients keep updating properties to null etc.
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a resource", hidden = true)
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    public User update(@ApiParam(name = "pk", required = true, value = "string") @PathVariable String id, @RequestBody User resource) {
        throw new NotImplementedException("PUT is not supported; use PATCH");
    }

    @ApiOperation(value = "Update files",
            notes = "The files are saved using the parameter names of the multipart files contained in this request. "
                    + "These are the field names of the form (like with normal parameters), not the original file names.")
    @RequestMapping(value = "{id}/files",
            method = {RequestMethod.POST},
            headers = ("content-type=multipart/*"),
            produces = {"application/json", "application/xml"})
    public User updateFiles(@PathVariable String id,
                            MultipartHttpServletRequest request, HttpServletResponse response) {
        return this.service.updateFiles(id, request, response);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    public User patch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable String id, @RequestBody User resource) {
        LOGGER.debug("patch, resource: {}", resource);
        ICalipsoUserDetails principal = this.service.getPrincipal();
        LOGGER.debug("patch, principal: {}", principal);
        if (!principal.isAdmin() && !principal.isSiteAdmin()) {
            resource.setRoles(null);
        }
        resource.setCredentials(null);
        resource.setContactDetails(null);
        resource.setUsername(null);
        User user = super.patch(id, resource);
        LOGGER.debug("patch, user: {}", user);
        LOGGER.debug("patch, user: {}", user.getClass().getCanonicalName());
        return user;
    }

    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    public void deleteFiles(String id, String... filenames) {
        this.service.deleteFiles(id, filenames);
    }

}
