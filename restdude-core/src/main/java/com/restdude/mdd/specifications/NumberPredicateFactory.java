/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.mdd.specifications;

import org.springframework.util.NumberUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NumberPredicateFactory<F extends Number> implements IPredicateFactory<F> {

	private final Class<F> type;

	@SuppressWarnings("unused")
	private NumberPredicateFactory() {
		this.type = null;
	}

	public NumberPredicateFactory(Class<F> type) {
		this.type = type;
	}

	/**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
     */
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<F> fieldType,
			String[] propertyValues) {
		Predicate predicate = null;
		if (!Number.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException(fieldType + " is not a subclass of Number for field: " + propertyName);
		}

		if (propertyValues.length == 1) {
			predicate = cb.equal(root.<F> get(propertyName), propertyValues[0]);
		} else if (propertyValues.length == 2) {
			F from = NumberUtils.parseNumber(propertyValues[0], this.type);
			F to = NumberUtils.parseNumber(propertyValues[1], this.type);
			Predicate predicate1 = cb.ge(root.<F> get(propertyName), from);
			Predicate predicate2 = cb.le(root.<F> get(propertyName), to);
			predicate = cb.and(predicate1, predicate2);
			// criteriaQuery.where(criteriaBuilder.and(predicate1,
			// predicate2));
			// predicate = cb.between(root.<T> get(propertyName), (Integer)
			// from, (Integer) to);
		}
		return predicate;
		// root...addStringSecification(personRoot, query,
		// cb, propertyName, searchTerms.get(propertyName));

	}
}