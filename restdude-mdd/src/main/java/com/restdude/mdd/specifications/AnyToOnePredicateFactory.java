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

import com.restdude.mdd.model.PersistableModel;
import com.restdude.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A predicates for members that are Many2one/OneToOne or members
 * annotated with {@link javax.persistence.Embedded} or {@link javax.persistence.EmbeddedId}
 */
public class AnyToOnePredicateFactory<T extends PersistableModel<PK>, PK extends Serializable> extends AbstractPredicateFactory<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnyToOnePredicateFactory.class);
	private Class<PK> idType;

	public AnyToOnePredicateFactory() {
	}


	/**
	 * @see com.restdude.mdd.specifications.IPredicateFactory#buildPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
	 */
	@Override
	public Predicate buildPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<T> fieldType, ConversionService conversionService, String[] propertyValues) {

		Predicate predicate = null;
		try {
			LOGGER.debug("buildPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);
			if (!PersistableModel.class.isAssignableFrom(fieldType)) {
				LOGGER.warn("Non-Entity type for property '" + propertyName + "': " + fieldType.getName());
			}
            if (this.idType == null) {
				this.idType = (Class<PK>) ClassUtils.getBeanPropertyType(fieldType, "pk", false);
			}
            Path<T> relatedPath = this.<T>getPath(root, propertyName, fieldType);
			if (propertyValues.length == 1) {
				PK pId = conversionService.convert(propertyValues[0], idType);
				predicate = pId != null ? cb.equal(relatedPath.<PK>get("pk"), pId) : relatedPath.isNull();
			} else if (propertyValues.length > 1) {
				Expression<PK> exp = relatedPath.get("pk");
				ArrayList<PK> pks = new ArrayList<>(propertyValues.length);
				for (String val : propertyValues) {
                    pks.add(conversionService.convert(val, idType));
                }
                predicate = exp.in(pks);
            } else {
				predicate = relatedPath.isNull();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return predicate;
	}
}