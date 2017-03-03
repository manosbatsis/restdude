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

import com.restdude.mdd.annotation.model.FilePersistence;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.model.MetadatumModel;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.UploadedFileModel;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.specification.SpecificationUtils;
import lombok.NonNull;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * SCRUD service handling a specific type of {@link PersistableModel} using a {@link ModelRepository}
 *
 * @param <T>  Your resource class to manage, usually an entity class
 * @param <PK> Resource pk type, usually Long or String
 * @param <R>  The repository class to automatically inject
 */
public abstract class AbstractPersistableModelServiceImpl<T extends PersistableModel<PK>, PK extends Serializable, R extends ModelRepository<T, PK>>
        extends AbstractBaseServiceImpl
        implements PersistableModelService<T, PK>{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistableModelServiceImpl.class);

    protected R repository;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Class<T> getDomainClass() {
        return this.repository.getDomainClass();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getIdentifier(Object entity){
        return this.repository.getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
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
    public PersistableModel findRelatedEntityByOwnId(PK pk, FieldInfo fieldInfo) {
        return repository.findRelatedEntityByOwnId(pk, fieldInfo);
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
    public Page<T> findPaginated(Specification<T> spec, @NonNull Pageable pageable) {
        LOGGER.debug("findPaginated, pageable: {}", pageable);

        if (spec != null) {
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
    public void addMetadatum(PK subjectId, MetadatumModel dto) {
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
    public void addMetadata(PK subjectId, Collection<MetadatumModel> dtos) {
        if (!CollectionUtils.isEmpty(dtos)) {
            for (MetadatumModel dto : dtos) {
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
    public List<UploadedFileModel> getUploadsForProperty(PK subjectId, String propertyName) {
        return this.repository.getUploadsForProperty(subjectId, propertyName);
    }

    @Override
    @Transactional(readOnly = false)
    public T updateFiles(@PathVariable PK id, MultipartHttpServletRequest request, HttpServletResponse response) {
        T entity = this.findById(id);
        LOGGER.debug("Entity before uploading files: {}", entity);
        try {
            String basePath = new StringBuffer(this.getDomainClass().getSimpleName())
                    .append('/').append(id).append('/').toString();
            String propertyName;
            for (Iterator<String> iterator = request.getFileNames(); iterator.hasNext(); ) {
                // get the property name
                propertyName = iterator.next();

                // verify the property exists
                Field fileField = SpecificationUtils.getField(this.getDomainClass(), propertyName);
                if (fileField == null || !fileField.isAnnotationPresent(FilePersistence.class)) {
                    throw new IllegalArgumentException("No FilePersistence annotation found for member: " + propertyName);
                }

                // store the file and update the property URL
                String url = this.filePersistenceService.saveFile(fileField, request.getFile(propertyName), basePath + propertyName);
                BeanUtils.setProperty(entity, propertyName, url);

            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update files", e);
        }
        // return the updated entity
        entity = this.update(entity);

        LOGGER.debug("Entity after uploading files: {}", entity);
        return entity;
    }

    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional(readOnly = false)
    public void deleteFiles(PK id, String... filenames) {
        String basePath = new StringBuffer(this.getDomainClass().getSimpleName())
                .append('/').append(id).append('/').toString();
        List<String> keys = new LinkedList<String>();

        for (String propertyName : filenames) {
            // verify the property exists
            Field fileField = SpecificationUtils.getField(this.getDomainClass(), propertyName);
            if (fileField == null || !fileField.isAnnotationPresent(FilePersistence.class)) {
                throw new IllegalArgumentException("No FilePersistence annotation found for member: " + propertyName);
            }

            // store the file key
            keys.add(basePath + propertyName);
        }

        // delete files
        this.filePersistenceService.deleteFiles(keys.toArray(new String[keys.size()]));
    }

}