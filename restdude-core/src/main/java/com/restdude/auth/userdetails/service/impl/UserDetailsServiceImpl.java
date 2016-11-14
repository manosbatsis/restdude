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
package com.restdude.auth.userdetails.service.impl;

import com.restdude.auth.userAccount.model.PasswordResetRequest;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.DuplicateEmailException;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.util.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Named("userDetailsService")
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService,
		org.springframework.security.core.userdetails.UserDetailsService,
		org.springframework.social.security.SocialUserDetailsService,
		ConnectionSignUp, SignInAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private UserDetailsConfig userDetailsConfig = new SimpleUserDetailsConfig();

	private StringKeyGenerator keyGenerator = KeyGenerators.string();

	private UserService userService;

	@Autowired(required = false)
	public void setUserDetailsConfig(UserDetailsConfig userDetailsConfig) {
		this.userDetailsConfig = userDetailsConfig;
	}

	@Autowired(required = true)
	@Qualifier("userService") // somehow required for CDI to work on 64bit JDK?
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateLastLogin(ICalipsoUserDetails u){
		this.userService.updateLastLogin(u);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public ICalipsoUserDetails loadUserByUsername(
			String findByUsernameOrEmail) throws UsernameNotFoundException {
		ICalipsoUserDetails userDetails = null;

        User user = this.userService.findActiveByUserNameOrEmail(findByUsernameOrEmail);
		if (user == null) {
			throw new UsernameNotFoundException("Could not match username: " + findByUsernameOrEmail);
		}

		return UserDetails.fromUser(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false)
	@Override
	public ICalipsoUserDetails create(final ICalipsoUserDetails tryUserDetails) {

		ICalipsoUserDetails userDetails = null;
		if (tryUserDetails != null) {
			String usernameOrEmail = tryUserDetails.getUsername();
			if (StringUtils.isBlank(usernameOrEmail)) {
				usernameOrEmail = tryUserDetails.getEmail();
			}
			String password = tryUserDetails.getPassword();
			// TODO
			Map<String, String> metadata = tryUserDetails.getMetadata();

			// make sure we have credentials to send
			if (StringUtils.isNotBlank(usernameOrEmail)
					&& StringUtils.isNotBlank(password)) {

				// ask for the corresponding persisted user
				User user = this.userService
						.findActiveByCredentials(usernameOrEmail, password, metadata);
				if (user != null && user.getId() != null) {
					// convert to UserDetails if not null
					userDetails = UserDetails.fromUser(user);
				}

			}

		}
		return userDetails;
	}


	@Override
	@Transactional(readOnly = false)
	public void handlePasswordResetRequest(String usernameOrEmail) {
		// require user handle
		if (StringUtils.isBlank(usernameOrEmail)) {
			throw new BadRequestException("Unauthorised request must provide a username or email");
		}
		this.userService.handlePasswordResetRequest(usernameOrEmail);
	}

	@Override
	@Transactional(readOnly = false)
	public ICalipsoUserDetails resetPassword(PasswordResetRequest passwordResetRequest) {
		ICalipsoUserDetails userDetails = this.getPrincipal();
		User u = null;
		LOGGER.debug("resetPassword, userDetails: {}, currentPassword: {}", userDetails, passwordResetRequest.getCurrentPassword());
		// Case 1: if authorized as current user, require current password
		if (userDetails != null && StringUtils.isNotBlank(passwordResetRequest.getCurrentPassword())) {
			u = this.userService.changePassword(
					userDetails.getUsername(),
					passwordResetRequest.getCurrentPassword(),
					passwordResetRequest.getPassword(),
					passwordResetRequest.getPasswordConfirmation());
			//userDetails = this.create(new UserDetails( new LoginSubmission(u.getEmail(), passwordResetRequest.getPassword())));
		}
		// Case 2: if authorized using reset token
		else if (!StringUtils.isAnyBlank(passwordResetRequest.getEmailOrUsername(), passwordResetRequest.getPassword(), passwordResetRequest.getResetPasswordToken())) {
			// password and password confirmation must match
			if (!passwordResetRequest.getPassword().equals(passwordResetRequest.getPasswordConfirmation())) {
				throw new BadRequestException("Both password and password confirmation are required and must be equal");
			}
			// update matching user credentials
			u = this.userService.handlePasswordResetToken(passwordResetRequest.getEmailOrUsername(), passwordResetRequest.getResetPasswordToken(), passwordResetRequest.getPassword());
			//userDetails = this.create(new UserDetails( new LoginSubmission(u.getEmail(), passwordResetRequest.getPassword())));
		}
		// Case 3: forgotten password
		else {
			String usernameOrEmail = userDetails != null ? userDetails.getUsername() : passwordResetRequest.getEmailOrUsername();
			this.handlePasswordResetRequest(usernameOrEmail);
			userDetails = new UserDetails();
		}
		userDetails = u != null ? UserDetails.fromUser(u) : new UserDetails();
		userDetails.setPassword(passwordResetRequest.getPassword());

		return userDetails;
	}

	@Override
	public ICalipsoUserDetails getPrincipal() {
		return SecurityUtil.getPrincipal();
	}

	@Override
	public User getPrincipalLocalUser() {
		ICalipsoUserDetails principal = getPrincipal();
		User user = null;
		if (principal != null) {
			String username = principal.getUsername();
			if(StringUtils.isBlank(username)){
				username = principal.getEmail();
			}
			if(StringUtils.isNotBlank(username) && !"anonymous".equals(username)){
                user = this.userService.findActiveByUserNameOrEmail(username);
            }
		}

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("getPrincipalUser, user: " + user);
		}
		return user;
	}


	@Override
	@Transactional(readOnly = false)
	public ICalipsoUserDetails update(ICalipsoUserDetails resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(ICalipsoUserDetails resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAllWithCascade() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ICalipsoUserDetails findById(String id) {
		// LOGGER.info("findById: " + id);
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ICalipsoUserDetails> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<ICalipsoUserDetails> findAll(Pageable pageRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long count() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.springframework.social.security.SocialUserDetailsService#loadUserByUserId(java.lang.String)
	 */
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		ICalipsoUserDetails userDetails = null;

		// LOGGER.info("loadUserByUserId using: " + userId);
        User user = this.userService.findActiveById(userId);

		// LOGGER.info("loadUserByUserId user: " + user);
        if (user != null && user.getCredentials().getActive()) {
            userDetails = UserDetails.fromUser(user);
		}

		if (user == null) {
			throw new UsernameNotFoundException("Could not match user id: " + userId);
		}
		return userDetails;
	}
	
	/**
	 * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
	 */
	@Override
	@Transactional(readOnly = false)
	public String execute(Connection<?> connection) {
		
		UserProfile profile = connection.fetchUserProfile();

		String socialUsername = profile.getUsername();
		String socialName = profile.getName();
		String socialEmail = profile.getEmail();
		String socialFirstName = profile.getFirstName();
		String socialLastName = profile.getLastName();

		User user = this.getPrincipalLocalUser();
		
		if (!StringUtils.isBlank(socialEmail)) {
            user = userService.findOneByUserNameOrEmail(socialEmail);
            //

			if (user == null) {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("ConnectionSignUp#execute, Email did not match an local user, trying to create one");
				}

				user = new User();
				user.setEmail(socialEmail);
				user.setFirstName(socialFirstName);
				user.setLastName(socialLastName);
				try {
					user = userService.createForImplicitSignup(user);
					
					//username = user.getUsername();
				} catch (DuplicateEmailException e) {
					LOGGER.error("ConnectionSignUp#executeError while implicitly registering user", e);
				}

			}
		}
		else {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("ConnectionSignUp#execute, Social email was not accessible, unable to implicitly sign in user");
			}
		}
		//userService.createAccount(account);
		String result = user != null && user.getId() != null ? user.getId().toString() : null;
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("ConnectionSignUp#execute, returning result: "+result); 
		}
		return result;
	}

	/**
	 *  {@inheritDoc}
	 * @see org.springframework.social.connect.web.SignInAdapter#signIn(java.lang.String, org.springframework.social.connect.Connection, org.springframework.web.context.request.NativeWebRequest)
	 */
	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		LOGGER.info("signIn, userId: " + userId);

        User user = this.userService.findActiveById(userId);
        if(user == null){
            user = this.userService.findActiveByUserNameOrEmail(userId);
        }
		//if(LOGGER.isDebugEnabled()){
			LOGGER.info("SignInAdapter#signIn userId: " + userId + ", connection: " + connection.getKey() + ", mached user: " + user);
		//}
		if(user != null){
			SecurityUtil.login((HttpServletRequest) request.getNativeRequest(), (HttpServletResponse) request.getNativeResponse(), user, this.userDetailsConfig, this);
		}
		return null;
	}

	@Override
	public ICalipsoUserDetails createForImplicitSignup(
			User user) throws DuplicateEmailException {
		LOGGER.info("createForImplicitSignup, user: " + user);
		ICalipsoUserDetails userDetails = UserDetails
				.fromUser(this.userService.createForImplicitSignup(user));
		return userDetails;
	}

	@Override
	public Iterable<ICalipsoUserDetails> findByIds(Set<String> ids) {
		throw new UnsupportedOperationException();
	}

}