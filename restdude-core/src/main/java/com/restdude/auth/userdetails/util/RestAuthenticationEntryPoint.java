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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * In a standard web application, the authentication process may be
 * automatically triggered when the client tries to access a secured resource
 * without being authenticated – this is usually done by redirecting to a login
 * page so that the user can enter credentials. However, for a RESTful Web
 * Service this behavior doesn’t make much sense – authentication should only be
 * done by a request to the correct URI and all other requests should simply
 * fail with a 401 UNAUTHORIZED status code if the user is not authenticated.
 * 
 * Spring Security handles this automatic triggering of the authentication
 * process with the concept of an entry point; the entry point is a required
 * part of the configuration, and can be injected via the entry-point-ref
 * attribute of the <http> element. Keeping in mind that this functionality
 * doesn’t make sense in the context of the RESTful web service, the new custom
 * entry point is defined:
 * 
 */
@Component("restAuthenticationEntryPoint")
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print("{\"message\":\"Full authentication is required to access this resource.\", \"access-denied\":true,\"cause\":\"NOT AUTHENTICATED\"}");
        out.flush();
        out.close();
    }
}