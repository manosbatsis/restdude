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

import com.restdude.domain.cms.service.BinaryFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping(value = "/api/rest/binaryFiles")
public class UploadsController  {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadsController.class);
	private BinaryFileService service;

//	@Override
	@Inject
	@Qualifier("binaryFileService") // somehow required for CDI to work on 64bit JDK?
	public void setService(BinaryFileService service) {
		this.service = service;
	}
	
//
//    @RequestMapping
//    public String index() {
//        LOGGER.info("BinaryFileController home");
//        return "file/index";
//    }
    

    
    
}
