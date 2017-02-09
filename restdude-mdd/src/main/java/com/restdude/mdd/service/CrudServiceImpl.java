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
package com.restdude.mdd.service;


import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.CrudService;
import com.restdude.domain.cms.model.UploadedFile;
import com.restdude.domain.metadata.model.Metadatum;
import com.restdude.domain.base.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.specifications.SpecificationsBuilder;
import com.restdude.mdd.util.ParameterMapBackedPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CRUD service that uses a {@link org.springframework.data.repository.PagingAndSortingRepository} Spring Data repository implementation
 * <p>
 * You should extend it and inject your Repository bean by overriding {@link #setRepository(ModelRepository)}
 *
 * @param <T>  Your resource class to manage, usually an entity class
 * @param <PK> Resource pk type, usually Long or String
 * @param <R>  The repository class
 */
@Transactional(readOnly = true, rollbackFor = Exception.class)
public abstract class CrudServiceImpl<T extends CalipsoPersistable<PK>, PK extends Serializable, R extends ModelRepository<T, PK>> implements
        CrudService<T, PK>, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudServiceImpl.class);

    private SpecificationsBuilder<T, PK> specificationsBuilder;
    private ConversionService conversionService;
    protected R repository;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        this.specificationsBuilder = new SpecificationsBuilder<T, PK>(this.getDomainClass(), this.conversionService);
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
    public void delete(PK id) {
        Assert.notNull(id, "Resource PK can't be null");
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
    public T findById(PK id) {
        Assert.notNull(id, "Resource PK can't be null");
        return repository.findOne(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public List<T> findByIds(Set<PK> ids) {
        Assert.notNull(ids, "Resource ids can't be null");
        return repository.findAll(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ModelDrivenPreAuth
    public Page<T> findPaginated(Pageable pageable) {
        Assert.notNull(pageable, "page request can't be null");
        LOGGER.debug("findPaginated, pageable: {}", pageable);

        // if
        if (pageable instanceof ParameterMapBackedPageRequest) {
            @SuppressWarnings("unchecked")
            Map<String, String[]> params = ((ParameterMapBackedPageRequest) pageable).getParameterMap();
            Specification<T> spec = this.specificationsBuilder.<T>build(params);

            LOGGER.debug("findPaginated, spec: {}", spec);
            return this.repository.findAll(spec, pageable);
        } else {
            return this.repository.findAll(pageable);
        }
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
    public void addMetadatum(PK subjectId, Metadatum dto) {
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
    public void addMetadata(PK subjectId, Collection<Metadatum> dtos) {
        if (!CollectionUtils.isEmpty(dtos)) {
            for (Metadatum dto : dtos) {
                this.addMetadatum(subjectId, dto);
            }
        }
    }

    @Transactional(readOnly = false)
    public void removeMetadatum(PK subjectId, String predicate) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("removeMetadatum subjectId: " + subjectId + ", predicate: "
                    + predicate);
        }
        this.repository.removeMetadatum(subjectId, predicate);
    }


    /**
     * Get the entity's file uploads for this propert
     *
     * @param subjectId    the entity pk
     * @param propertyName the property holding the upload(s)
     * @return the uploads
     */
    public List<UploadedFile> getUploadsForProperty(PK subjectId, String propertyName) {
        return this.repository.getUploadsForProperty(subjectId, propertyName);
    }

}
