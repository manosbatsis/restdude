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

import com.restdude.domain.UserDetails;
import com.restdude.domain.cases.model.BusinessContext;
import com.restdude.domain.cases.repository.BusinessContextRepository;
import com.restdude.domain.cases.service.BusinessContextService;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Slf4j
@Named("businessContextService")
public class BusinessContextServiceImpl
		extends AbstractContextServiceImpl<BusinessContext, BusinessContextRepository>
		implements BusinessContextService {

	private BusinessContext syetemBusinessContext;


	/**
	 * {@inheritDoc}
	 *
	 * @param resource
	 */
	@Override
	@Transactional(readOnly = false)
	@ModelDrivenPreAuth
	public BusinessContext create(BusinessContext resource) {
		if(resource.getOwner() == null || resource.getOwner().getPk() == null){
			UserDetails ud = this.getPrincipal();
			if(StringUtils.isNotBlank(ud.getPk())){
				resource.setOwner(new User(ud.getPk()));
			}
		}
		log.debug("create, owner: {}", resource.getOwner());
		return super.create(resource);
	}
}