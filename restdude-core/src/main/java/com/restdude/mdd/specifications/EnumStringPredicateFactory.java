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
import java.util.ArrayList;

public class EnumStringPredicateFactory implements IPredicateFactory<Enum> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnumStringPredicateFactory.class);

	private Class type;

	private EnumStringPredicateFactory() {
	}

	public EnumStringPredicateFactory(Class clazz) {
		this.type = clazz;
	}


	/**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
     */
	@SuppressWarnings("unchecked")
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<Enum> fieldType,
			String[] propertyValues) {
		if (!fieldType.isEnum()) {
			LOGGER.warn("Non-Enum type for property '" + propertyName + "': " + fieldType.getName() + ", class: " + fieldType.getClass());
		}

		Predicate predicate;
		
		if(propertyValues.length == 1){
			predicate = cb.equal(root.<Enum> get(propertyName), Enum.valueOf((Class<Enum>) this.type, propertyValues[0]));
		}
		else{
			ArrayList<Enum> choices = new ArrayList<Enum>(propertyValues.length);
			for (int i = 0; i < propertyValues.length; i++) {
				try{
					choices.add(Enum.valueOf((Class<Enum>) this.type, propertyValues[i]));
				}catch(Exception e){
					LOGGER.warn(
							"Invalid Enum entry '" + propertyValues[i] + "' for property '" + propertyName + "' and class "
									+ fieldType.getName(), e);
				}
			}
			
			predicate = cb.isTrue(root.<Enum> get(propertyName).in(choices));
		}
		return predicate;
	}
}