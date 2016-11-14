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
package com.restdude.auth.userdetails.service;

import com.restdude.auth.userAccount.model.PasswordResetRequest;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.util.DuplicateEmailException;
import com.restdude.domain.users.model.User;
import org.resthub.common.service.CrudService;


public interface UserDetailsService extends CrudService<ICalipsoUserDetails, String> {

    ICalipsoUserDetails resetPassword(PasswordResetRequest resource);

	void handlePasswordResetRequest(String userNameOrEmail);

//	ICalipsoUserDetails confirmPrincipal(String confirmationToken);

	ICalipsoUserDetails createForImplicitSignup(User user)
			throws DuplicateEmailException;

	ICalipsoUserDetails getPrincipal();

	User getPrincipalLocalUser();

	void updateLastLogin(ICalipsoUserDetails u);
}