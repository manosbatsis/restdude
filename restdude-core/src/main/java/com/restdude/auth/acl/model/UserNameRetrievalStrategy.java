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
package com.restdude.auth.acl.model;

import com.restdude.auth.userdetails.model.UserDetails;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

/**
 * overwrite the strategy: build ObjectIdentity based on user object login
 * property, instead of Spring Security default getId() call
 */
public class UserNameRetrievalStrategy implements
		ObjectIdentityRetrievalStrategy {

	@Override
	public ObjectIdentity getObjectIdentity(Object domainObject) {
		UserDetails user = (UserDetails) domainObject;
		return new ObjectIdentityImpl(UserDetails.class, user.getUsername());
	}

}