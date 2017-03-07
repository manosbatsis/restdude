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

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * A page aware of the query params that created it and able to provide links for navigation
 */
public interface ParamsAwarePage<T> extends Page<T> {
    Map<String, String[]> getParameters();
    List<Link> buildLinks(HttpServletRequest request);
}
