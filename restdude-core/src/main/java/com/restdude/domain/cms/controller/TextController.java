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
package com.restdude.domain.cms.controller;

import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.cms.model.Text;
import com.restdude.domain.cms.service.TextService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;


//@Controller
@Api(hidden = true, tags = "Text resources", description = "Operations about text resources")
@RequestMapping(value = "/api/rest/texts", produces = { "application/json", "application/xml" })
public class TextController extends AbstractModelController<Text, String, TextService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TextController.class);

	@Override
	@Inject
	@Qualifier("textService") // somehow required for CDI to work on 64bit JDK?
	public void setService(TextService service) {
		this.service = service;
	}
    
}
