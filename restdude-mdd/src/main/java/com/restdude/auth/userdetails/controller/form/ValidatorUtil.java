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
package com.restdude.auth.userdetails.controller.form;

import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.util.exception.http.BeanValidationException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
*/
public class ValidatorUtil {

	public static final ConcurrentHashMap<Class, List<String>> uniqueFieldNames = new ConcurrentHashMap<Class, List<String>>();

	public static List<String> getUniqueFieldNames(Class beanClass) {
		List<String> names;

		// if names have not been initialized for bean classz
		if (uniqueFieldNames.containsKey(beanClass)) {
			names = uniqueFieldNames.get(beanClass);
		} else {
			// init unique names
			names = new LinkedList<String>();
			Field[] fields = FieldUtils.getFieldsWithAnnotation(beanClass, Column.class);
			if (fields.length > 0) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					Column column = field.getAnnotation(Column.class);

					// if unique or not-null field
					if (!field.getName().equals("id")) {
						if (column.unique()) {
							names.add(field.getName());
						}
					}
				}
			}

			// cache unique names for bean class
			uniqueFieldNames.put(beanClass, names);
		}

		return names;
	}

	public static void addValidationError(String field, ConstraintValidatorContext context) {
		context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addNode(field).addConstraintViolation();
	}

	public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field f = object.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);
		return f.get(object);
    }


    public static void throwIfNonEmpty(Set<ConstraintViolation<UsernameChangeRequest>> constraintViolations, String modelType) {
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            Set<ConstraintViolation> errors = new HashSet<ConstraintViolation>();
            errors.addAll(constraintViolations);
            BeanValidationException ex = new BeanValidationException("Validation failed", errors);
            ex.setModelType(modelType);
            throw ex;
        }
    }
}