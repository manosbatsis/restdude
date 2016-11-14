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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

/**
* Adds a "display=popup" parameter so that the Facebook authorization is displayed with minimal decoration.
* @Deprecated
*/
public class FacebookConnectInterceptor implements ProviderSignInInterceptor<Facebook> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FacebookConnectInterceptor.class);

	@Override
	public void preSignIn(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
		parameters.set("display", "popup");
		LOGGER.info("topWindowDomain: " + parameters.get("topWindowDomain"));
		for (String key : parameters.keySet()) {
			LOGGER.info("param " + key + ": " + parameters.get(key));
		}
		LOGGER.info("WebRequest params...");
		for (String key : request.getParameterMap().keySet()) {

			LOGGER.info("param " + key + ": "
					+ request.getParameterMap().get(key));
		}
	}

	@Override
	public void postSignIn(Connection<Facebook> connection, WebRequest request) {
		// Nothing to do
	}

}