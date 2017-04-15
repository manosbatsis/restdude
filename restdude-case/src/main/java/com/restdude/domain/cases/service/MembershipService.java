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
package com.restdude.domain.cases.service;

import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.service.PersistableModelService;

import java.util.Set;

public interface MembershipService extends PersistableModelService<Membership, String> {


	Set<String> findOnlineMemberUsernames(Space context);

	/**
	 * Get the membership of the current user for the given BusinessContext if any exists, 
	 * else implicitly accept any pending invitation sent to the 
	 * user for the given BusinessContext or create a join request otherwise.
	 */
	Membership getMembership(Space context);

	/**
	 * Get the membership of the given user for the given BusinessContext if any exists, 
	 * else implicitly accept any pending invitation sent to the 
	 * user for the given BusinessContext or create a join request otherwise.
	 */
	Membership getMembership(Space context, User user);

	/**
	 * Delete any membership of the given user for the given Space, if any exists
	 */
	void delete(Space businessContext, User user);

	Membership createTest(Membership resource);
	
}