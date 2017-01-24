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

import com.restdude.domain.geography.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

public class EnumStringPredicateFactory extends AbstractPredicateFactory<Enum> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnumStringPredicateFactory.class);

	private Class<Enum> type;

	private EnumStringPredicateFactory() {
	}

	public EnumStringPredicateFactory(Class<Enum> clazz) {
		this.type = clazz;
	}


	@Override
	public Predicate getPredicate(Root<Country> root, CriteriaBuilder cb, String propertyName, Class<Enum> fieldType, String[] propertyValues) {
		Predicate predicate = null;
		try {
			LOGGER.debug("getPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);
			Path path = this.<Enum>getPath(root, propertyName, fieldType);

			if (propertyValues.length == 1) {
				predicate = cb.equal(path, Enum.valueOf(this.type, propertyValues[0]));
			} else {
				Set choices = new HashSet(propertyValues.length);
				for (int i = 0; i < propertyValues.length; i++) {
					choices.add(Enum.valueOf(this.type, propertyValues[i]));
				}

				predicate = path.in(choices);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return predicate;
	}
}