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

import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.dto.SpaceInvitations;
import com.restdude.domain.cases.model.dto.SpaceInvitationsResult;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.service.PersistableModelService;

import java.util.Optional;


public interface MembershipRequestService extends PersistableModelService<MembershipRequest, String> {

	public Optional<MembershipRequest> findOneByContextAndUser(BaseContext context, User user);

	/**
	 * Persist a list of BusinessContext invitations made by the BusinessContext owner.
	 * @param resource
	 * @return
	 */
	public SpaceInvitationsResult create(SpaceInvitations resource);


	public MembershipRequest createConfirmed(MembershipRequest resource);


	/**
	 * Check if an entry already exists for the given BusinessContext and User
	 */
	public Boolean exists(BaseContext context, User user);

}