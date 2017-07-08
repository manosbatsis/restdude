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
package com.restdude.domain.cases.service.impl;


import javax.inject.Named;

import com.restdude.domain.cases.model.CaseStatus;
import com.restdude.domain.cases.service.CaseStatusService;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named("caseStatusService")
public class CaseStatusServiceImpl extends AbstractPersistableModelServiceImpl<CaseStatus, String, ModelRepository<CaseStatus, String>>
        implements CaseStatusService {



}