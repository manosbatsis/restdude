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
import com.restdude.mdd.annotation.CurrentPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A generic specifications class that builds predicates for any implementation
 * of org.springframework.data.domain.Persistable
 */
public class GenericSpecifications<T extends CalipsoPersistable<ID>, ID extends Serializable> {


	static final Logger LOGGER = LoggerFactory.getLogger(GenericSpecifications.class);

	private static final HashMap<String, Field> FIELD_CACHE = new HashMap<String, Field>();
	
	protected static final HashMap<Class, List<Field>> SIMPLE_SEARCH_FIELDs_CACHE = new HashMap<Class, List<Field>>();
	protected static final String SIMPLE_SEARCH_PARAM_NAME = "_all";
	
	protected static final String SEARCH_MODE = "_searchmode";

	protected static final StringPredicateFactory stringPredicateFactory = new StringPredicateFactory();
	protected static final BooleanPredicateFactory booleanPredicateFactory = new BooleanPredicateFactory();
	protected static final DatePredicateFactory datePredicateFactory = new DatePredicateFactory();

	protected static final NumberPredicateFactory<Short> shortPredicateFactory = new NumberPredicateFactory<Short>(Short.class);
	protected static final NumberPredicateFactory<Integer> integerPredicateFactory = new NumberPredicateFactory<Integer>(Integer.class);
	protected static final NumberPredicateFactory<Long> longPredicateFactory = new NumberPredicateFactory<Long>(Long.class);
	protected static final NumberPredicateFactory<Double> doublePredicateFactory = new NumberPredicateFactory<Double>(Double.class);

	protected static final AnyToOnePredicateFactory anyToOnePredicateFactory = new AnyToOnePredicateFactory();
	protected static final AnyToOneToOnePropertyPredicateFactory anyToOneToOnePredicateFactory = new AnyToOneToOnePropertyPredicateFactory();
	protected static final CurrentPrincipalPredicateFactory currentPrincipalPredicateFactory = new CurrentPrincipalPredicateFactory();
	
	protected static final HashMap<Class,IPredicateFactory> factoryForClassMap = new HashMap<Class, IPredicateFactory>();

	protected static final String OR = "OR";
	protected static final String AND = "AND";
	protected static List<String> IGNORED_FIELD_NAMES;

	static {
		factoryForClassMap.put(String.class, stringPredicateFactory);
		factoryForClassMap.put(Boolean.class, booleanPredicateFactory);
		factoryForClassMap.put(Date.class, datePredicateFactory);

		factoryForClassMap.put(Short.class, shortPredicateFactory);
		factoryForClassMap.put(Integer.class, integerPredicateFactory);
		factoryForClassMap.put(Long.class, longPredicateFactory);
		factoryForClassMap.put(Double.class, doublePredicateFactory);

		// init ignore list
		// TODO: pick model-specific exclides from anotation
		String[] ignoredFieldNames = { "page", "direction", "properties", "size", "totalPages", "_searchmode", "_all", "totalElements" };
		IGNORED_FIELD_NAMES = new ArrayList<String>(ignoredFieldNames.length);
		for (int i = 0; i < ignoredFieldNames.length; i++) {
			IGNORED_FIELD_NAMES.add(ignoredFieldNames[i]);
		}
	}
	


	/**
	 * Get an appropriate predicate factory for the given field class
	 * @param field
	 * @return
	 */
	
	public static IPredicateFactory<?> getPredicateFactoryForClass(Field field) {
		Class clazz = field.getType();
		if (clazz.isEnum()) {
			return new EnumStringPredicateFactory(clazz);
		} 
		else if (field.isAnnotationPresent(CurrentPrincipal.class)) {
			return currentPrincipalPredicateFactory;
		} else if (CalipsoPersistable.class.isAssignableFrom(clazz) 
				|| field.isAnnotationPresent(EmbeddedId.class) 
				|| field.isAnnotationPresent(Embedded.class)) {
			 
				return anyToOnePredicateFactory;
			
		}
		else {
			return factoryForClassMap.get(clazz);
		}
	}

	/**
	 * Get a (cached) field for the given class' member name
	 * @param clazz
	 * @param fieldName the member name
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		Field field = null;
		if (!IGNORED_FIELD_NAMES.contains(fieldName)) {

			String key = clazz.getName() + "#" + fieldName;
			field = FIELD_CACHE.get(key);

			// find it if not cached
			if (field == null && !FIELD_CACHE.containsKey(key)) {
				Class<?> tmpClass = clazz;
				do {
					for (Field tmpField : tmpClass.getDeclaredFields()) {
						String candidateName = tmpField.getName();
						if (candidateName.equals(fieldName)) {
							// field.setAccessible(true);
							FIELD_CACHE.put(key, tmpField);
							field = tmpField;
							break;
						}
					}
					tmpClass = tmpClass.getSuperclass();
				} while (tmpClass != null && field == null);
			}
			if (field == null) {
				LOGGER.warn("Field '" + fieldName + "' not found on class " + clazz);
				// HashMap handles null values so we can use containsKey to cach
				// invalid fields and hence skip the reflection scan
				FIELD_CACHE.put(key, null);
			}
			// throw new RuntimeException("Field '" + fieldName +
			// "' not found on class " + clazz);
		}

		return field;
	}
	



	

	

}
