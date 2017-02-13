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
package com.restdude.auth.userdetails.service.impl;

import com.restdude.auth.userAccount.model.EmailConfirmationOrPasswordResetRequest;
import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.mdd.model.UserDetailsModel;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.domain.details.contact.model.ContactDetails;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.util.exception.http.BadRequestException;
import com.restdude.util.exception.http.InvalidCredentialsException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

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
    public void updateLastLogin(UserDetailsModel u){
        this.userService.updateLastLogin(u);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetailsModel loadUserByUsername(
            String findByUsernameOrEmail) throws UsernameNotFoundException {

        LOGGER.debug("#loadUserByUsername, findByUsernameOrEmail: {}", findByUsernameOrEmail);
        UserDetailsModel userDetails = null;

        User user = this.userService.findActiveByUserNameOrEmail(findByUsernameOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("Could not match username: " + findByUsernameOrEmail);
        }
        LOGGER.debug("#loadUserByUsername, user: {}", user);
        return UserDetails.fromUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    @Override
    public UserDetailsModel create(final UserDetailsModel tryUserDetails) {
        LOGGER.debug("create, userDetails: {}", tryUserDetails);
        UserDetailsModel userDetails = null;
        if (tryUserDetails != null) {
            String usernameOrEmail = tryUserDetails.getUsername();
            /*if (StringUtils.isBlank(usernameOrEmail)) {
                usernameOrEmail = tryUserDetails.getEmail();
			}*/
            String password = tryUserDetails.getPassword();
            // TODO
            Map<String, String> metadata = tryUserDetails.getMetadata();

            LOGGER.debug("create, usernameOrEmail: {}, pw: {}", usernameOrEmail, password);
            // make sure we have credentials to send
            if (StringUtils.isNotBlank(usernameOrEmail)
                    && StringUtils.isNotBlank(password)) {

                // ask for the corresponding persisted user
                User user = this.userService
                        .findActiveByCredentials(usernameOrEmail, password, metadata);
                if (user != null && user.getPk() != null) {

                    LOGGER.info("#create, user: {}", user);
                    // convert to UserDetails if not null
                    userDetails = UserDetails.fromUser(user);
                } else {
                    throw new InvalidCredentialsException();
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
    public UserDetailsModel updateUsername(UsernameChangeRequest usernameChangeRequest) {
        String pw = usernameChangeRequest.getPassword();
        User user = this.userService.updateUsername(usernameChangeRequest);
        UserDetailsModel userDetails = UserDetails.fromUser(user);
        userDetails.setPassword(pw);
        return userDetails;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDetailsModel resetPassword(EmailConfirmationOrPasswordResetRequest passwordResetRequest) {
        UserDetailsModel userDetails = this.getPrincipal();
        User u = null;
        // Case 1: if authorized as current user and in an attempt to directly change password, require current password
        if (userDetails != null && userDetails.getPk() != null && StringUtils.isNoneBlank(passwordResetRequest.getPassword(), passwordResetRequest.getPasswordConfirmation())) {
            u = this.userService.changePassword(
                    userDetails.getUsername(),
                    passwordResetRequest.getCurrentPassword(),
                    passwordResetRequest.getPassword(),
                    passwordResetRequest.getPasswordConfirmation());
        }
        // Case 2: if using reset token
        else if (StringUtils.isNotBlank(passwordResetRequest.getResetPasswordToken())) {
            // password and password confirmation must match
            if (passwordResetRequest.getPassword() != null && (StringUtils.isBlank(passwordResetRequest.getPassword()) || !passwordResetRequest.getPassword().equals(passwordResetRequest.getPasswordConfirmation()))) {
                throw new BadRequestException("A password, when given, must be non-blank and equal to the password confirmation");
            }
            // update matching user credentials
            u = this.userService.handleConfirmationOrPasswordResetToken(passwordResetRequest);
            //userDetails = this.create(new UserDetails( new LoginSubmission(u.getEmail(), passwordResetRequest.getPassword())));
        }
        // Case 3: forgotten password
        else {
            String usernameOrEmail = userDetails != null ? userDetails.getUsername() : passwordResetRequest.getEmailOrUsername();
            this.handlePasswordResetRequest(usernameOrEmail);
            userDetails = new UserDetails();
        }
        userDetails = u != null ? UserDetails.fromUser(u) : new UserDetails();
        userDetails.setPassword(u.getCredentials().getPassword());

        return userDetails;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityUtil.getAuthentication();
    }

    @Override
    public UserDetailsModel getPrincipal() {
        return SecurityUtil.getPrincipal();
    }

    @Override
    public User getPrincipalLocalUser() {
        UserDetailsModel principal = getPrincipal();
        User user = null;
        if (principal != null) {
            String username = principal.getUsername();
            /*
            if(StringUtils.isBlank(username)){
				username = principal.getEmail();
			}
			if(StringUtils.isNotBlank(username) && !"anonymous".equals(username)){
                user = this.userService.findActiveByUserNameOrEmail(username);
            }
            */
        }

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("#getPrincipalUser, user: {}", user);
        }
        return user;
    }



    /**
     * @see org.springframework.social.security.SocialUserDetailsService#loadUserByUserId(java.lang.String)
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        UserDetailsModel userDetails = null;

        LOGGER.info("#loadUserByUserId using: {}", userId);
        User user = this.userService.findActiveById(userId);

        LOGGER.info("#loadUserByUserId user: {}", user);
        if (user != null && user.getCredentials().getActive()) {
            userDetails = UserDetails.fromUser(user);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Could not match user pk: " + userId);
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

        if (user == null) {
            if (!StringUtils.isBlank(socialEmail)) {
                user = userService.findOneByUserNameOrEmail(socialEmail);
                //

                if (user == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("ConnectionSignUp#execute, Email did not match an local user, trying to create one");
                    }

                    user = new User();
                    EmailDetail email = new EmailDetail();
                    email.setEmail(socialEmail);
                    user.setContactDetails(new ContactDetails.Builder().primaryEmail(email).build());
                    user.setFirstName(socialFirstName);
                    user.setLastName(socialLastName);
                    try {
                        user = (User) userService.createForImplicitSignup(user);

                        //username = user.getUsername();
                    } catch (Exception e) {
                        LOGGER.error("ConnectionSignUp#executeError while implicitly registering user", e);
                    }

                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("ConnectionSignUp#execute, Social email was not accessible, unable to implicitly sign in user");
                }
            }
        }
        //userService.createAccount(account);
        String result = user != null && user.getPk() != null ? user.getPk().toString() : null;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ConnectionSignUp#execute, returning result: {}", result);
        }
        return result;
    }

    /**
     *  {@inheritDoc}
     * @see org.springframework.social.connect.web.SignInAdapter#signIn(java.lang.String, org.springframework.social.connect.Connection, org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        LOGGER.info("#signIn, userId: {}", userId);

        User user = this.userService.findActiveById(userId);
        if(user == null){
            user = this.userService.findActiveByUserNameOrEmail(userId);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("#signIn userId: " + userId + ", connection: " + connection.getKey() + ", mached user: {}", user);
        }
        if(user != null){
            SecurityUtil.login((HttpServletRequest) request.getNativeRequest(), (HttpServletResponse) request.getNativeResponse(), user, this.userDetailsConfig, this);
        }
        return null;
    }

    @Override
    public UserDetailsModel createForImplicitSignup(
            User user) {
        LOGGER.info("#createForImplicitSignup, user: {}", user);
        UserDetailsModel userDetails = UserDetails
                .fromUser((User) this.userService.createForImplicitSignup(user));
        return userDetails;
    }


}