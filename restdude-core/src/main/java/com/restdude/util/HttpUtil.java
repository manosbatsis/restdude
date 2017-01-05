/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.util;

import com.restdude.domain.error.model.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static String setBaseUrl(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        String baseUrl = (String) request.getAttribute(Constants.BASE_URL_KEY);
        if (StringUtils.isBlank(baseUrl)) {
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String ctx = request.getContextPath();
            baseUrl = url.substring(0, url.length() - uri.length() + ctx.length());
            String scheme = request.getHeader("X-Forwarded-Proto");
            if (StringUtils.isNotBlank(scheme) && scheme.equalsIgnoreCase("HTTPS") && baseUrl.startsWith("http:")) {
                baseUrl = baseUrl.replaceFirst("http:", "https:");
            }
            request.setAttribute(Constants.BASE_URL_KEY, baseUrl);
            LOGGER.debug("Added request attribute '{}': {}", Constants.BASE_URL_KEY, baseUrl);
            request.setAttribute(Constants.DOMAIN_KEY, request.getServerName());
        }
        return baseUrl;
    }

    public static UserAgent getUserAgent(HttpServletRequest request) {

        UserAgent ua = null;
        String value = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(value)) {
            ua = new UserAgent(HashUtils.buildHash(value), value);
        }
        return ua;
    }

    public static String getRemoteAddress(HttpServletRequest request) {
        String addresss = request.getHeader("X-FORWARDED-FOR");
        if (addresss == null) {
            addresss = request.getRemoteAddr();
        }
        return addresss;
    }

}
