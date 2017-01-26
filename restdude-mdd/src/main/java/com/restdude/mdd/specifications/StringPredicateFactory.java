/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class StringPredicateFactory extends AbstractPredicateFactory<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringPredicateFactory.class);
    private static final String WILDCARD = "%";

	public StringPredicateFactory() {
	}


    /**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
     */
    @Override
    public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<String> fieldType, ConversionService conversionService, String[] propertyValues) {
        Predicate predicate = null;

        LOGGER.debug("getPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);

        Path path = this.<Date>getPath(root, propertyName, fieldType);
        // no value i.e. match NULL
        if (propertyValues.length == 0) {
            predicate = path.isNull();
        }
        // single value, equals, like or IS NULL
        else if (propertyValues.length == 1) {
            String value = propertyValues[0];
            if (value.startsWith(WILDCARD) || value.endsWith(WILDCARD)) {
                predicate = StringUtils.isNotBlank(value) ? cb.like(path, value) : path.isNull();
            } else {
                predicate = StringUtils.isNotBlank(value) ? cb.equal(path, value) : path.isNull();
            }
        }
        // two values, region
        else if (propertyValues.length == 2) {
            predicate = cb.between(path, propertyValues[0], propertyValues[1]);
        }
        // more than two values, any of
        else {
            Set<String> values = new HashSet<>();
            for (int i = 0; i < propertyValues.length; i++) {
                String value = propertyValues[i];
                if (StringUtils.isNotBlank(value)) {
                    values.add(value);
                }
            }
            predicate = path.in(values);
        }

        return predicate;
    }
}