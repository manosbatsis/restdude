/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.repository;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.metadata.model.MetadataSubject;
import com.restdude.domain.metadata.model.Metadatum;
import com.restdude.mdd.specifications.SpecificationsBuilder;
import com.restdude.mdd.util.ParameterMapBackedPageRequest;
import com.restdude.util.exception.http.BadRequestException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ListUtils;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class BaseRepositoryImpl<T extends CalipsoPersistable<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ModelRepository<T, ID> {


	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryImpl.class);

	private SpecificationsBuilder<T, ID> specificationsBuilder;
	private EntityManager entityManager;
	private Class<T> domainClass;
    private List<String> uniqueFieldNames = new LinkedList<String>();
    private List<String> requiredFieldNames = new LinkedList<String>();


    /**
	 * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
	 *
     * @param domainClass must not be {@literal null}.
     * @param entityManager must not be {@literal null}.
	 */
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
		this.domainClass = domainClass;
		this.specificationsBuilder = new SpecificationsBuilder<T, ID>(this.domainClass);

        // init constraints info
        Field[] fields = FieldUtils.getFieldsWithAnnotation(this.domainClass, Column.class);
        if (fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Column column = field.getAnnotation(Column.class);

                // if unique or not-null field
                if (!field.getName().equals("id")) {
                    if (column.unique()) {
                        uniqueFieldNames.add(field.getName());
                    }
                    if (!column.nullable()) {
                        requiredFieldNames.add(field.getName());
                    }
                }

            }


        }
    }

	/***
	 * t {@inheritDoc} 
	 */
	@Override
	public Class<T> getDomainClass() {
		return this.domainClass;
	}

	/**
	 * @return the entityManager
	 */
	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}


	
	@Override
	public T merge(T entity) {
		return this.getEntityManager().merge(entity);
	}
	
	@Override
	public T persist(T entity) {
		this.getEntityManager().persist(entity);
		return entity;
	}

	public Optional<T> findOptional(ID id){
		return Optional.ofNullable(this.findOne(id));
	}
	
	
	
	@Override
	public Metadatum addMetadatum(ID subjectId, String predicate, String object) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put(predicate, object);
		List<Metadatum> saved = addMetadata(subjectId, metadata);
		if (!CollectionUtils.isEmpty(metadata)) {
			return saved.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Metadatum> addMetadata(ID subjectId,
			Map<String, String> metadata) {
		ensureMetadataIsSupported();
		List<Metadatum> saved;
		if (!CollectionUtils.isEmpty(metadata)) {
			saved = new ArrayList<Metadatum>(metadata.size());
			for (String predicate : metadata.keySet()) {
				LOGGER.info("addMetadatum subjectId: " + subjectId
						+ ", predicate: " + predicate);
				Metadatum metadatum = this.findMetadatum(subjectId, predicate);
				LOGGER.info("addMetadatum metadatum: " + metadatum);
				if (metadatum == null) {
					T entity = this.findOne(subjectId);
					// Class<?> metadatumClass = ((MetadataSubject) entity)
					// .getMetadataDomainClass();
					MetadataSubject subject = (MetadataSubject) entity;
					metadatum = this.buildMetadatum(subject, predicate,
							metadata.get(predicate));
					this.getEntityManager().persist(metadatum);
				} else {
					// if exists, only update the value
					metadatum.setObject(metadata.get(predicate));
					metadatum = this.getEntityManager().merge(metadatum);
				}

				// subject.addMetadatum(model.getPredicate(), model.getObject());
				// this.entityManager.merge(entity);
				LOGGER.info("addMetadatum saved metadatum: " + metadatum);
				saved.add(metadatum);
			}
		} else {
			saved = new ArrayList<Metadatum>(0);
		}
		LOGGER.info("addMetadatum returns: " + saved);
		return saved;
	}

	@SuppressWarnings("unchecked")
	private Metadatum buildMetadatum(MetadataSubject subject, String predicate,
			String object) {
		Class<?> metadatumClass = subject.getMetadataDomainClass();
		Metadatum metadatum = null;
		try {
			metadatum = (Metadatum) metadatumClass.getConstructor(
					this.getDomainClass(), String.class, String.class)
					.newInstance(subject, predicate, object);
		} catch (Exception e) {
			throw new RuntimeException("Failed adding metadatum", e);
		}
		return metadatum;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeMetadatum(ID subjectId, String predicate) {
		Assert.notNull(subjectId);
		Assert.notNull(predicate);
		ensureMetadataIsSupported();
		T subjectEntity = this.findOne(subjectId);
		Class<?> metadatumClass = ((MetadataSubject) subjectEntity)
				.getMetadataDomainClass();
		// TODO: refactor to criteria
		Metadatum metadatum = findMetadatum(subjectId, predicate,
				metadatumClass);
		if (metadatum != null) {
			this.getEntityManager().remove(metadatum);
		}
		// CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		// CriteriaQuery criteria = builder.createQuery(metadatumClass);
		// Root root = criteria.from(metadatumClass);
		// criteria.where( builder.equal(root.get("predicate"), predicate));

		// T entity = this.findOne(subjectId);
		// MetadataSubject subject = (MetadataSubject) entity;
		// if (subject.getMetadata() != null) {
		// subject.getMetadata().remove(predicate);
		// this.merge(entity);
		// }
	}

	@Override
	public Metadatum findMetadatum(ID subjectId, String predicate) {
		T subjectEntity = this.findOne(subjectId);
		Class<?> metadatumClass = ((MetadataSubject) subjectEntity)
				.getMetadataDomainClass();
		return this.findMetadatum(subjectId, predicate, metadatumClass);

	}

	protected Metadatum findMetadatum(ID subjectId, String predicate,
			Class<?> metadatumClass) {
		List<Metadatum> results = this
				.getEntityManager()
				.createQuery(
						"from "
								+ metadatumClass.getSimpleName()
								+ " m where m.predicate = ?1 and m.subject.id = ?2")
				.setParameter(1, predicate).setParameter(2, subjectId)
				.getResultList();
		Metadatum metadatum = results.isEmpty() ? null : results.get(0);
		return metadatum;
	}

	protected void ensureMetadataIsSupported() {
		if (!MetadataSubject.class.isAssignableFrom(getDomainClass())) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public void refresh(T entity) {
		this.getEntityManager().refresh(entity);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		// if
		if (pageable instanceof ParameterMapBackedPageRequest) {
			@SuppressWarnings("unchecked")
			Map<String, String[]> params = ((ParameterMapBackedPageRequest) pageable).getParameterMap();
			Specification<T> spec = this.specificationsBuilder.getMatchAll(getDomainClass(), params);

			return super.findAll(spec, pageable);
		} else {
			return super.findAll(pageable);
		}
	}

	/** 
	 * Get the entity's file uploads for this property
	 * @param subjectId the entity id
	 * @param propertyName the property holding the upload(s)
	 * @return the uploads
	 */
	public List<BinaryFile> getUploadsForProperty(ID subjectId, String propertyName){
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<BinaryFile> query = cb.createQuery(BinaryFile.class);
		Root<T> root = query.from(this.domainClass);
		query.where(cb.equal(root.get("id"), subjectId));
		Selection<? extends BinaryFile> join = root.join(propertyName,JoinType.INNER);
		query.select(join);
		return this.entityManager.createQuery(query).getResultList();
    }

    public List<String> validateConstraints(T resource) {
        LOGGER.debug("validateConstraints, uniqueFieldNames: {}", this.uniqueFieldNames);
        List<String> errors = new LinkedList<String>();
        // validate not-null
        for (String fieldName : this.requiredFieldNames) {
            try {
                Object value = PropertyUtils.getProperty(resource, fieldName);
                if (value == null) {
                    errors.add("Missing required property value: " + fieldName);
                }
            } catch (Exception e) {
                LOGGER.warn("Failed validating unique constrains for property: " + fieldName, e);
            }
        }


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.domainClass);
        Root<T> root = criteriaQuery.from(this.domainClass);
        List<Predicate> predicates = new ArrayList<Predicate>(this.uniqueFieldNames.size());
        try {
            for (String propertyName : this.uniqueFieldNames) {

                LOGGER.debug("validateConstraints, adding predicate for: {}", propertyName);
                Object propertyValue = PropertyUtils.getProperty(resource, propertyName);
                Predicate predicate = criteriaBuilder.equal(root.get(propertyName), propertyValue);
                predicates.add(predicate);
            }

            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<T> typedQuery = this.entityManager.createQuery(criteriaQuery);
            List<T> resultSet = typedQuery.getResultList();
            LOGGER.debug("validateConstraints, resultSet size: {}", resultSet.size());
            if (!resultSet.isEmpty()) {
                Set<String> unavailableValueFieldNames = new HashSet<String>();
                for (T match : resultSet) {
                    if (!match.getId().equals(resource.getId())) {
                        for (String propertyName : this.uniqueFieldNames) {
                            Object newValue = PropertyUtils.getProperty(resource, propertyName);
                            Object existingValue = PropertyUtils.getProperty(match, propertyName);

                            LOGGER.debug("validateConstraints, newValue: {}, existingValue: {}", newValue, existingValue);
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

        LOGGER.debug("validateConstraints, errors: {}", errors);
        return errors;
    }

    protected void validate(T resource) {
        List<String> errors = this.validateConstraints(resource);

        if (!ListUtils.isEmpty(errors)) {
            StringBuffer message = new StringBuffer("Validation failed: ")
                    .append(errors.get(0));
            if (errors.size() > 1) {
                message.append(" (")
                        .append(errors.size() - 1)
                        .append(" more)");
            }
            throw new BadRequestException(message.toString(), errors);
        }

    }

    @Override
    public <S extends T> S save(S entity) {
        entity.preSave();
        this.validate(entity);
        return super.save(entity);
    }
}
