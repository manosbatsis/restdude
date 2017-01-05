/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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

import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.util.Constants;

public class SimpleUserDetailsConfig implements UserDetailsConfig {

	private String cookiesBasicAuthTokenName = Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME;
	private boolean cookiesSecure = false;
	private boolean cookiesHttpOnly = false;

	public SimpleUserDetailsConfig() {

	}

	public SimpleUserDetailsConfig(String cookiesBasicAuthTokenName) {
		this(cookiesBasicAuthTokenName, false, false);
	}
	public SimpleUserDetailsConfig(String cookiesBasicAuthTokenName, String cookiesDomain, String cookiesContextPath) {
		this(cookiesBasicAuthTokenName, false, false);
	}

	public SimpleUserDetailsConfig(String cookiesBasicAuthTokenName, boolean cookiesSecure, boolean cookiesHttpOnly) {
		super();
		this.cookiesBasicAuthTokenName = cookiesBasicAuthTokenName;
		this.cookiesSecure = cookiesSecure;
		this.cookiesHttpOnly = cookiesHttpOnly;
	}

	public String getCookiesBasicAuthTokenName() {
		return cookiesBasicAuthTokenName;
	}

	public boolean isCookiesSecure() {
		return cookiesSecure;
	}

	public boolean isCookiesHttpOnly() {
		return cookiesHttpOnly;
	}


}
