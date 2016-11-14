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
package com.restdude.domain.base.service;

import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.metadata.model.Metadatum;
import org.resthub.common.service.CrudService;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * CRUD Service interface.
 * 
 * @param <T>
 *            Your resource POJO to manage, maybe an entity or DTO class
 * @param <ID>
 *            Resource id type, usually Long or String
 */
public interface GenericService<T extends Persistable<ID>, ID extends Serializable>
		extends
		CrudService<T, ID> {

	/**
	 * Get the entity Class corresponding to the generic T
	 * 
	 * @return the corresponding entity Class
	 */
	public Class<T> getDomainClass();

	public void addMetadatum(ID subjectId, Metadatum dto);

	public void addMetadata(ID subjectId, Collection<Metadatum> dtos);

	public void removeMetadatum(ID subjectId, String predicate);
	/** 
	 * Get the entity's file uploads for this property
	 * @param subjectId the entity id
	 * @param propertyName the property holding the upload(s)
	 * @return the uploads
	 */
	public List<BinaryFile> getUploadsForProperty(ID subjectId, String propertyName);

	public T patch(T resource);
}
