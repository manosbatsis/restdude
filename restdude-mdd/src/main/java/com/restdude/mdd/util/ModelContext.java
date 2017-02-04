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
package com.restdude.mdd.util;

import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.mdd.annotation.ModelRelatedResource;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.ClassUtils;
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
 * Adapter-ish context class for classes with {@link com.restdude.mdd.annotation.ModelResource}
 * and {@link com.restdude.mdd.annotation.ModelRelatedResource}
 * annotations.
 */
public final class ModelContext {
	
	private static final String AUDITABLE2 = "auditable";

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelContext.class);

    private Class<?> modelType;
    
    private Class<?> modelIdType;

    private Class<?> parentClass;
    private String name, path, parentProperty, generatedClassNamePrefix, beansBasePackage;

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
        if (this.getModelIdType() != null) {
            genericTypes.add(this.getModelIdType());
        }
        return genericTypes;
    }

    public ModelContext(Class<?> domainClass) {
        Assert.notNull(domainClass, "A domain class is required");
        LOGGER.debug("domainClass: {}", domainClass);
        String packageName = domainClass.getPackage().getName();
        this.beansBasePackage = packageName.endsWith(".model") ? packageName.substring(0, packageName.indexOf(".model")) : packageName;
        this.modelType = domainClass;
        this.generatedClassNamePrefix = domainClass.getSimpleName().replace("Model", "").replace("Entity", "");
        this.modelIdType = ClassUtils.getBeanPropertyType(domainClass, "pk", true);
    }


    public ModelContext(ModelResource modelResource, Class<?> domainClass){
        Assert.notNull(domainClass, "A domain class is required");
        String packageName = domainClass.getPackage().getName();
        this.beansBasePackage = packageName.endsWith(".model") ? packageName.substring(0, packageName.indexOf(".model")) : packageName;
        this.modelType = domainClass;
        this.modelResource = modelResource;

        this.name = getPath(domainClass);
        this.apiAnnotationMembers = getApiAnnotationMembers(domainClass);
        this.path = "/" + name;

        this.generatedClassNamePrefix = domainClass.getSimpleName().replace("Model", "").replace("Entity", "");
        this.parentClass = null;
        this.parentProperty = null;

        this.modelIdType = ClassUtils.getBeanPropertyType(domainClass, "pk", true);

    }



    public static ModelContext from(Class<?> domainClass){

        ModelResource ar = domainClass.getAnnotation(ModelResource.class);

        ModelContext wrapper = null;
        if( ar != null ){
            wrapper = new ModelContext(ar, domainClass);
        }

        // finally add a wrapper for limited processing if needed
        if (wrapper == null) {
            wrapper = new ModelContext(domainClass);
        }
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
        Class sClass = this.modelResource.controllerSuperClass();
        if(sClass == null || Object.class.equals(sClass)){
            sClass = AbstractModelController.class;
        }
        return  sClass;
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

        StringBuffer result = new StringBuffer();

        // parent app path
        String appParent = ar.applicationParent();
        if(StringUtils.isNotBlank(appParent)){
            result.append(appParent);
            if(!appParent.endsWith("/")){
                result.append("/");
            }
        }

        // append app name
        String appName;
        if( ar != null ){
            appName = ar.value();
        }else if( anr != null){
            appName = anr.path();
        }else{
            throw new IllegalStateException("Not an entity");
        }
        if( StringUtils.isBlank(appName)){
            appName = domainClass.getSimpleName();
            appName = appName.toLowerCase().charAt(0) + result.substring(1) + "s";
        }
        result.append(appName);

        return result.toString();
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
            // or value
            else if(StringUtils.isNotBlank(resource.value())){

            	String[] tags = {resource.value()};
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
