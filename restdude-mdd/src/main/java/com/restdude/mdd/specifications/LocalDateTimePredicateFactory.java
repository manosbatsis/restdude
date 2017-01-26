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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class LocalDateTimePredicateFactory extends AbstractPredicateFactory<LocalDateTime> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateTimePredicateFactory.class);

    public LocalDateTimePredicateFactory() {
    }

    /**
     * @see IPredicateFactory#buildPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
     */
    @Override
    public Predicate buildPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<LocalDateTime> fieldType, ConversionService conversionService, String[] propertyValues) {
        Predicate predicate = null;

        try {
            LOGGER.debug("buildPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);

            Path path = this.<LocalDateTime>getPath(root, propertyName, fieldType);
            if (propertyValues.length == 0) {
                predicate = path.isNull();
            }
            if (propertyValues.length == 1) {
                LocalDateTime date = conversionService.convert(propertyValues[0], LocalDateTime.class);
                predicate = date != null ? cb.equal(path, date) : path.isNull();
            } else if (propertyValues.length == 2) {
                LocalDateTime from = conversionService.convert(propertyValues[0], LocalDateTime.class);
                LocalDateTime to = conversionService.convert(propertyValues[1], LocalDateTime.class);
                predicate = cb.between(path, from, to);
            } else {
                Set<LocalDateTime> values = new HashSet<>();
                for (int i = 0; i < propertyValues.length; i++) {
                    LocalDateTime d = conversionService.convert(propertyValues[i], LocalDateTime.class);
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