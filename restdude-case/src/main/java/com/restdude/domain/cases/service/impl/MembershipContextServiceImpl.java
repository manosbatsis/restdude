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
package com.restdude.domain.cases.service.impl;

import com.restdude.domain.cases.model.MembershipContext;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.repository.MembershipContextRepository;
import com.restdude.domain.cases.service.MembershipContextService;
import com.restdude.domain.users.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Slf4j
@Named("membershipContextService")
public class MembershipContextServiceImpl
		extends AbstractContextServiceImpl<MembershipContext, MembershipContextRepository>
		implements MembershipContextService {

	private MembershipContext syetemMembershipContext;


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initDataOverride(User systemUser){

		// initialize globals?
		this.syetemMembershipContext = this.repository.getSystemContext();
		if(this.syetemMembershipContext  == null){


			// create global MembershipContext
			this.syetemMembershipContext = this.create(
					new MembershipContext.Builder().owner(systemUser)
							.name(MembershipContextRepository.SYSTEM_CONTEXT_NAME)
							.title(MembershipContextRepository.SYSTEM_CONTEXT_TITLE)
							.description("System context")
							.visibility(ContextVisibilityType.SECRET)
							.build());

		}

	}

	@Override
	public MembershipContext getSystemContext(){
		if(this.syetemMembershipContext == null){
			this.syetemMembershipContext = this.repository.getSystemContext();
		}
		return this.syetemMembershipContext;
	}

}