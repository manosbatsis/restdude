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
package com.restdude.domain.users.repository;

import java.io.Serializable;

import com.restdude.domain.users.model.UserTotalsCount;
import com.restdude.mdd.repository.ModelRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTotalsCountRepository extends ModelRepository<UserTotalsCount, String> {

	@Modifying
	@Query("update UserTotalsCount totalsCount set totalsCount.timesLoggedAsActivitySubject = (totalsCount.timesLoggedAsActivitySubject + 1) where totalsCount.id = ?1")
	void incrementTimesLoggedAsActivitySubject(Serializable id);

	@Modifying
	@Query("update UserTotalsCount totalsCount set totalsCount.membershipsCount = (totalsCount.membershipsCount + 1) where totalsCount.id = ?1")
	void incrementMembershipsCount(Serializable id);

	@Modifying
	@Query("update UserTotalsCount totalsCount set totalsCount.membershipsCount = (totalsCount.membershipsCount - 1) where totalsCount.id = ?1")
	void decrementMembershipsCount(Serializable id);

	@Modifying
	@Query("update UserTotalsCount totalsCount set totalsCount.friendsCount = (totalsCount.friendsCount + 1) where totalsCount.id = ?1")
	void incrementFriendsCount(Serializable id);

	@Modifying
	@Query("update UserTotalsCount totalsCount set totalsCount.friendsCount = (totalsCount.friendsCount - 1) where totalsCount.id = ?1")
	void decrementFriendsCount(Serializable id);
}
