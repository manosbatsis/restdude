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
package com.restdude.domain.error;

import com.restdude.domain.error.model.UserAgent;
import com.restdude.util.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public class ErrorUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorUtil.class);


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