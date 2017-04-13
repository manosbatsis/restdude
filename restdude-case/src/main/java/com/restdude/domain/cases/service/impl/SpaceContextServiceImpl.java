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

import com.restdude.domain.cases.model.SpaceContext;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.repository.SpaceContextRepository;
import com.restdude.domain.cases.service.SpaceContextService;
import com.restdude.domain.users.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Slf4j
@Named("spaceContextService")
public class SpaceContextServiceImpl
		extends AbstractContextServiceImpl<SpaceContext, SpaceContextRepository>
		implements SpaceContextService {

	private SpaceContext syetemSpaceContext;


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initDataOverride(User systemUser){

		// initialize globals?
		this.syetemSpaceContext = this.repository.getSystemContext();
		if(this.syetemSpaceContext == null){


			// create global SpaceContext
			this.syetemSpaceContext = this.create(
					new SpaceContext.Builder().owner(systemUser)
							.name(SpaceContextRepository.SYSTEM_CONTEXT_NAME)
							.title(SpaceContextRepository.SYSTEM_CONTEXT_TITLE)
							.description("System context")
							.visibility(ContextVisibilityType.SECRET)
							.build());

		}

	}

	@Override
	public SpaceContext getSystemContext(){
		if(this.syetemSpaceContext == null){
			this.syetemSpaceContext = this.repository.getSystemContext();
		}
		return this.syetemSpaceContext;
	}

}