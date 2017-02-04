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

import com.restdude.auth.userdetails.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Detects if there is no {@code Authentication} object in the
 * {@code SecurityContextHolder}, and populates it with one if needed.
 */
public class AnonymousAuthenticationFilter extends org.springframework.security.web.authentication.AnonymousAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnonymousAuthenticationFilter.class);

    /**
     * Creates a filter with a principal named "anonymousUser" and the single authority
     * "ROLE_ANONYMOUS".
     *
     * @param key the key to identify tokens created by this filter
     */
    public AnonymousAuthenticationFilter(String key) {
        super(key, UserDetails.anonymous(), UserDetails.ROLES_ANONYMOUD);
    }

}
