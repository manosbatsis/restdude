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
package com.restdude.domain.error.repository;

import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.error.model.BaseError;
import com.restdude.mdd.repository.ModelRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaseErrorRepository extends ModelRepository<BaseError, String> {

    String ERRORS_WORKFLOW_NAME = "BASEERR";

    @Query(value = "select w from CaseWorkflow w  where w.maintainerContext.owner.username = 'system' and w.maintainerContext.title = 'System' and w.name = '" + ERRORS_WORKFLOW_NAME + "'")
    CaseWorkflow getWorkflow();
}
