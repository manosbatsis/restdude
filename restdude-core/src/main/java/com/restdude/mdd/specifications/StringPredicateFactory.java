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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

public class StringPredicateFactory implements IPredicateFactory<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringPredicateFactory.class);

	public StringPredicateFactory() {
	}


	/**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
     */
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<String> fieldType,
			String[] propertyValues) {
		if (!fieldType.equals(String.class)) {
			LOGGER.warn("Non-String type for property '" + propertyName + "': " + fieldType.getName());
		}

		Predicate predicate = null;
		if (propertyValues.length == 1) {
			String val = propertyValues[0];
			// case insensitive like?
			if (val.contains("%")) {
				predicate = cb.like(cb.lower(root.<String> get(propertyName)), val.toLowerCase());
			} else {
				predicate = cb.equal(cb.lower(root.<String> get(propertyName)), val.toLowerCase());
			}
		} else {// if (propertyValues.length == 2) {
			List<String> wordList = Arrays.asList(propertyValues);
			predicate = cb.isTrue(root.<String> get(propertyName).in(wordList));
		}
		return predicate;
	}
}