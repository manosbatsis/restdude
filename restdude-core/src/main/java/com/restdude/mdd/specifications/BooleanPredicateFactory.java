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

import org.apache.commons.lang3.BooleanUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BooleanPredicateFactory implements IPredicateFactory<Boolean> {

	public BooleanPredicateFactory() {
	}

	/**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
     */
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<Boolean> fieldType, 	String[] propertyValues) {
		Predicate predicate = null;
		if (!Boolean.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException(fieldType
					+ " is not a subclass of Boolean for field: "
					+ propertyName);
		}

		Boolean b = BooleanUtils.toBooleanObject(propertyValues[0]);
		if (b == null) {
			b = Boolean.FALSE;
		}

		predicate = cb.equal(root.<Boolean> get(propertyName), b);
		return predicate;
	}
}