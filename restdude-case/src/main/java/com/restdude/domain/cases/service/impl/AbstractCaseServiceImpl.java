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

import com.restdude.domain.cases.model.AbstractCaseCommentModel;
import com.restdude.domain.cases.model.AbstractCaseModel;
import com.restdude.domain.cases.model.CaseStatus;
import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.cases.model.dto.CaseCommenttInfo;
import com.restdude.domain.cases.repository.AbstractCaseModelRepository;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public abstract class AbstractCaseServiceImpl<T extends AbstractCaseModel<?, ?, ?>, CC extends AbstractCaseCommentModel, R extends AbstractCaseModelRepository<T>>
        extends AbstractPersistableModelServiceImpl<T, String, R>        {


    public abstract CaseWorkflow getWorkflow();
    public abstract String getWorkflowName();
    public abstract String getWorkflowTitle();
    public abstract String getWorkflowDescription();


    public static final char INDEX_CHAR = '-';


    /**
     * {@inheritDoc}
     */
    public CaseStatus getStatusForNew(T unpersisted){
        CaseWorkflow workflow = this.getWorkflow();
        if(workflow != null && CollectionUtils.isNotEmpty(workflow.getStatuses())){
            return workflow.getStatuses().get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<CaseStatus> getNextStatusOptions(T unpersisted){
        CaseWorkflow workflow = this.getWorkflow();
        if(workflow != null && CollectionUtils.isNotEmpty(workflow.getStatuses())){
            return workflow.getStatuses();
        }
        return null;
    }



    /**
     * {@inheritDoc}
     */
    public abstract List<CaseCommenttInfo> getCompactCommentsBySubject(T subject);

    /**
     * {@inheritDoc}
     */
    public abstract Integer getCaseIndex(T persisted);


    /**
     * {@inheritDoc}
     */
    public List<CaseCommenttInfo> getCompactCommentsBySubject(String id){
        T resource = this.findById(id);
        return this.getCompactCommentsBySubject(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public T create(T resource) {
        log.debug("create, resource: {}", resource);

        // save case
        resource = super.create(resource);

        log.debug("create, calling getCaseIndex with: {}", resource);
        log.debug("create, this.repository: {}", this.repository);
        // TODO: tmp hack, needs to be refactored to some transaction synchronization handler in @PostPersit or whatever
        Integer caseIndex = this.getCaseIndex(resource);
        resource.setCaseIndex(caseIndex);
        resource.setName(new StringBuffer(resource.getApplicationDTO().getName()).append(INDEX_CHAR).append(caseIndex).toString());
        resource = this.repository.save(resource);
        log.debug("create, returning error:: {}", resource);

        return resource;
    }

}