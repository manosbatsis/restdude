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

import java.util.Objects;

import com.restdude.domain.cases.model.BaseCase;
import com.restdude.domain.cases.model.BaseCaseComment;
import com.restdude.domain.cases.repository.CaseCommentNoRepositoryBean;
import com.restdude.domain.cases.service.AbstractCaseService;
import com.restdude.domain.event.EntityCreatedEvent;
import com.restdude.domain.event.EntityUpdatedEvent;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractCaseCommentServiceImpl<T extends BaseCaseComment<C,T>, C extends BaseCase<C, T>, R extends CaseCommentNoRepositoryBean<T>>
        extends AbstractPersistableModelServiceImpl<T, String, R>        {

    public static final char INDEX_CHAR = '-';

    private AbstractCaseService<C, T> caseService;

    protected AbstractCaseService<C, T> getCaseService() {
        return caseService;
    }

    @Autowired
    public void setCaseService(AbstractCaseService<C, T> caseService) {
        this.caseService = caseService;
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
    @Override
    @Transactional(readOnly = false)
    public T create(@NonNull T resource) {
        resource = this.repository.persist(resource);

        log.debug("create, calling getEntryIndex with: {}", resource);
        log.debug("create, this.repository: {}", this.repository);
        // TODO: tmp hack, needs to be refactored to some custom sequence or adapter
        Integer caseIndex = this.getEntryIndex(resource);
        resource.setEntryIndex(caseIndex);
        resource.setName(new StringBuffer(resource.getParent().getName()).append(INDEX_CHAR).append(caseIndex).toString());
        //resource = this.repository.save(resource);
        log.debug("create, returning: {}", resource);

        ApplicationEvent event;

        // create different events for simple comment VS more complex case updates
        // i.e. if some of the case-patching props is not null
        if(!(Objects.isNull(resource.getPriority()) && Objects.isNull(resource.getStatus()) &&Objects.isNull(resource.getAssignee()))){

            // patch the case this comment belongs to
            C commentCase = patchParentCase(resource);

            // create case updated event
            event = new EntityUpdatedEvent<C>(commentCase);

        }
        else{
            // create new comment event
            event = new EntityCreatedEvent<T>(resource);
        }

        // publish event
        this.applicationEventPublisher.publishEvent(event);

        return resource;
    }

    protected C patchParentCase(@NonNull T resource) {
        // patch case
        C commentCase = this.caseService.findById(resource.getParentCase().getId());
        // update priority?
        if(Objects.isNull(resource.getPriority())){
			commentCase.setPriority(resource.getPriority());
		}
        // update status?
        if(Objects.isNull(resource.getStatus())){
			commentCase.setStatus(resource.getStatus());
		}
        // update assignee?
        if(Objects.isNull(resource.getAssignee())){
			commentCase.setAssignee(resource.getAssignee());
		}
        return commentCase;
    }

}