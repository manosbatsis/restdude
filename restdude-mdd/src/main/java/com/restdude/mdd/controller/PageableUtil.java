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
package com.restdude.mdd.controller;

import com.restdude.mdd.util.ParameterMapBackedPageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageableUtil {

    public static ParameterMapBackedPageRequest buildPageable(Integer page, Integer size, String sort,
                                                              String direction, Map<String, String[]> paramsMap) {
        Assert.isTrue(page >= 0, "Page index must be greater than, or equal to, 0");

        Sort pageableSort = buildSort(sort, direction);

        ParameterMapBackedPageRequest pageable = pageableSort != null
                ? new ParameterMapBackedPageRequest(paramsMap, page, size, pageableSort)
                : new ParameterMapBackedPageRequest(paramsMap, page, size);
        return pageable;
    }

    public static Sort buildSort(String sort, String direction) {
        Sort pageableSort = null;
        if (sort != null && direction != null) {
            List<Order> orders = null;
            Order order = new Order(
                    direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC
                            : Sort.Direction.DESC, sort);
            orders = new ArrayList<Order>(1);
            orders.add(order);
            pageableSort = new Sort(orders);
        }
        return pageableSort;
    }
}
