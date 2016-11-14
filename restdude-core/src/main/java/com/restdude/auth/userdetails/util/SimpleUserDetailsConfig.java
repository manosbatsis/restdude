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
import com.restdude.util.Constants;

public class SimpleUserDetailsConfig implements UserDetailsConfig {

	private String cookiesBasicAuthTokenName = Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME;
	private String cookiesDomain = null;
	private String cookiesContextPath = null;
	private boolean cookiesSecure = false;
	private boolean cookiesHttpOnly = false;

	public SimpleUserDetailsConfig() {

	}

	public SimpleUserDetailsConfig(String cookiesBasicAuthTokenName) {
		this(cookiesBasicAuthTokenName, null, null, false, false);
	}
	public SimpleUserDetailsConfig(String cookiesBasicAuthTokenName, String cookiesDomain, String cookiesContextPath) {
		this(cookiesBasicAuthTokenName, cookiesDomain, cookiesContextPath, false, false);
	}
	
	public SimpleUserDetailsConfig(String cookiesBasicAuthTokenName, String cookiesDomain, String cookiesContextPath, boolean cookiesSecure, boolean cookiesHttpOnly) {
		super();
		this.cookiesBasicAuthTokenName = cookiesBasicAuthTokenName;
		this.cookiesDomain = cookiesDomain;
		this.cookiesContextPath = cookiesContextPath;
		this.cookiesSecure = cookiesSecure;
		this.cookiesHttpOnly = cookiesHttpOnly;
	}

	public String getCookiesBasicAuthTokenName() {
		return cookiesBasicAuthTokenName;
	}

	public String getCookiesDomain() {
		return cookiesDomain;
	}

	public String getCookiesContextPath() {
		return cookiesContextPath;
	}

	public boolean isCookiesSecure() {
		return cookiesSecure;
	}

	public boolean isCookiesHttpOnly() {
		return cookiesHttpOnly;
	}


}
