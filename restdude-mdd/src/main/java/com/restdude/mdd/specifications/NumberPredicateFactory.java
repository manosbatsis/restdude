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
package com.restdude.mdd.specifications;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

public class NumberPredicateFactory<T extends Number> extends AbstractPredicateFactory<T> {


    static final Logger LOGGER = LoggerFactory.getLogger(NumberPredicateFactory.class);

    private final Class<T> type;

	@SuppressWarnings("unused")
	private NumberPredicateFactory() {
		this.type = null;
	}

    public NumberPredicateFactory(Class<T> type) {
        this.type = type;
	}

    /**
     * @see com.restdude.mdd.specifications.IPredicateFactory#buildPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
     */
    @Override
    public Predicate buildPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<T> fieldType, ConversionService conversionService, String[] propertyValues) {
        Predicate predicate = null;

        LOGGER.debug("buildPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);
        Path path = this.<T>getPath(root, propertyName, fieldType);

        if (propertyValues.length == 0) {
            predicate = path.isNull();
        } else if (propertyValues.length == 1) {
            String value = propertyValues[0];
            predicate = StringUtils.isNotBlank(value) ? cb.equal(path, conversionService.convert(value, fieldType)) : path.isNull();
        } else if (propertyValues.length == 2) {
            T from = conversionService.convert(propertyValues[0], fieldType);
            T to = conversionService.convert(propertyValues[1], fieldType);
            Predicate predicate1 = cb.ge(path, from);
            Predicate predicate2 = cb.le(path, to);
            predicate = cb.and(predicate1, predicate2);
        } else if (propertyValues.length > 2) {
            Set<T> values = new HashSet<>();
            for (int i = 0; i < propertyValues.length; i++) {
                values.add(conversionService.convert(propertyValues[i], fieldType));
            }
            predicate = path.in(values);
        }
        return predicate;

	}
}