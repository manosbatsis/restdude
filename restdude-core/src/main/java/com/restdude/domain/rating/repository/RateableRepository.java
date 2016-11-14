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
package com.restdude.domain.rating.repository;

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.rating.model.Rateable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Base repository for entities extending AbstractAuditableRateable or otherwise implementing Rateable
 * 
 * @see gr.abiss.calipso.model.entities.AbstractAuditableRateable
 * @see Rateable
 */
@NoRepositoryBean
public interface RateableRepository<T extends Rateable<?>, ID extends Serializable> extends ModelRepository<T, ID> {

	@Transactional(readOnly = false)
	@Modifying
	@Query("update #{#entityName} e set e.rating = ((e.rating * e.ratingsSize) + ?2) / (e.ratingsSize + 1), e.ratingsSize = (e.ratingsSize + 1) WHERE e.id = ?1")
	public void addRating(ID id, float rating);
	
}
