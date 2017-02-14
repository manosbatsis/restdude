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


import com.restdude.mdd.model.MetadatumModel;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.UploadedFileModel;
import com.restdude.mdd.repository.ModelRepository;
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

    void addMetadatum(PK subjectId, MetadatumModel dto);

    void addMetadata(PK subjectId, Collection<MetadatumModel> dtos);

    void removeMetadatum(PK subjectId, String predicate);


    /**
     * @see ModelRepository#validateConstraints(PersistableModel)
     */
    Set<ConstraintViolation<T>> validateConstraints(T resource);

    /**
     * Get the entity's file uploads for this property
     *
     * @param subjectId    the entity pk
     * @param propertyName the property holding the upload(s)
     * @return the uploads
     */
    List<UploadedFileModel> getUploadsForProperty(PK subjectId, String propertyName);

    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    void deleteFiles(PK id, String... filenames);

    T updateFiles(@PathVariable PK id, MultipartHttpServletRequest request, HttpServletResponse response);
}