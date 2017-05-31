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


import com.restdude.domain.MetadatumModel;
import com.restdude.domain.PersistableModel;
import com.restdude.domain.UploadedFileModel;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.repository.ModelRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

//import com.restdude.domain.users.model.User;

/**
 * Provides SCRUD and utility operations for {@link T} entities
 * @author manos
 *
 * @param <T> the entity type
 * @param <PK> the entity PK type
 */
@Service
public interface PersistableModelService<T extends PersistableModel<PK>, PK extends Serializable>
        extends ModelService<T, PK> {

    String PRE_AUTHORIZATION_PREFIX = "SERVICE_";

    /**
     * Override to initialize data related to your model type
     */
    void initData();

    /**
     * Return the id of the entity.
     * A generated id is not guaranteed to be available until after
     * the database insert has occurred.
     * Returns null if the entity does not yet have an id.
     *
     * @param entity entity instance
     *
     * @return id of the entity
     *
     * @throws IllegalArgumentException if the object is found not
     * to be an entity
     */
    Object getIdentifier(Object entity);

    /**
     * Find a page of results matching the given entity type and specification
     * @param entityType the root entity type
     * @param spec the query specification
     * @param pageable the page config
     * @return the page of results, may be <code>null</code>
     */
    <M extends PersistableModel<MID>, MID extends Serializable> Page<M> findRelatedPaginated(Class<M> entityType, Specification<M> spec, @NonNull Pageable pageable);

    void addMetadatum(PK subjectId, MetadatumModel dto);

    void addMetadata(PK subjectId, Collection<MetadatumModel> dtos);

    void removeMetadatum(PK subjectId, String predicate);

    /**
     * Find the other end of a ToOne relationship
     * @param id the root entity ID
     * @param fieldInfo the member/relation name
     * @return the single related entity, if any
     */
    PersistableModel findRelatedSingle(PK id, FieldInfo fieldInfo);

    /**
     * @see ModelRepository#validateConstraints(PersistableModel)
     */
    Set<ConstraintViolation<T>> validateConstraints(T resource);

    /**
     * Get the entity's file uploads for this property
     *
     * @param subjectId    the entity id
     * @param propertyName the property holding the upload(s)
     * @return the uploads
     */
    List<UploadedFileModel> getUploadsForProperty(PK subjectId, String propertyName);


    /**
     * Delete all existing resource. Do not use cascade remove (not a choice -> JPA specs)
     */
    void deleteAll();

    /**
     * Delete all existing resource, including linked entities with cascade delete
     */
    void deleteAllWithCascade();

    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    void deleteFiles(PK id, String... filenames);

    T updateFiles(@PathVariable PK id, MultipartHttpServletRequest request, HttpServletResponse response);

}