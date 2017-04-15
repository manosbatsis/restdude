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

import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.repository.SpaceRepository;
import com.restdude.domain.cases.service.SpaceService;
import com.restdude.domain.users.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Slf4j
@Named("spaceService")
public class SpaceServiceImpl
		extends AbstractContextServiceImpl<Space, SpaceRepository>
		implements SpaceService {

	private Space syetemSpace;


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initDataOverride(User systemUser){

		// initialize globals?
		this.syetemSpace = this.repository.getSystemContext();
		if(this.syetemSpace == null){


			// create global Space
			this.syetemSpace = this.create(
					new Space.Builder().owner(systemUser)
							.name(SpaceRepository.SYSTEM_CONTEXT_NAME)
							.title(SpaceRepository.SYSTEM_CONTEXT_TITLE)
							.description("The global system context")
							.visibility(ContextVisibilityType.SECRET)
							.build());

		}

	}

	@Override
	public Space getSystemContext(){
		if(this.syetemSpace == null){
			this.syetemSpace = this.repository.getSystemContext();
		}
		return this.syetemSpace;
	}

}