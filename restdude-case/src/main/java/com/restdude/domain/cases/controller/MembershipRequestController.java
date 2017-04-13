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
package com.restdude.domain.cases.controller;

import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.dto.SpaceContextInvitations;
import com.restdude.domain.cases.model.dto.SpaceContextInvitationsResult;
import com.restdude.domain.cases.service.MembershipRequestService;
import com.restdude.mdd.controller.AbstractPersistableModelController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "BusinessContext Membership Requests", description = "Requests or invitations to join a BusinessContext")
@RequestMapping(value = "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT)
public class MembershipRequestController extends AbstractPersistableModelController<MembershipRequest, String, MembershipRequestService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembershipRequestController.class);
	
	@RequestMapping(value = "cc", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Create multiple invitations to a BusinessContext", notes = "Only the owner of the BusinessContext in context is allowed this peration.")
	public SpaceContextInvitationsResult create(@RequestBody SpaceContextInvitations resource) {
		return this.service.create(resource);
	}
    
}
