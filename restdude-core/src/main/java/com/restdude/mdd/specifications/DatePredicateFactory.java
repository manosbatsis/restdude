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
import java.util.Date;

public class DatePredicateFactory implements IPredicateFactory<Date> {

	public DatePredicateFactory() {
	}

	/**
	 * @see gr.abiss.calipso.uischema.jpa.search.specifications.IPredicateFactory#addPredicate(javax.persistence.criteria.Root,
	 *      javax.persistence.criteria.CriteriaBuilder, java.lang.String,
	 *      java.lang.Class, java.lang.String[])
	 */
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<Date> fieldType,
			String[] propertyValues) {
		Predicate predicate = null;
		if (!Date.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException(fieldType
					+ " is not a subclass of java.util.Date for field: " + propertyName);
		}

		if (propertyValues.length == 1) {
			Date date = new Date(NumberUtils.parseNumber(propertyValues[0], Long.class));
			predicate = cb.equal(root.<Date> get(propertyName), date);
		} else if (propertyValues.length == 2) {
			Date from = new Date(NumberUtils.parseNumber(propertyValues[0], Long.class));
			Date to = new Date(NumberUtils.parseNumber(propertyValues[1], Long.class));
			predicate = cb.between(root.<Date> get(propertyName), from, to);
		}
		return predicate;
	}
}