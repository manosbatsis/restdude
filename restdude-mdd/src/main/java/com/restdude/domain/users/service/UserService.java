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
package com.restdude.domain.users.service;

import com.restdude.auth.userAccount.model.EmailConfirmationOrPasswordResetRequest;
import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.mdd.model.UserDetailsModel;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.model.UserModel;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserInvitationResultsDTO;
import com.restdude.domain.users.model.UserInvitationsDTO;

import java.util.Map;

public interface UserService extends PersistableModelService<User, String>{


	/**
     * Find the first user matching one of the given username or email tokena
     * @param tokens the username or email tokens to search for
     * @return the matching user, if any
     */
	User findOneByUserNameOrEmail(String... tokens);

    /**
     * Find an active user matching the given username or email
     * @param userNameOrEmail the username or email of the user
	 * @return the matching user, if any
	 */
	User findActiveByUserNameOrEmail(String userNameOrEmail);

    /**
     * Find an active user matching the given username
     *
     * @param username the username of the user
     * @return the matching user, if any
     */
	User findActiveByUsername(String username);

    /**
     * Find an active user matching the given email
     *
     * @param email the email of the user
     * @return the matching user, if any
     */
	User findActiveByEmail(String email);

    /**
	 * Update the password for the user matching the given credentials
	 * @param userNameOrEmail the username or email of the user
	 * @param oldPassword the user's current password
	 * @param newPassword the new password 
	 * @param newPasswordConfirm the confirmation for the new password
	 * @return the matching user, if any, with the persistent password already updated 
	 */
	User changePassword(String userNameOrEmail, String oldPassword, String newPassword, String newPasswordConfirm);

    /**
     * Find an active  user with the given ID
     *
     * @param userId
     * @return
     */
	User findActiveById(String userId);
	/**
	 * Find the user with the given ID
	 * @param userId
	 * @return
	 */
	User findById(String userId);

	UserModel createForImplicitSignup(User user);

	/**
	 * Get a local application user matching the given credentials, after adding
	 * and possibly persisting the given metadata for the match, if any.
     *
	 * @param userNameOrEmail
	 *            the username or email
	 * @param userPassword
	 *            the user password
	 * @param metadata
	 *            the metadata to add to the the matching user. May be
	 *            <code>null</code>.
	 * @return the local user or null if no match was found for the given
     *         credentials
     */
	User findActiveByCredentials(String userNameOrEmail, String userPassword,
								 Map<String, String> metadata);

    /**
     * Get a local application user matching the given credentials,
     *
     * @param userNameOrEmail the username or email
     * @param userPassword    the user password
     * @return the local user or null if no match was found for the given
     * credentials
     */
	User findActiveByCredentials(String userNameOrEmail, String userPassword);

	//User confirmPrincipal(String confirmationToken);

	void handlePasswordResetRequest(String userNameOrEmail);

    User handleConfirmationOrPasswordResetToken(EmailConfirmationOrPasswordResetRequest passwordResetRequest);

	/**
	 * Create pre-confirmed users. Used for testing and social signup
	 *
	 * @param resource
	 * @return
	 */
	User createAsConfirmed(User resource);

	void updateLastLogin(UserDetailsModel u);

	void expireConfirmationOrPasswordResetTokens();


	UserInvitationResultsDTO inviteUsers(UserInvitationsDTO invitations);

    /**
     * Update the username of the current user
     *
     * @param usernameChangeRequest
     * @return
     */
    User updateUsername(UsernameChangeRequest usernameChangeRequest);
}