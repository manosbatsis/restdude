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
package com.restdude.domain.cases.repository;

import com.restdude.domain.cases.model.BaseCaseComment;
import com.restdude.mdd.repository.ModelRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

@NoRepositoryBean
public interface CaseCommentNoRepositoryBean<T extends BaseCaseComment<?,?>> extends ModelRepository<T, String> {


	@Query(value = "select count(c)+1 from  BaseCaseComment c where c.parent.id = :#{#unIndexed.parent.id}  and c.createdDate  <  :#{#unIndexed.createdDate} ")
	Integer getEntryIndex(@P("unIndexed") @Param("unIndexed") BaseCaseComment unIndexed);

}
