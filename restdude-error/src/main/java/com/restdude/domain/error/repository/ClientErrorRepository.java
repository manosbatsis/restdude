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
import com.restdude.domain.cases.repository.AbstractCaseModelRepository;
import com.restdude.domain.error.model.BaseError;
import com.restdude.domain.error.model.ClientError;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface ClientErrorRepository extends AbstractCaseModelRepository<ClientError> {

    String ERRORS_WORKFLOW_NAME = "WEBERR";
    String ERRORS_WORKFLOW_TITLE = "Client Errors";
    String ERRORS_WORKFLOW_DESCRIPTION = "Global client  (web UI) error entries";

    @Query(value = "select w from CaseWorkflow w  where w.maintainerContext.owner.username = 'system' and w.maintainerContext.title = 'System' and w.name = '" + ERRORS_WORKFLOW_NAME + "'")
    CaseWorkflow getWorkflow();

    @Query(value = "select count(c)+1 from  BaseError c where c.application = :#{#unIndexed.application}  and c.createdDate  <  :#{#unIndexed.createdDate} ")
    Integer getCaseIndex( @Param("unIndexed") BaseError unIndexed);


    String SELECT_NEW_ERROR_COMMENT_INFOS = "select new com.restdude.domain.cases.model.dto.CaseCommenttInfo";
    String NEW_ERROR_COMMENT_INFO_PARAMS = "e.id, e.content, e.createdDate, " +
            "e.createdBy.id, e.createdBy.firstName, e.createdBy.lastName, e.createdBy.username, e.createdBy.emailHash, e.createdBy.avatarUrl ";

    String GET_ERROR_COMMENTS = SELECT_NEW_ERROR_COMMENT_INFOS + "(" + NEW_ERROR_COMMENT_INFO_PARAMS + ") from ErrorComment e where e.subject = :#{#subject}  order by e.createdDate ASC";

    @Query(value = GET_ERROR_COMMENTS)
    List<CaseCommenttInfo> getCompactCommentsBySubject(@P("subject") @Param("subject") ClientError subject);
}
