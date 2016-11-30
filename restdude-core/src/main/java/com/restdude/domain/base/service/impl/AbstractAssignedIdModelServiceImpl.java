package com.restdude.domain.base.service.impl;

import com.restdude.domain.base.model.AbstractAssignedIdPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.AbstractAssignedIdModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Created by manos on 30/11/2016.
 */
public class AbstractAssignedIdModelServiceImpl<T extends AbstractAssignedIdPersistable<ID>, ID extends Serializable, R extends ModelRepository<T, ID>>
        extends AbstractModelServiceImpl<T, ID, R> implements AbstractAssignedIdModelService<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAssignedIdModelServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_CREATE)
    public T findOrCreate(@P("resource") T resource) {
        Assert.notNull(resource, "Resource can't be null");
        Assert.notNull(resource.getId(), "Resource ID can't be null");
        T persisted = this.repository.findOne(resource.getId());
        if (persisted != null) {
            resource = persisted;
            LOGGER.debug("Returning pre-persisted {} with ID: {}", this.getDomainClass().getName(), resource.getId());
        } else {
            resource = this.create(resource);
            LOGGER.debug("Returning newly persisted {} with ID: {}", this.getDomainClass().getName(), resource.getId());
        }
        return resource;
    }
}
