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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BooleanPredicateFactory extends AbstractPredicateFactory<Boolean> {


	private static final Logger LOGGER = LoggerFactory.getLogger(BooleanPredicateFactory.class);

	public BooleanPredicateFactory() {
	}

	/**
	 * @see com.restdude.mdd.specifications.IPredicateFactory#buildPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
	 */
    @Override
	public Predicate buildPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<Boolean> fieldType, ConversionService conversionService, String[] propertyValues) {
		Predicate predicate = null;

		try {
			LOGGER.debug("buildPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);

			Path path = this.<Boolean>getPath(root, propertyName, fieldType);
			if (propertyValues.length == 1) {
                Boolean b = conversionService.convert(propertyValues[0], Boolean.class);
                predicate = b != null ? cb.equal(path, propertyValues[0]) : path.isNull();
			} else if (propertyValues.length > 1) {
				List<Boolean> values = new LinkedList<>();
				for (int i = 0; i < propertyValues.length; i++) {
                    Boolean b = conversionService.convert(propertyValues[i], Boolean.class);
                    if (!values.contains(b)) {
						values.add(b);
					}
				}
				predicate = path.in(Arrays.asList(propertyValues));
			} else {
				predicate = path.isNull();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return predicate;
	}
}