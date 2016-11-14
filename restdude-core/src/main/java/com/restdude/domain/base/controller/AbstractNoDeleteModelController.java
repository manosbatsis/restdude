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
package com.restdude.domain.base.controller;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.service.ModelService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.resthub.web.exception.NotImplementedClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Base class for model controllers that must not support HTTP DELETE
 * operations.
 */
public abstract class AbstractNoDeleteModelController<T extends CalipsoPersistable<ID>, ID extends Serializable, S extends ModelService<T, ID>>
		extends AbstractModelController<T, ID, S> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNoDeleteModelController.class);

	@Override
	@ApiOperation(hidden = true, value = "Delete a resource (unsupported)")
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id) {
		throw new NotImplementedClientException("Method is unsupported.");
	}

	@Override
	@RequestMapping(method = RequestMethod.DELETE)
	@ApiOperation(hidden = true, value = "Delete all resources (unsupported)")
	public void delete() {
		throw new NotImplementedClientException("Method is unsupported.");
	}

	@ApiOperation(hidden = true, value = "Delete an uploaded file")
    @RequestMapping(value = "{subjectId}/uploads/{propertyName}/{id}", method = RequestMethod.DELETE)
    public @ResponseBody List deleteById(@PathVariable String subjectId, @PathVariable String propertyName, @PathVariable String id) {
		throw new NotImplementedClientException("Method is unsupported.");
	}

	@RequestMapping(value = "{subjectId}/metadata/{predicate}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Remove metadatum")
	public void removeMetadatum(@PathVariable ID subjectId, @PathVariable String predicate) {
		throw new NotImplementedClientException("Method is unsupported.");
	}
}
