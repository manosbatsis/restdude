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

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.geography.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A predicates for members that are Many2one/OneToOne or members
 * annotated with {@link javax.persistence.Embedded} or {@link javax.persistence.EmbeddedId}
 */
public class AnyToOnePredicateFactory<T extends CalipsoPersistable<ID>, ID extends Serializable> extends AbstractPredicateFactory<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnyToOnePredicateFactory.class);

	public AnyToOnePredicateFactory() {
	}


	/**
	 * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
	 */
	@Override
	public Predicate getPredicate(Root<Country> root, CriteriaBuilder cb, String propertyName, Class<T> fieldType, String[] propertyValues) {

		Predicate predicate = null;
		try {
			LOGGER.debug("getPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);
			if (!CalipsoPersistable.class.isAssignableFrom(fieldType)) {
				LOGGER.warn("Non-Entity type for property '" + propertyName + "': " + fieldType.getName());
			}

			Path<T> relatedPath = this.<T>getPath(root, propertyName, fieldType);

			if (propertyValues.length == 1) {
				Serializable pId = propertyValues[0];
				predicate = pId != null ? cb.equal(relatedPath.<ID>get("pk"), pId) : relatedPath.isNull();
			} else if (propertyValues.length > 1) {
				Expression<ID> exp = relatedPath.get("pk");
				predicate = exp.in(Arrays.asList(propertyValues));
			} else {
				predicate = relatedPath.isNull();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return predicate;
	}
}