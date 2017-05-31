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

import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.repository.ModelRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

/**
 * Calipso JPA repository for the Membership entity.
 * {@inheritDoc}
 */
public interface MembershipRepository extends ModelRepository<Membership, String> {

    @Query("select membership from Membership membership where membership.context = ?1 AND membership.user = ?2")
    Optional<Membership> findOneByBusinessContextAndUser(@Param("context") Space context, @Param("user") User user);

    @Modifying
    @Query("delete from Membership membership where membership.context = ?1 AND membership.user = ?2")
    void deleteByBusinessContextAndUser(@Param("context") BaseContext context, @Param("user") User user);

    @Query("select membership.user.username from Membership membership where membership.context = ?1 AND membership.user.stompSessionCount > 0 ")
    Set<String> findOnlineMemberUsernames(Space context);

    @Query("select case when (count(contextMembership) > 0)  then true else false end from Membership contextMembership where contextMembership.context.id = :#{#context.id} AND contextMembership.user.id = :#{#user.id}")
    Boolean exists(@Param("context") Space context, @Param("user") User user);
}
