package com.restdude.domain.base.service.impl;


import com.restdude.auth.userdetails.controller.form.ValidatorUtil;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.CrudService;
import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.metadata.model.Metadatum;
import com.restdude.mdd.util.EntityUtil;
import com.restdude.util.exception.http.BeanValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
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
public class CrudServiceImpl<T extends CalipsoPersistable<ID>, ID extends Serializable, R extends ModelRepository<T, ID>> implements
        CrudService<T, ID>, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudServiceImpl.class);

    protected R repository;
    private Validator validator;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // init unique field names
        //this.uniqueFieldNames =
        ValidatorUtil.getUniqueFieldNames(this.getDomainClass());
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
    @PreAuthorize(T.PRE_AUTHORIZE_CREATE)
    public T create(@P("resource") T resource) {
        Assert.notNull(resource, "Resource can't be null");
        this.validate(resource);
        resource = repository.save(resource);
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
    @PreAuthorize(T.PRE_AUTHORIZE_UPDATE)
    public T update(@P("resource") T resource) {
        Assert.notNull(resource, "Resource can't be null");
        this.validate(resource);
        return repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_UPDATE)
    public T patch(@P("resource") T resource) {
        // make sure entity is set to support partial updates
        T persisted = this.findById(resource.getId());
        // copy non-null properties to persisted
        BeanUtils.copyProperties(resource, persisted, EntityUtil.getNullPropertyNames(resource));
        resource = persisted;
        // FW to normal update
        return this.update(persisted);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_DELETE)
    public void delete(T resource) {
        Assert.notNull(resource, "Resource can't be null");
        repository.delete(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_DELETE_BY_ID)
    public void delete(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        repository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_DELETE_ALL)
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_DELETE_WITH_CASCADE)
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
    @PreAuthorize(T.PRE_AUTHORIZE_VIEW)
    public T findById(ID id) {
        Assert.notNull(id, "Resource ID can't be null");
        return repository.findOne(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(T.PRE_AUTHORIZE_FIND_BY_IDS)
    public Iterable<T> findByIds(Set<ID> ids) {
        Assert.notNull(ids, "Resource ids can't be null");
        return repository.findAll(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(T.PRE_AUTHORIZE_FIND_ALL)
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(T.PRE_AUTHORIZE_SEARCH)
    public Page<T> findAll(Pageable pageRequest) {
        Assert.notNull(pageRequest, "page request can't be null");
        return repository.findAll(pageRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(T.PRE_AUTHORIZE_COUNT)
    public Long count() {
        return repository.count();
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public Set<ConstraintViolation<T>> validateConstraints(T resource) {
        Set<ConstraintViolation<T>> constraintViolations = validator.<T>validate(resource);

        return constraintViolations;
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

    /*
        protected List<String> validateColumnConstraints(T resource) {
            LOGGER.debug("validateColumnConstraints, uniqueFieldNames: {}", this.uniqueFieldNames);
            List<String> errors = new LinkedList<String>();

            EntityManager em = this.repository.getEntityManager();
            CriteriaBuilder criteriaBuilder = this.repository.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getDomainClass());
            Root<T> root = criteriaQuery.from(this.getDomainClass());
            List<Predicate> predicates = new ArrayList<Predicate>(this.uniqueFieldNames.size());
            try {
                for (String propertyName : this.uniqueFieldNames) {

                    LOGGER.debug("validateColumnConstraints, adding predicate for: {}", propertyName);
                    Object propertyValue = PropertyUtils.getProperty(resource, propertyName);
                    Predicate predicate = criteriaBuilder.equal(root.get(propertyName), propertyValue);
                    predicates.add(predicate);
                }

                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
                List<T> resultSet = typedQuery.getResultList();
                LOGGER.debug("validateColumnConstraints, resultSet size: {}", resultSet.size());
                if (!resultSet.isEmpty()) {
                    Set<String> unavailableValueFieldNames = new HashSet<String>();
                    for (T match : resultSet) {
                        if (!match.getId().equals(resource.getId())) {
                            for (String propertyName : this.uniqueFieldNames) {
                                Object newValue = PropertyUtils.getProperty(resource, propertyName);
                                Object existingValue = PropertyUtils.getProperty(match, propertyName);

                                LOGGER.debug("validateColumnConstraints, newValue: {}, existingValue: {}", newValue, existingValue);
                                if (newValue != null && newValue.equals(existingValue)) {
                                    unavailableValueFieldNames.add(propertyName);
                                }
                            }
                        }
                    }

                    for (String propertyName : unavailableValueFieldNames) {
                        errors.add("Value already exists for field " + propertyName);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error while validating constraints", e);
            }

            LOGGER.debug("validateColumnConstraints, errors: {}", errors);
            return errors;
        }
    */
    protected void validate(T resource) {
        resource.preSave();
        Set<ConstraintViolation<T>> violations = this.validateConstraints(resource);
        if (!CollectionUtils.isEmpty(violations)) {
            Set<ConstraintViolation> errors = new HashSet<ConstraintViolation>();
            errors.addAll(violations);
            throw new BeanValidationException("Validation failed", errors);
        }


    }
}
