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

import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.metadata.model.MetadataSubject;
import com.restdude.domain.metadata.model.Metadatum;
import com.restdude.mdd.specifications.SpecificationsBuilder;
import com.restdude.mdd.util.ParameterMapBackedPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ModelRepository<T, ID> {


	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryImpl.class);

	private SpecificationsBuilder<T, ID> specificationsBuilder;
	private EntityManager entityManager;
	private Class<T> domainClass;
	
	
	/**
	 * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
	 * 
	 * @param entityInformation must not be {@literal null}.
	 * @param entityManager must not be {@literal null}.
	 */
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
		this.domainClass = domainClass;
		this.specificationsBuilder = new SpecificationsBuilder<T, ID>(this.domainClass);
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
}
