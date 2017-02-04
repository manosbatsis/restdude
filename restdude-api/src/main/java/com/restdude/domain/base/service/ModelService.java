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
package com.restdude.domain.base.service;


import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.cms.model.UploadedFile;
import com.restdude.domain.metadata.model.Metadatum;
//import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.LocalUser;
import com.restdude.websocket.message.IActivityNotificationMessage;
import com.restdude.websocket.message.IMessageResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Provides SCRUD and utility operations for {@link T} entities
 * @author manos
 *
 * @param <T> the entity type
 * @param <PK> the entity PK type
 */
@Service
public interface ModelService<T extends CalipsoPersistable<PK>, PK extends Serializable>
        extends CrudService<T, PK> {

    public static final String PRE_AUTHORIZATION_PREFIX = "SERVICE_";

	/**
     * Get the entity Class corresponding to the generic T
     *
     * @return the corresponding entity Class
     */
    public Class<T> getDomainClass();

    /**
     * Get the current user's details
	 * @return
	 */
	public ICalipsoUserDetails getPrincipal();

    public void addMetadatum(PK subjectId, Metadatum dto);

    public void addMetadata(PK subjectId, Collection<Metadatum> dtos);

    public void removeMetadatum(PK subjectId, String predicate);

    /**
     * Get the entity's file uploads for this property
     *
     * @param subjectId    the entity pk
     * @param propertyName the property holding the upload(s)
     * @return the uploads
     */
    public List<UploadedFile> getUploadsForProperty(PK subjectId, String propertyName);

    /**
	 * Get the current user's details from the DB
	 * @return
	 */
	public LocalUser getPrincipalLocalUser();

	public void sendStompActivityMessage(IActivityNotificationMessage msg, String useername);

	public void sendStompActivityMessage(IActivityNotificationMessage msg, Iterable<String> useernames);

	public void sendStompStateChangeMessage(IMessageResource msg, String useername);

	public void sendStompStateChangeMessage(IMessageResource msg, Iterable<String> useernames);


    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    public void deleteFiles(PK id, String... filenames);

    public T updateFiles(@PathVariable PK id, MultipartHttpServletRequest request, HttpServletResponse response);
}