/**
 * calipso-hub-utilities - A full stack, high level framework for lazy application hackers.
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
package com.restdude.util;

public class ClassUtils {

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


}
