/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-util, https://manosbatsis.github.io/restdude/restdude-util
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
package com.restdude.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

public class ClassUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

	/**
     * Returns the (initialized) class represented by <code>className</code>
     * using the current thread's context class loader and wraps any exceptions 
     * in a RuntimeException. 
     * 
     * This implementation
     * supports the syntaxes "<code>java.util.Map.Entry[]</code>",
     * "<code>java.util.Map$Entry[]</code>", "<code>[Ljava.util.Map.Entry;</code>",
     * and "<code>[Ljava.util.Map$Entry;</code>".
     *
     * @param className  the class name
     * @return the class represented by <code>className</code> using the current thread's context class loader
     * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> getClass(String className) {
		Class<?> clazz;
		try {
			clazz = org.apache.commons.lang.ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return clazz;
	}

    public static <T extends Object> T newInstance(Class<T> clazz) {
        Assert.notNull(clazz, "clazz cannot be null");
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed instantiating new instance for class: " + clazz.getCanonicalName(), e);
        }
    }

    public static Class<?> getBeanPropertyType(Class clazz, String fieldName, boolean silent) {
        Class fieldType = null;
        String[] steps = fieldName.contains(".") ? fieldName.split(".") : new String[]{fieldName};

        try {

            Iterator<String> stepNames = Arrays.asList(steps).listIterator();
            Class tmpClass = clazz;
            String tmpFieldName = null;
            while (stepNames.hasNext() && tmpClass != null) {
                tmpFieldName = stepNames.next();

                for (PropertyDescriptor pd : Introspector.getBeanInfo(tmpClass).getPropertyDescriptors()) {
                    if (tmpFieldName.equals(pd.getName())) {
                        Method getter = pd.getReadMethod();
                        if (getter != null) {
                            tmpClass = GenericTypeResolver.resolveReturnType(getter, tmpClass);
                        } else {
                            tmpClass = null;
                        }
                    }
                }
            }
            fieldType = tmpClass;

        } catch (IntrospectionException e) {
            if (silent) {
                LOGGER.warn("failed getting type bean property: {}#{}", clazz.getCanonicalName(), fieldName);
            } else {
                throw new RuntimeException("failed getting bean property type", e);

            }

        }
        return fieldType;
    }



}
