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
package com.restdude.domain.users.controller;

import com.restdude.domain.base.controller.AbstractNoDeleteModelController;
import com.restdude.domain.users.model.UserRegistrationCodeBatch;
import com.restdude.domain.users.model.UserRegistrationCodeInfo;
import com.restdude.domain.users.service.UserRegistrationCodeBatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Api(tags = "RegistrationCodeBatches", description = "Codes management (admin, operator)")
@RequestMapping(value = "/api/rest/registrationCodeBatches", produces = {"application/json", "application/xml"})
public class UserRegistrationCodeBatchController extends AbstractNoDeleteModelController<UserRegistrationCodeBatch, String, UserRegistrationCodeBatchService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationCodeBatchController.class);
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";

	@RequestMapping(value = "{id}/csv", method = RequestMethod.GET, produces = "text/csv")
	@ResponseBody
	@ApiOperation(value = "Export batch to a spreadsheet (CSV) report", notes = "The filename will be [batch name]_[date: yyyyMMddHHmmss].csv")
	public List<UserRegistrationCodeInfo> exportToCsv(@ApiParam(name = "id", required = true, value = "string") @PathVariable String id, HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		// get batch name ot name file
		String csvFileName = new StringBuffer(this.service.findBatchName(id))
				.append('_')
				.append(sdf.format(new Date()))
				.append(".csv").toString();
		LOGGER.debug("exportToCsv, filename: {}", csvFileName);
		// tell browser to launch spreadsheet
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);
		response.setContentType("text/csv;charset=utf-8");

		// return results
		return this.service.findBatchCodes(id);
	}

	@RequestMapping(value = "csv", method = RequestMethod.GET, produces = "text/csv")
	@ResponseBody
	@ApiOperation(value = "Export batch to a spreadsheet (CSV) report", notes = "The filename will be [batch name]_[date: yyyyMMddHHmmss].csv")
	public List<UserRegistrationCodeInfo> exportToCsv(HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		// get batch name ot name file
		String csvFileName = "foobar.csv";

		// tell browser to launch spreadsheet
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);
		response.setContentType("text/csv;charset=utf-8");

		// return results
		return this.service.findBatchCodes();
	}

}
