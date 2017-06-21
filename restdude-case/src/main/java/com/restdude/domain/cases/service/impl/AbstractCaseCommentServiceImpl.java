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

import com.restdude.domain.PersistableModel;
import com.restdude.domain.cases.model.BaseCaseComment;
import com.restdude.domain.cases.repository.CaseCommentNoRepositoryBean;
import com.restdude.domain.event.EntityCreatedEvent;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
public abstract class AbstractCaseCommentServiceImpl<T extends BaseCaseComment<?,?>, R extends CaseCommentNoRepositoryBean<T>>
        extends AbstractPersistableModelServiceImpl<T, String, R>        {

    public static final char INDEX_CHAR = '-';


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
    public T create(T resource) {
        // save case
        Assert.notNull(resource, "Resource can't be null");
        log.debug("create resource: {}", resource);
        resource = this.repository.persist(resource);
        BaseCaseComment persisted = (BaseCaseComment) resource;

        log.debug("create, calling getEntryIndex with: {}", resource);
        log.debug("create, this.repository: {}", this.repository);
        // TODO: tmp hack, needs to be refactored to some custom sequence or adapter
        Integer caseIndex = this.getEntryIndex(resource);
        persisted.setEntryIndex(caseIndex);
        persisted.setName(new StringBuffer(persisted.getParent().getName()).append(INDEX_CHAR).append(caseIndex).toString());
        //resource = this.repository.save(resource);
        log.debug("create, returning: {}", resource);

        log.debug("create applicationEventPublisher: {}", this.applicationEventPublisher);
        EntityCreatedEvent<T> event = new EntityCreatedEvent<T>(resource);
        log.debug("create event: {}", event);
        this.applicationEventPublisher.publishEvent(event);

        return resource;
    }

}