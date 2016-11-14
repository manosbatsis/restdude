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
package com.restdude.mdd.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CreateClassCommand {
	
	String name; 
	Class<?> baseImpl; 
	Collection<Class<?>> interfaces; 
	Collection<Class<?>> genericTypes;
	Map<Class<?>, Map<String, Object>> typeAnnotations;
	
	public CreateClassCommand(String name, Class<?> baseImpl) {
		super();
		this.name = name;
		this.baseImpl = baseImpl;
	}
	
	public CreateClassCommand(String name, Class<?> baseImpl,
			Collection<Class<?>> interfaces, Collection<Class<?>> genericTypes) {
		this(name, baseImpl);
		this.interfaces = interfaces;
		this.genericTypes = genericTypes;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getBaseImpl() {
		return baseImpl;
	}
	public void setBaseImpl(Class<?> baseImpl) {
		this.baseImpl = baseImpl;
	}
	public Collection<Class<?>> getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(Collection<Class<?>> interfaces) {
		this.interfaces = interfaces;
	}
	public Collection<Class<?>> getGenericTypes() {
		return genericTypes;
	}
	public void setGenericTypes(Collection<Class<?>> genericTypes) {
		this.genericTypes = genericTypes;
	}
	public Map<Class<?>, Map<String, Object>> getTypeAnnotations() {
		return typeAnnotations;
	}
	public void setTypeAnnotations(Map<Class<?>, Map<String, Object>> typeAnnotations) {
		this.typeAnnotations = typeAnnotations;
	}
	public CreateClassCommand addGenericType(Class<?> genericType) {
		if(this.genericTypes == null){
			this.genericTypes = new LinkedList<Class<?>>();
		}
		this.genericTypes.add(genericType);
		return this;
	}
	public CreateClassCommand addInterface(Class<?> interfaze) {
		if(this.interfaces == null){
			this.interfaces = new LinkedList<Class<?>>();
		}
		this.interfaces.add(interfaze);
		return this;
	}
	
	public CreateClassCommand addTypeAnnotation(Class<?> annotation, Map<String, Object> members) {
		if(this.typeAnnotations == null){
			this.typeAnnotations = new HashMap<Class<?>, Map<String, Object>>();
		}
		this.typeAnnotations.put(annotation, members);
		return this;
	}
	
	
}
