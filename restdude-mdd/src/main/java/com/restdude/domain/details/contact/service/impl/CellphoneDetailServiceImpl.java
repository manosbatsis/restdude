/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.details.contact.service.impl;

import com.restdude.domain.details.contact.model.CellphoneDetail;
import com.restdude.domain.details.contact.repository.CellphoneDetailRepository;
import com.restdude.domain.details.contact.service.CellphoneDetailService;

import javax.inject.Named;

@Named(CellphoneDetailService.BEAN_ID)
public class CellphoneDetailServiceImpl extends AbstractContactDetailServiceImpl<CellphoneDetail, String, CellphoneDetailRepository> implements CellphoneDetailService {

}
