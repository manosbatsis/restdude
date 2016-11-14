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
package com.restdude.domain.users.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.restdude.domain.base.controller.AbstractNoDeleteModelController;
import com.restdude.domain.base.controller.IFilesModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.fs.FilePersistenceService;
import com.restdude.domain.metadata.model.MetadatumDTO;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.resthub.web.exception.NotImplementedClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;


@Api(tags = "Users", description = "User management operations")
@RequestMapping(value = "/api/rest/users", produces = {"application/json", "application/xml"})
public class UserController extends AbstractNoDeleteModelController<User, String, UserService> implements IFilesModelController<User, String, UserService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    FilePersistenceService filePersistenceService;

    @Inject
    @Qualifier(FilePersistenceService.BEAN_ID)
    public void setFilePersistenceService(FilePersistenceService filePersistenceService) {
        this.filePersistenceService = filePersistenceService;
    }

    @Override
    public FilePersistenceService getFilePersistenceService() {
        return this.filePersistenceService;
    }

    @RequestMapping(value = "byUserNameOrEmail/{userNameOrEmail}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get one by username or email", notes = "Get the single user with the given username or email.")
    public User getByUserNameOrEmail(@PathVariable String userNameOrEmail) {
        return this.service.findOneByUserNameOrEmail(userNameOrEmail);
    }

    @RequestMapping(value = "{subjectId}/metadata", method = RequestMethod.PUT)
    @ResponseBody
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
    @ResponseBody
    @ApiOperation(value = "Update a resource", hidden = true)
    @JsonView(AbstractSystemUuidPersistable.ItemView.class)
    public User update(@ApiParam(name = "id", required = true, value = "string") @PathVariable String id, @RequestBody User resource) {
        throw new NotImplementedClientException("PUT is not supported; use PATCH");
    }

}
