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

import com.restdude.domain.cases.model.MembershipContext;
import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.repository.ModelRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MembershipRequestRepository extends ModelRepository<MembershipRequest,String> {

    @Query("select req from MembershipRequest req where req.context.pk = :#{#context.pk} AND req.user.pk = :#{#user.pk}")
    Optional<MembershipRequest> findOneByContextAndUser(@Param("context") MembershipContext context, @Param("user") User user);

    @Query("select case when (count(req) > 0)  then true else false end from MembershipRequest req where req.context.pk = :#{#context.pk} AND req.user.pk = :#{#user.pk}")
    Boolean exists(@Param("context") MembershipContext context, @Param("user") User user);
}
