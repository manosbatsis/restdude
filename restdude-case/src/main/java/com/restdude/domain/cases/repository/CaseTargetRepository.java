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


import java.util.Optional;

import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.CaseTarget;
import com.restdude.mdd.repository.ModelRepository;

import org.springframework.data.jpa.repository.Query;

public interface CaseTargetRepository extends ModelRepository<CaseTarget, String> {


	@Query("select n from CaseTarget n where n.path = ?1 and n.host.name =?2 and n.context = ?3")
	Optional<CaseTarget> find(String path, String domain, BaseContext spaceBlock);
}
