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
import java.util.Objects;

import com.restdude.domain.cases.model.BaseCase;
import com.restdude.domain.cases.model.BaseCaseComment;
import com.restdude.domain.cases.model.CaseStatus;
import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.cases.model.SpaceCasesApp;
import com.restdude.domain.cases.model.dto.CaseCommenttInfo;
import com.restdude.domain.cases.repository.CaseNoRepositoryBean;
import com.restdude.domain.cases.repository.CaseTargetRepository;
import com.restdude.domain.cases.repository.CaseWorkflowRepository;
import com.restdude.domain.cases.repository.SpaceCasesAppRepository;
import com.restdude.domain.event.EntityCreatedEvent;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractCaseServiceImpl<T extends BaseCase<?, ?>, CC extends BaseCaseComment, R extends CaseNoRepositoryBean<T>>
        extends AbstractPersistableModelServiceImpl<T, String, R> {

    public static final char INDEX_CHAR = '-';

    @Getter private SpaceCasesAppRepository spaceCasesAppRepository;
    @Getter private CaseWorkflowRepository caseWorkflowRepository;
    @Getter private CaseTargetRepository caseTargetRepository;


    public abstract CaseWorkflow getWorkflow();
    public abstract String getWorkflowName();
    public abstract String getWorkflowTitle();
    public abstract String getWorkflowDescription();

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setSpaceCasesAppRepository(SpaceCasesAppRepository spaceCasesAppRepository) {
        this.spaceCasesAppRepository = spaceCasesAppRepository;
    }

    @Autowired
    public void setCaseWorkflowRepository(CaseWorkflowRepository caseWorkflowRepository) {
        this.caseWorkflowRepository = caseWorkflowRepository;
    }

    @Autowired
    public void setCaseTargetRepository(CaseTargetRepository caseTargetRepository) {
        this.caseTargetRepository = caseTargetRepository;
    }

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
    public Integer getEntryIndex(T persisted){
        return this.repository.getEntryIndex(persisted);
    }


    /**
     * {@inheritDoc}
     */
    public abstract List<CaseCommenttInfo> getCompactCommentsBySubject(T subject);

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
    @ModelDrivenPreAuth
    public T create(@NonNull @P("resource") T resource) {

        // load parent
        SpaceCasesApp spaceCasesApp = null;
        // set both parent/parent app if needed
        if(Objects.nonNull(resource.getParent())){
            spaceCasesApp = this.spaceCasesAppRepository.findOne(resource.getParent().getId());
            resource.setParent(spaceCasesApp);
            resource.setParentApplication(spaceCasesApp);
        }
        else if(Objects.nonNull(resource.getParentApplication())){
            spaceCasesApp = this.spaceCasesAppRepository.findOne(resource.getParentApplication().getId());
            resource.setParent(spaceCasesApp);
            resource.setParentApplication(spaceCasesApp);
        }
        // set status if null
        if(Objects.isNull(resource.getStatus()) && Objects.nonNull(spaceCasesApp)){
           resource.setStatus(spaceCasesApp.getWorkflow().getStatuses().get(0));
        }



        log.debug("create, resource: {}", resource);
        resource = this.repository.persist(resource);

        log.debug("create, calling getEntryIndex with: {}", resource);
        log.debug("create, this.repository: {}", this.repository);
        // TODO: tmp hack, needs to be refactored to some custom sequence or adapter
        Integer caseIndex = this.getEntryIndex(resource);
        resource.setEntryIndex(caseIndex);
        resource.setName(new StringBuffer(spaceCasesApp.getName()).append(INDEX_CHAR).append(caseIndex).toString());
        resource.preSave();
        //resource = this.repository.save(resource);
        log.debug("create, returning: {}", resource);

        EntityCreatedEvent<T> event = new EntityCreatedEvent<T>(resource);
        this.applicationEventPublisher.publishEvent(event);

        return resource;
    }

}