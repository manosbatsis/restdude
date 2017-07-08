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
package com.restdude.domain.error.service.impl;

import javax.inject.Named;

import com.restdude.domain.cases.service.impl.AbstractContextServiceImpl;
import com.restdude.domain.error.model.ErrorsApplication;
import com.restdude.domain.error.repository.ErrorsApplicationRepository;
import com.restdude.domain.error.service.ErrorsApplicationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named("errorsApplicationService")
public class ErrorsApplicationServiceImpl
		extends AbstractContextServiceImpl<ErrorsApplication, ErrorsApplicationRepository>
		implements ErrorsApplicationService {


}