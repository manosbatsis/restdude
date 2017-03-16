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
package com.restdude.auth.userdetails.util;

import com.restdude.auth.model.UserDetailsAuthenticationToken;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.UserDetailsImpl;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.model.UserDetails;
import com.restdude.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;


/**
*/
public class SecurityUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);
	// TODO: move to config
	private static final String COOKIE_NAME_SESSION = "JSESSIONID";

    public static void login(HttpServletRequest request, HttpServletResponse response, User user,
                             UserDetailsConfig userDetailsConfig, UserDetailsService userDetailsService) {
        UserDetails userDetails = UserDetailsImpl.fromUser(user);
		login(request, response, userDetails, userDetailsConfig, userDetailsService);
	}

	public static void login(HttpServletRequest request, HttpServletResponse response, UserDetails userDetails,
			UserDetailsConfig userDetailsConfig, UserDetailsService userDetailsService) {

        if (userDetails != null && StringUtils.isNoneBlank(userDetails.getPk(), userDetails.getUsername(), userDetails.getPassword())) {
            String token = new String(Base64.encode((userDetails.getUsername()
					+ ":" + userDetails.getPassword()).getBytes()));
			CookieUtil.addCookie(request, response, userDetailsConfig.getCookiesBasicAuthTokenName(), token, false, userDetailsConfig);
			userDetailsService.updateLastLogin(userDetails);
			Authentication authentication = new UserDetailsAuthenticationToken(userDetails);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else{
			LOGGER.warn("Login failed, force logout to clean any stale cookies");
			SecurityUtil.logout(request, response, userDetailsConfig);
			throw new BadCredentialsException("The provided user details are incomplete");
		}
		
	}

	public static void logout(HttpServletRequest request, HttpServletResponse response, UserDetailsConfig userDetailsConfig) {
		CookieUtil.addCookie(request, response, userDetailsConfig.getCookiesBasicAuthTokenName(), null, true, userDetailsConfig);
		CookieUtil.addCookie(request, response, COOKIE_NAME_SESSION, null, true, userDetailsConfig);
		HttpSession session = request.getSession();
		if (session == null) {
			LOGGER.debug("logout, no session to clear");
		} else {
			LOGGER.debug("logout, invalidating session");
			session.invalidate();
		}
	}

	public static Authentication getAuthentication() {
		Authentication auth = null;
		if (SecurityContextHolder.getContext() != null){
			auth = SecurityContextHolder.getContext().getAuthentication();
		}
		LOGGER.debug("getAuthentication, auth: {}", auth);
		return auth;
	}

	public static Optional<UserDetails> getPrincipalOptional() {
		return Optional.ofNullable(getPrincipal()); 
	}


    public static UserDetails getPrincipal() {
		Object principal = null;
		Authentication auth = getAuthentication();
		if (auth != null) {
			principal = auth.getPrincipal();
			LOGGER.debug("getPrincipal, auth principal: {}", principal);

		}

		if (principal != null) {
			if(String.class.isAssignableFrom(principal.getClass())){
				LOGGER.warn("getPrincipal1, principal is {}, forcing anonymous: ",  principal.toString());
                principal = null;
			}
			else if (User.class.isAssignableFrom(principal.getClass())) {
				principal = UserDetailsImpl.fromUser((User) principal);
			}
		}

		return (UserDetails) principal;
	}

	public static void anonymous() {
	}
}