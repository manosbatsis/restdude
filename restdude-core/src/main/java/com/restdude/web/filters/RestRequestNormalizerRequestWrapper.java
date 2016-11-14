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
package com.restdude.web.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Simple {@link HttpServletRequestWrapper} wrapper that returns the supplied
 * HTTP BAsic authentication token and HTTP method for {@link HttpServletRequest#getMethod()}.
 */
class RestRequestNormalizerRequestWrapper extends
		HttpServletRequestWrapper {

	private final String method;

	private Map<String, String> customHeaderMap = new HashMap<String, String>();

	public void addHeader(String name, String value) {
		this.customHeaderMap.put(name, value);
	}

	public RestRequestNormalizerRequestWrapper(HttpServletRequest request,
			String method, String ssoToken) {
		super(request);
		
		// setup HTTP method
		if(org.apache.commons.lang3.StringUtils.isNotBlank(method)){
			this.method = method.toUpperCase(Locale.ENGLISH);
		}
		else{
			this.method = request.getMethod();
		}
		
		// setup SSO auth token if needed
		if(org.apache.commons.lang3.StringUtils.isNotBlank(ssoToken)){
			this.addHeader("Authorization", "Basic " + ssoToken);
		}			
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public String getHeader(String name) {
        String header = super.getHeader(name);
        if("accept".equalsIgnoreCase(name)){
        	header = "application/json";
        }
		if (header == null) {
			header = this.customHeaderMap.get(name);
		}
        return header; // Note: you can't use getParameterValues() here.
    }

	@Override
	public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.add("Accept");
		names.addAll(this.customHeaderMap.keySet());
        return Collections.enumeration(names);
    }
}