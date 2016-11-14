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
package com.restdude.auth.userdetails.util;

import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.domain.users.model.User;
import com.restdude.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;

import javax.servlet.http.Cookie;
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
        ICalipsoUserDetails userDetails = UserDetails.fromUser(user);
		login(request, response, userDetails, userDetailsConfig, userDetailsService);
	}

	public static void login(HttpServletRequest request, HttpServletResponse response, ICalipsoUserDetails userDetails,
			UserDetailsConfig userDetailsConfig, UserDetailsService userDetailsService) {

        if (userDetails != null && StringUtils.isNoneBlank(userDetails.getId(), userDetails.getUsername(), userDetails.getPassword())) {
            String token = new String(Base64.encode((userDetails.getUsername()
					+ ":" + userDetails.getPassword()).getBytes()));
			addCookie(request, response, userDetailsConfig.getCookiesBasicAuthTokenName(), token, false, userDetailsConfig);
			userDetailsService.updateLastLogin(userDetails);
		} else{
			LOGGER.warn("Login failed, force logout to clean any stale cookies");
			SecurityUtil.logout(request, response, userDetailsConfig);
			throw new BadCredentialsException("The provided user details are incomplete");
		}
		
	}


	public static void logout(HttpServletRequest request, HttpServletResponse response, UserDetailsConfig userDetailsConfig) {
		addCookie(request, response, userDetailsConfig.getCookiesBasicAuthTokenName(), null, true, userDetailsConfig);
		addCookie(request, response, COOKIE_NAME_SESSION, null, true, userDetailsConfig);
		HttpSession session = request.getSession();
		if (session == null) {
			LOGGER.debug("logout, no session to clear");
		} else {
			LOGGER.debug("logout, invalidating session");
			session.invalidate();
		}
	}

	/**
	 * Writes a cookie to the response. In case of a blank value the method will 
	 * set the max age to zero, effectively marking the cookie for immediate 
	 * deletion by the client if the <code>allowClear</code> is true or throw an exception if false.
	 * Blank value strings mark cookie deletion. If 
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param allowClear
	 */
	private static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, boolean allowClear, UserDetailsConfig userDetailsConfig) {
		if (StringUtils.isBlank(cookieValue) && !allowClear) {
			throw new RuntimeException("Was given a blank cookie value but allowClear is false for cookie name: " + cookieName);
		}

		String server = (String) request.getAttribute(Constants.DOMAIN_KEY);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("addCookie, cookieName: " + cookieName + 
				", cookie value: " + cookieValue+
					", domain: " + server +
				", secure: "+userDetailsConfig.isCookiesSecure() +
				", http-only: "+userDetailsConfig.isCookiesHttpOnly() +
				", path: "+userDetailsConfig.getCookiesContextPath());
		}
		Cookie cookie = new Cookie(cookieName, cookieValue);
		
		// set the cookie domain
		if (StringUtils.isNotBlank(server)) {
			cookie.setDomain('.' + server);
		}

		// set the cookie path
		if (StringUtils.isNotBlank(userDetailsConfig.getCookiesContextPath())) {
			cookie.setPath(userDetailsConfig.getCookiesContextPath());
		}
		
		cookie.setSecure(userDetailsConfig.isCookiesSecure());
		cookie.setHttpOnly(userDetailsConfig.isCookiesHttpOnly());
		
		if (StringUtils.isBlank(cookieValue)) {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("addCookie, setting max-age to 0 to clear cookie: {}", cookieName);
			}
			cookie.setMaxAge(0);
		}
		response.addCookie(cookie);
		response.addHeader("X-Calipso-Token", cookieValue);
	}

	public static Authentication getAuthentication() {
		Authentication auth = null;
		if (SecurityContextHolder.getContext() != null){
			auth = SecurityContextHolder.getContext().getAuthentication();
		}
		return auth;
	}
	

	public static Optional<ICalipsoUserDetails> getPrincipalOptional() {
		return Optional.ofNullable(getPrincipal()); 
	}
	
	
	public static ICalipsoUserDetails getPrincipal() {
		Object principal = null;
		Authentication auth = getAuthentication();
		if (auth != null) {
			principal = auth.getPrincipal();

		}

		
		if (principal != null) {
			if(String.class.isAssignableFrom(principal.getClass())){
				LOGGER.warn("getPrincipal1, principal is {}, forcing anonymous: ",  principal.toString());
				principal = null;
				
			}
			else if (User.class.isAssignableFrom(principal.getClass())) {
				principal = UserDetails.fromUser((User) principal);
			}
		}

		return (ICalipsoUserDetails) principal;
	}

}