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
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * A predicates for many2one members implementing org.springframework.data.domain.Persistable
 */
public class AnyToOnePropertyPredicateFactory<F extends Serializable> implements IPredicateFactory<F> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnyToOnePropertyPredicateFactory.class);

	public AnyToOnePropertyPredicateFactory() {
	}


	/**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
     */
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<F> fieldType,String[] propertyValues) {
		if (!AbstractPersistable.class.isAssignableFrom(fieldType)) {
			LOGGER.warn("Non-Entity type for property '" + propertyName + "': " + fieldType.getName());
		}

		Predicate predicate = null;
		if (propertyValues.length > 0) {
			Path<F> parentId = root.<AbstractPersistable> get(propertyName).<F> get("id");
			predicate = cb.equal(parentId, propertyValues[0]);
		}
		return predicate;
	}
}