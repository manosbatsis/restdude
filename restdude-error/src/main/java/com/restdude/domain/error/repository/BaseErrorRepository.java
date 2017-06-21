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
import com.restdude.domain.cases.model.dto.CaseCommenttInfo;
import com.restdude.domain.cases.repository.CaseNoRepositoryBean;
import com.restdude.domain.error.model.BaseError;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface BaseErrorRepository extends CaseNoRepositoryBean<BaseError> {

    String ERRORS_WORKFLOW_NAME = "ALLERR";
    String ERRORS_WORKFLOW_TITLE = "All Errors";
    String ERRORS_WORKFLOW_DESCRIPTION = "Global errors, including system and client entries";

    @Query(value = "select w from CaseWorkflow w  where w.maintainerContext.owner.username = 'system' and w.maintainerContext.title = 'System' and w.name = '" + ERRORS_WORKFLOW_NAME + "'")
    CaseWorkflow getWorkflow();

    String SELECT_NEW_ERROR_COMMENT_INFOS = "select new com.restdude.domain.cases.model.dto.CaseCommenttInfo";
    String NEW_ERROR_COMMENT_INFO_PARAMS = "e.id, e.detail, e.createdDate, " +
            "e.createdBy.id, e.createdBy.firstName, e.createdBy.lastName, e.createdBy.username, e.createdBy.emailHash, e.createdBy.avatarUrl ";

    String GET_ERROR_COMMENTS = SELECT_NEW_ERROR_COMMENT_INFOS + "(" + NEW_ERROR_COMMENT_INFO_PARAMS + ") from ErrorComment e where e.parent = :#{#parent}  order by e.createdDate ASC";

    @Query(value = GET_ERROR_COMMENTS)
    List<CaseCommenttInfo> getCompactCommentsBySubject(@P("parent") @Param("parent") BaseError parent);

}
