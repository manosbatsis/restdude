package com.restdude.domain.base.service.impl;


import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.CrudService;
import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.metadata.model.Metadatum;
import com.restdude.mdd.annotation.ModelDrivenPreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * CRUD service that uses a {@link org.springframework.data.repository.PagingAndSortingRepository} Spring Data repository implementation
 * <p>
 * You should extend it and inject your Repository bean by overriding {@link #setRepository(ModelRepository)}
 *
 * @param <T>  Your resource class to manage, usually an entity class
 * @param <ID> Resource id type, usually Long or String
 * @param <R>  The repository class
 */
@Transactional(readOnly = true, rollbackFor = Exception.class)
public abstract class CrudServiceImpl<T extends CalipsoPersistable<ID>, ID extends Serializable, R extends ModelRepository<T, ID>> implements
        CrudService<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudServiceImpl.class);

    protected R repository;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }


    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Class<T> getDomainClass() {
        return this.repository.getDomainClass();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public T create(@P("resource") T resource) {
        Assert.notNull(resource, "Resource can't be null");
        LOGGER.debug("create resource: {}", resource);
        resource = repository.persist(resource);
        this.postCreate(resource);
        return resource;
    }


    @Override
    public void postCreate(T resource) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public T update(@P("resource") T resource) {
        Assert.notNull(resource, "Resource can't be null");
        LOGGER.debug("update resource: {}", resource);
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public T patch(@P("resource") T resource) {
        LOGGER.debug("patch resource: {}", resource);
        return repository.patch(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public void delete(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        repository.delete(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public void delete(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        repository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public void deleteAllWithCascade() {
        Iterable<T> list = repository.findAll();
        for (T entity : list) {
            repository.delete(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public T findById(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        return repository.findOne(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public Iterable<T> findByIds(Set<ID> ids) {
        Assert.notNull(ids, "Resource ids can't be null");
        return repository.findAll(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public Page<T> findPaginated(Pageable pageRequest) {
        Assert.notNull(pageRequest, "page request can't be null");
        return repository.findAll(pageRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public Long count() {
        return repository.count();
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public Set<ConstraintViolation<T>> validateConstraints(T resource) {
        return this.repository.validateConstraints(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void addMetadatum(ID subjectId, Metadatum dto) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addMetadatum subjectId: " + subjectId + ", metadatum: " + dto);
        }
        this.repository.addMetadatum(subjectId, dto.getPredicate(),
                dto.getObject());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void addMetadata(ID subjectId, Collection<Metadatum> dtos) {
        if (!CollectionUtils.isEmpty(dtos)) {
            for (Metadatum dto : dtos) {
                this.addMetadatum(subjectId, dto);
            }
        }
    }

    @Transactional(readOnly = false)
    public void removeMetadatum(ID subjectId, String predicate) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("removeMetadatum subjectId: " + subjectId + ", predicate: "
                    + predicate);
        }
        this.repository.removeMetadatum(subjectId, predicate);
    }


    /**
     * Get the entity's file uploads for this propert
     *
     * @param subjectId    the entity id
     * @param propertyName the property holding the upload(s)
     * @return the uploads
     */
    public List<BinaryFile> getUploadsForProperty(ID subjectId, String propertyName) {
        return this.repository.getUploadsForProperty(subjectId, propertyName);
    }

}
