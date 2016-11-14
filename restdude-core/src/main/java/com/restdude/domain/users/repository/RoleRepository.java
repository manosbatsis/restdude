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
package com.restdude.domain.users.repository;

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.users.model.Role;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@JaversSpringDataAuditable
public interface RoleRepository extends ModelRepository<Role, String> {

	@Query("select r from Role r where r.name = ?1")
	public Role findByName(String name);

	@Query("select r from Role r JOIN r.members m where m.id = ?1")
	public List<Role> findByMemberId(String userId);

	@Query("select r from Role r where (r.id = ?1 or r.name = ?1) ")
	public Role findByIdOrName(String idOrEmail);
}
