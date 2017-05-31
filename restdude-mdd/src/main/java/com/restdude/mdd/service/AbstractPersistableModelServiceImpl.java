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

import com.restdude.auth.model.UserDetailsAuthenticationToken;
import com.restdude.auth.userdetails.model.UserDetailsImpl;
import com.restdude.domain.MetadatumModel;
import com.restdude.domain.PersistableModel;
import com.restdude.domain.Roles;
import com.restdude.domain.UploadedFileModel;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.FilePersistence;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.specification.SpecificationUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * @param <PK> Resource id type, usually Long or String
 * @param <R>  The repository class to automatically inject
 */
@Slf4j
public abstract class AbstractPersistableModelServiceImpl<T extends PersistableModel<PK>, PK extends Serializable, R extends ModelRepository<T, PK>>
        extends AbstractBaseServiceImpl
        implements PersistableModelService<T, PK>{

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
    public final void initData() {

        // load system user if it exists
        User systemUser = this.userRepository.findByUsername("admin");
        log.debug("initData, available users: {}, systemUser: {}", this.userRepository.count(), systemUser);
        List<User> users = this.userRepository.findAll();
        for(User u : users){
            log.debug("initData, available user: {}", u);
        }

        Authentication auth = null;

        // init auth with system user or emulate
        if(systemUser != null){
            auth = new UserDetailsAuthenticationToken(UserDetailsImpl.fromUser(systemUser));
        }
        else{
            auth = new AnonymousAuthenticationToken(this.getClass().getName(), this.getClass().getName(),
                            Arrays.asList(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority(Roles.ROLE_USER), new SimpleGrantedAuthority(Roles.ROLE_ADMIN)}));
        }

        // login
        SecurityContextHolder.getContext().setAuthentication(auth);

        // init data
        this.initDataOverride(systemUser);

        // logout
        SecurityContextHolder.clearContext();
    }

    /**
     * {@inheritDoc}
     */
    protected void initDataOverride(User systemUser){
        // noop, override if needed
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public T create(@P("resource") T resource) {
        Assert.notNull(resource, "Resource can't be null");
        log.debug("create resource: {}", resource);
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
        log.debug("update resource: {}", resource);
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public T patch(@P("resource") T resource) {
        log.debug("patch resource: {}", resource);
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
    public PersistableModel findRelatedSingle(@NonNull PK id, @NonNull FieldInfo fieldInfo) {
        // throw error if not valid or linkable relationship
        if(!fieldInfo.isLinkableResource() || !fieldInfo.isToOne()){
            throw new IllegalArgumentException("Related must be linkable and *ToOne");
        }
        return repository.findRelatedEntityByOwnId(id, fieldInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <M extends PersistableModel<MID>, MID extends Serializable> Page<M>  findRelatedPaginated(Class<M> entityType, Specification<M> spec, @NonNull Pageable pageable) {
        ModelRepository<M, MID> repo = (ModelRepository) this.repositoryRegistryService.getRepositoryFor(entityType);

        if(repo == null){
            throw new  IllegalArgumentException("Could not find a repository for model type: " + entityType);
        }

        if (spec != null) {
            return repo.findAll(spec, pageable);
        } else {
            return repo.findAll(pageable);
        }
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
        log.debug("findPaginated, pageable: {}", pageable);

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
        if (log.isDebugEnabled()) {
            log.debug("addMetadatum subjectId: " + subjectId + ", metadatum: " + dto);
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
        if (log.isDebugEnabled()) {
            log.debug("removeMetadatum subjectId: " + subjectId + ", predicate: "
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
    public List<UploadedFileModel> getUploadsForProperty(PK subjectId, String propertyName) {
        return this.repository.getUploadsForProperty(subjectId, propertyName);
    }

    @Override
    @Transactional(readOnly = false)
    public T updateFiles(@PathVariable PK id, MultipartHttpServletRequest request, HttpServletResponse response) {
        T entity = this.findById(id);
        log.debug("Entity before uploading files: {}", entity);
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

        log.debug("Entity after uploading files: {}", entity);
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