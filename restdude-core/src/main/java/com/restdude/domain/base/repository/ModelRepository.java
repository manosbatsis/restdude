/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General License for more details.
 *
 * You should have received a copy of the GNU Lesser General License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.repository;

import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.metadata.model.Metadatum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic repository that provides SCRUD and utility methods based on domain and id type variables.
 * 
 * @param <T> the domain type the repository manages
 * @param <ID> the type of the id of the entity the repository manages
 * 
 * @see org.springframework.data.domain.Sort
 * @see org.springframework.data.domain.Pageable
 * @see org.springframework.data.domain.Page
 */
@NoRepositoryBean
public interface ModelRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

	EntityManager getEntityManager();
	
	/**
	 * Get the domain type class
	 * @return the domain type class
	 */
	Class<T> getDomainClass();

	/**
	 * Retrieves a container holding the entity in case of an id match or nothing (i.e. null) otherwise.
	 * @param id must not be {@literal null}.
	 * @return the container
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	Optional<T> findOptional(ID id);
	
	/**
	 * Flushes all pending changes to the database.
	 */
	void flush();

	/**
	 * Deletes the given entities in a batch which means it will create a single {@link Query}. Assume that we will clear
	 * the {@link javax.persistence.EntityManager} after the call.
	 * 
	 * @param entities
	 */
	void deleteInBatch(Iterable<T> entities);

	/**
	 * Deletes all entites in a batch call.
	 */
	void deleteAllInBatch();

	/**
	 * Returns a reference to the entity with the given identifier.
	 * 
	 * @param id must not be {@literal null}.
	 * @return a reference to the entity with the given identifier.
	 * @see EntityManager#getReference(Class, Object)
	 */
	T getOne(ID id);

	
	/**
	 * @param entity
	 * @return
	 */
	T merge(T entity);


	/**
	 * @see javax.persistence.EntityManager.refresh(T)
	 * @param entity
	 */
	void refresh(T entity);

	/**
	 * @see javax.persistence.EntityManager.persist(T)
	 * @param entity
	 */
	T persist(T entity);

	Metadatum addMetadatum(ID subjectId, String predicate, String object);

	List<Metadatum> addMetadata(ID subjectId, Map<String, String> metadata);

	void removeMetadatum(ID subjectId, String predicate);

	Metadatum findMetadatum(ID subjectId, String predicate);
	
	/** 
	 * Get the entity's file uploads for this property
	 * @param subjectId the entity id
	 * @param propertyName the property holding the upload(s)
	 * @return the uploads
	 */
	List<BinaryFile> getUploadsForProperty(ID subjectId, String propertyName);

}
