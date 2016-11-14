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

import com.restdude.domain.base.model.CalipsoPersistable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * A predicates for members that are Many2one/OneToOne or members
 * annotated with {@link javax.persistence.Embedded} or {@link javax.persistence.EmbeddedId}
 */
public class AnyToOnePredicateFactory<F extends Serializable> implements IPredicateFactory<F> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnyToOnePredicateFactory.class);

	public AnyToOnePredicateFactory() {
	}


	/**
	 * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
	 */
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<F> fieldType, String[] propertyValues) {

		Predicate predicate = null;
		try {
			LOGGER.info("getPredicate, propertyName: {}, nroot: {}", propertyName, root);
			if (!CalipsoPersistable.class.isAssignableFrom(fieldType)) {
				LOGGER.warn("Non-Entity type for property '" + propertyName + "': " + fieldType.getName());
			}

			if (propertyValues.length > 0) {
				Path<F> parentId = root.<F> get(propertyName).<F> get("id");
				predicate = cb.equal(parentId, propertyValues[0]);
			} else {

				String[] pathSteps = propertyName.split("\\.");
				Path<F> basePath = root.<F> get(pathSteps[0]);
				Path targetPath;
				String step;
				for (int i = 1; i < pathSteps.length - 2; i++) {
					LOGGER.info("getPredicate, prepare to add path step: {}, new basePath:: {}, root:" + 
							root, pathSteps[i],	basePath);
					step = pathSteps[i];
					Field subField = fieldType.getField(step);
					LOGGER.info("getPredicate,subField: {}", subField);
					targetPath = basePath.<Persistable> get(step);//.as(subField.getType());
				}
				Path<Serializable> targetProperty = basePath.<Serializable> get(pathSteps[pathSteps.length - 1]);
				predicate = cb.equal(targetProperty, propertyValues[0]);
			}
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return predicate;
	}
}