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

import java.util.List;

import javax.inject.Named;

import com.restdude.domain.cases.model.BaseCaseComment;
import com.restdude.domain.cases.model.BaseCase;
import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.cases.model.dto.CaseCommenttInfo;
import com.restdude.domain.cases.repository.BaseCaseModelRepository;
import com.restdude.domain.cases.service.BaseCaseService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named("baseCaseService")
public class BaseCaseServiceImpl<CC extends BaseCaseComment>
        extends  AbstractCaseServiceImpl<BaseCase<?, ?>, CC, BaseCaseModelRepository>
		implements BaseCaseService{


	@Override
	public CaseWorkflow getWorkflow() {
		return null;
	}

	@Override
	public String getWorkflowName() {
		return null;
	}

	@Override
	public String getWorkflowTitle() {
		return null;
	}

	@Override
	public String getWorkflowDescription() {
		return null;
	}

	/**
	 * {@inheritDoc}

	 * @param subject
	 */
	@Override
	public List<CaseCommenttInfo> getCompactCommentsBySubject(BaseCase<?, ?> subject) {
		return null;
	}

	/**
	 * {@inheritDoc}

	 * @param persisted
	 */
	public Integer getEntryIndex(@NonNull BaseCase persisted){
		return  this.repository.getEntryIndex(persisted);
	}
}