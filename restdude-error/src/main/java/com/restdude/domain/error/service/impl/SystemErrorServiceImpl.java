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

import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.repository.SystemErrorRepository;
import com.restdude.domain.error.service.SystemErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named(SystemErrorService.BEAN_ID)
public class SystemErrorServiceImpl extends AbstractErrorServiceImpl<SystemError, String, SystemErrorRepository>
        implements SystemErrorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemErrorServiceImpl.class);



}