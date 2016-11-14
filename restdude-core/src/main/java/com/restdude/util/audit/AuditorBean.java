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
package com.restdude.util.audit;

import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.AuditorAware;

/**
 * @deprecated Use javers {@link org.javers.spring.annotation.JaversSpringDataAuditable} annotation to your repositories instead
 */
@Deprecated
public class AuditorBean implements AuditorAware<User> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuditorBean.class);

	private User currentAuditor;

	private UserDetailsService userDetailsService;
	

	@Autowired(required = true)
	@Qualifier("userDetailsService") // somehow required for CDI to work on 64bit JDK?
	public void setLocalUserService(
			UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public User getCurrentAuditor() {
		if(currentAuditor == null){
			ICalipsoUserDetails userDetails = SecurityUtil.getPrincipal();
			if(userDetails != null){
				currentAuditor = new User(userDetails.getId());
			}
		}
		else{
			LOGGER.debug("getCurrentAuditor returns cached result");
		}
		return currentAuditor;
	}

	public void setCurrentAuditor(User currentAuditor) {
		this.currentAuditor = currentAuditor;
	}

}