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
package com.restdude.util;

import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
*/
public class CookieUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);
	// TODO: move to config
	private static final String COOKIE_NAME_SESSION = "JSESSIONID";

	/**
	 * Writes a cookie to the response. In case of a blank pathFragment the method will
	 * set the max age to zero, effectively marking the cookie for immediate 
	 * deletion by the client if the <code>allowClear</code> is true or throw an exception if false.
	 * Blank pathFragment strings mark cookie deletion. If
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param allowClear
	 */
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, boolean allowClear, UserDetailsConfig userDetailsConfig) {
		if (StringUtils.isBlank(cookieValue) && !allowClear) {
			throw new RuntimeException("Was given a blank cookie pathFragment but allowClear is false for cookie name: " + cookieName);
		}

		String server = (String) request.getAttribute(Constants.DOMAIN_KEY);
		LOGGER.debug("addCookie, cookie name: {}, value: {}, domain: {}, secure: {}, http-only: {}", cookieName, cookieValue, server, userDetailsConfig.isCookiesSecure(), userDetailsConfig.isCookiesHttpOnly());
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setPath("/");
		// set the cookie domain
		if (StringUtils.isNotBlank(server)) {
			cookie.setDomain('.' + server);
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
	}


	public static String getCookieValue(HttpServletRequest httpRequest, String cookieName) {
		String authToken = null;
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase(cookieName)) {
					LOGGER.debug("Matched cookie '{}', secure:  {}, comment: {}, domain: {}, value: {}",
							cookie.getName(), cookie.getSecure(), cookie.getComment(), cookie.getDomain(), cookie.getValue());
					authToken = cookie.getValue();
					break;
				}
				else{
					LOGGER.debug("Ignoring cookie '{}', secure:  {}, comment: {}, domain: {}, value: {}",
							cookie.getName(), cookie.getSecure(), cookie.getComment(), cookie.getDomain(), cookie.getValue());
				}
			}
			if (LOGGER.isDebugEnabled() && authToken == null) {
				LOGGER.debug("Found no calipso SSO cookie with name: ()", cookieName);

			}
		}
		return authToken;
	}

}