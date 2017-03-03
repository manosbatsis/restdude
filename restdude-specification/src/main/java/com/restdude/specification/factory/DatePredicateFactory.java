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
package com.restdude.specification.factory;

import com.restdude.specification.IPredicateFactory;
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

public class DatePredicateFactory extends AbstractPredicateFactory<Date> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatePredicateFactory.class);

    public DatePredicateFactory() {
    }

    /**
     * @see IPredicateFactory#buildPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
     */
    @Override
    public Predicate buildPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<Date> fieldType, ConversionService conversionService, String[] propertyValues) {
        Predicate predicate = null;

        try {
            LOGGER.debug("buildPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);

            Path path = this.<Date>getPath(root, propertyName, fieldType);
            if (propertyValues.length == 0) {
                predicate = path.isNull();
            }
            if (propertyValues.length == 1) {
                Date date = conversionService.convert(propertyValues[0], Date.class);
                predicate = date != null ? cb.equal(path, date) : path.isNull();
            } else if (propertyValues.length == 2) {
                Date from = conversionService.convert(propertyValues[0], Date.class);
                Date to = conversionService.convert(propertyValues[1], Date.class);
                predicate = cb.between(path, from, to);
            } else {
                Set<Date> values = new HashSet<>();
                for (int i = 0; i < propertyValues.length; i++) {
                    Date d = conversionService.convert(propertyValues[i], Date.class);
                    values.add(d);
                }
                predicate = path.in(values);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return predicate;
    }
}