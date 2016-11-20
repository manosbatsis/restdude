package com.restdude.domain.base.controller;

import com.restdude.mdd.util.ParameterMapBackedPageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BuildPageable {
	
	public default ParameterMapBackedPageRequest buildPageable(Integer page, Integer size, String sort,
			String direction, Map<String, String[]> paramsMap) {
		Assert.isTrue(page >= 0, "Page index must be greater than, or equal to, 0");

		Sort pageableSort = null;
		if(sort != null && direction != null){
            List<Order> orders = null;
            Order order = new Order(
					direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC
							: Sort.Direction.DESC, sort);
			orders = new ArrayList<Order>(1);
			orders.add(order);
			pageableSort = new Sort(orders);
		}

        ParameterMapBackedPageRequest pageable = pageableSort != null
                ? new ParameterMapBackedPageRequest(paramsMap, page, size, pageableSort)
                : new ParameterMapBackedPageRequest(paramsMap, page, size);
        return pageable;
	}
}
