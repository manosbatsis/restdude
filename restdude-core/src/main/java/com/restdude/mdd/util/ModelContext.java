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

import com.restdude.mdd.annotation.ModelRelatedResource;
import com.restdude.mdd.annotation.ModelResource;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.ManyToAny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Adapter-ish context class for classes with {@link javax.persistence.ModelResource}
 * and {@link com.restdude.mdd.annotation.ModelRelatedResource}
 * annotations.
 */
public final class ModelContext {
	
	private static final String AUDITABLE2 = "auditable";

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelContext.class);

    private Class<?> modelType;
    
    private Class<?> modelIdType;
    
    private final Class<?> parentClass;
    private final String name, path, parentProperty, generatedClassNamePrefix, beansBasePackage;

    private Class<?> repositoryType;
    private Class<?> serviceInterfaceType;
    private Class<?> serviceImplType;
    private AbstractBeanDefinition repositoryDefinition, serviceDefinition, controllerDefinition;

	private Map<String, Object> apiAnnotationMembers;

	private ModelResource modelResource;
	
	private boolean auditable;

	public boolean isAuditable() {
		return auditable;
	}

	public List<Class<?>> getGenericTypes() {
		List<Class<?>> genericTypes = new LinkedList<Class<?>>();
		genericTypes.add(this.getModelType());
		genericTypes.add(this.getModelIdType());
		return genericTypes;
	}
	
    public ModelContext(ModelResource modelResource, Class<?> domainClass){
    	Assert.notNull(domainClass, "A domain class is required");
    	String packageName = domainClass.getPackage().getName();
    	this.beansBasePackage = packageName.endsWith(".model") ? packageName.substring(0, packageName.indexOf(".model")) : packageName;
        this.modelType = (Class<?>) domainClass;
        this.modelResource = modelResource;
        
        this.name = getPath(domainClass);
        this.apiAnnotationMembers = getApiAnnotationMembers(domainClass);
        this.path = "/" + name;
        
        this.generatedClassNamePrefix = domainClass.getSimpleName().replace("Model", "").replace("Entity", "");
        this.parentClass = null;
        this.parentProperty = null;
        
        this.modelIdType = EntityUtil.getIdType(domainClass);
        
    }
	
    public ModelContext(ModelRelatedResource resource, Class<?> domainClass){
    	String packageName = domainClass.getPackage().getName();
    	this.beansBasePackage = packageName.endsWith(".model") ? packageName.substring(0, packageName.indexOf(".model")) : packageName;
        
        this.name = getPath(domainClass);
        this.apiAnnotationMembers = getApiAnnotationMembers(domainClass);

        String parentProperty = resource.parentProperty();
        this.parentClass = (Class<?>) ReflectionUtils.findField(domainClass, parentProperty).getType();

        String parentName = getPath(parentClass);
        this.path = "/" + parentName + "/{peId}/" + this.name;

        this.modelType = (Class<?>) domainClass;
        this.modelIdType = EntityUtil.getIdType(domainClass);
        this.generatedClassNamePrefix = domainClass.getSimpleName().replace("Model", "").replace("Entity", "");

        this.parentProperty = resource.parentProperty();
    }

    public static ModelContext from(Class<?> domainClass){

        ModelResource ar = domainClass.getAnnotation(ModelResource.class);
        ModelRelatedResource anr = domainClass.getAnnotation(ModelRelatedResource.class);

        ModelContext wrapper = null;
        if( ar != null ){
            wrapper = new ModelContext(ar, domainClass);
        }else if( anr != null ){
            wrapper = new ModelContext(anr, domainClass);
        }else{
            // look for an ancestor who might be a resource
            Class<?> superClass = domainClass.getSuperclass();
            if( superClass != null && !Object.class.equals( superClass )){
                wrapper = from( domainClass.getSuperclass() );
            }
        }
        wrapper.setModelType(domainClass);
        return wrapper;
    }

    private void setModelType(Class<?> modelType) {
		this.modelType = modelType;
	}

	public static ModelContext from(Field field){
        Class<?> domainClass = field.getType();
        if( Collection.class.isAssignableFrom(domainClass) ){
            domainClass = GenericCollectionTypeResolver.getCollectionFieldType(field);
        }
        return from(domainClass);
    };

    
    public Class getControllerSuperClass(){
    	return this.modelResource.controllerSuperClass();
    }
    
    public Class<?> getServiceInterfaceType() {
		return serviceInterfaceType;
	}

	public void setServiceInterfaceType(Class<?> serviceInterfaceType) {
		this.serviceInterfaceType = serviceInterfaceType;
	}

	public Class<?> getServiceImplType() {
		return serviceImplType;
	}

	public void setServiceImplType(Class<?> serviceImplType) {
		this.serviceImplType = serviceImplType;
	}

	public Class<?> getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(Class<?> repositoryType) {
		this.repositoryType = repositoryType;
	}

    public boolean isNested(){
        return parentClass != null;
    }

    public String getGeneratedClassNamePrefix() {
		return generatedClassNamePrefix;
	}

	public boolean isNestedCollection(){
        if( !isNested() ){
            return false;
        }

        ModelRelatedResource anr = modelType.getAnnotation(ModelRelatedResource.class);
        Assert.notNull(anr, "Not a nested resource");

        String parentProperty = anr.parentProperty();
        Field field = ReflectionUtils.findField(modelType, parentProperty);
        if( hasAnnotation(field, OneToOne.class, org.hibernate.mapping.OneToOne.class) ){
            return false;
        }else if( hasAnnotation(field, ManyToOne.class, org.hibernate.mapping.ManyToOne.class,
                ManyToMany.class, ManyToAny.class) ){ // TODO handle more mappings here?
            return true;
        }

        throw new IllegalStateException("No known mapping found");

    }

    private boolean hasAnnotation( Field field, Class<?>... annotations){

        for( Class<?> a : annotations ){
            if( field.isAnnotationPresent( (Class<Annotation>) a) ){
                return true;
            }
        }
        return false;
    }

    public static String getPath(Class<?> domainClass){
        ModelResource ar = domainClass.getAnnotation(ModelResource.class);
        ModelRelatedResource anr = domainClass.getAnnotation(ModelRelatedResource.class);

        String result;
        if( ar != null ){
            result = ar.path();
        }else if( anr != null){
            result = anr.path();
        }else{
            throw new IllegalStateException("Not an entity");
        }

        if( result == null || result.trim().isEmpty() ){
            result = domainClass.getSimpleName();
            result = result.toLowerCase().charAt(0) + result.substring(1) + "s";
        }

        return result;
    }

    public static Map<String, Object> getApiAnnotationMembers(Class<?> domainClass){
        ModelResource resource = domainClass.getAnnotation(ModelResource.class);
        Map<String, Object> apiAnnotationMembers = new HashMap<String, Object>();
        if( resource != null ){
        	// auditable?
        	apiAnnotationMembers.put(AUDITABLE2, resource.auditable());
        	// get tags (grouping key, try API name)
            if(StringUtils.isNotBlank(resource.apiName())){
            	String[] tags = {resource.apiName()};
            	apiAnnotationMembers.put("tags", tags);
            }
            // or path
            else if(StringUtils.isNotBlank(resource.path())){

            	String[] tags = {resource.path()};
            	apiAnnotationMembers.put("tags", tags);
            }
            // or simple name
            else{
            	String[] tags = {StringUtils.join(
           		     StringUtils.splitByCharacterTypeCamelCase(domainClass.getSimpleName()),
           		     ' '
           		)};
            	apiAnnotationMembers.put("tags", tags);
            }
            // add description
            if(StringUtils.isNotBlank(resource.apiDescription())){
            	apiAnnotationMembers.put("description", resource.apiDescription());
            }
        }else{
            throw new IllegalStateException("Not an entity");
        }

        return apiAnnotationMembers.size() > 0 ? apiAnnotationMembers : null;
    }

	
	public Class<?> getModelIdType() {
		return modelIdType;
	}

	public Class<?> getModelType() {
		return modelType;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public Map<String, Object> getApiAnnotationMembers() {
		return apiAnnotationMembers;
	}

	public String getParentProperty() {
		return parentProperty;
	}

	public AbstractBeanDefinition getRepositoryDefinition() {
		return repositoryDefinition;
	}

	public void setRepositoryDefinition(AbstractBeanDefinition repositoryDefinition) {
		this.repositoryDefinition = repositoryDefinition;
	}

	public AbstractBeanDefinition getServiceDefinition() {
		return serviceDefinition;
	}

	public void setServiceDefinition(AbstractBeanDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}

	public AbstractBeanDefinition getControllerDefinition() {
		return controllerDefinition;
	}

	public void setControllerDefinition(AbstractBeanDefinition controllerDefinition) {
		this.controllerDefinition = controllerDefinition;
	}

	public String getBeansBasePackage() {
		return beansBasePackage;
	};

}
