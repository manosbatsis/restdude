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
package com.restdude.mdd.registry;

import com.restdude.mdd.util.EntityUtil;
import com.restdude.util.ClassUtils;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import java.util.*;

/**
 * Provides metadata for all Model types and generates missing model-based components
 *  i.e. <code>Repository</code>, <code>Service</code> and
 * <code>Controller</code> mdd
 *
 * @see ModelBasedComponentGenerator
 */
@Component
public class ModelInfoRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelInfoRegistry.class);

    public static final String[] BASEPACKAGES_DEFAULT = {"**.calipso", "**.restdude"};
    public static final String BEAN_NAME = "modelInfoRegistry";

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //@Value("${restdude.api.basePath}")
    private String basePath = "/api/rest";

    //@Value("${restdude.api.defaultParentPath}")
    private String defaultParentPath = "";


    private Map<Class<?>, ModelInfo> modelEntries = new HashMap<>();

    private Map<Class<?>, Class<?>> handlerModelTypes = new HashMap<>();

    @Getter
    private EntityDictionary entityDictionary = new EntityDictionary();

    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        applicationContext = appContext;
    }

    public ModelInfo getEntryFor(Class<?> modelClass){
        Assert.notNull(modelClass, "Cannot get entry for null modelClass");
        return this.modelEntries.get(modelClass);
    }

    public List<ModelInfo> getEntries(){
        ArrayList<ModelInfo> entries = new ArrayList<>(this.modelEntries.size());
        entries.addAll(this.modelEntries.values());
        return entries;
    }

    public List<Class> getTypes(){
        ArrayList<Class> entries = new ArrayList<>(this.modelEntries.size());
        entries.addAll(this.modelEntries.keySet());
        return entries;
    }

    protected void scanPackages(String... basePackages) {

        // scan for models
        for (String basePackage : basePackages) {
            Set<BeanDefinition> entityBeanDefs = EntityUtil.findPersistableModels(basePackage);
            for (BeanDefinition beanDef : entityBeanDefs) {
                Class<?> modelType = ClassUtils.getClass(beanDef.getBeanClassName());
                LOGGER.info("Found model class {}", modelType.getCanonicalName());
                this.addEntryFor(modelType);
            }
        }

        Set<Class<?>> entityTypes = this.entityDictionary.getBindings();
        LOGGER.debug("scanPackages finished, bindings: {}", entityTypes);


        for(Class<?> entityType : entityTypes){
            LOGGER.debug("scanPackages finished, entity json name: {}, type: {}", this.entityDictionary.getJsonAliasFor(entityType), entityType);

        }
    }

    protected void addEntryFor(Class modelClass){
        Assert.notNull(modelClass, "Parameter modelClass cannot be null");

        // check for existing
        if(this.modelEntries.containsKey(modelClass)){
            throw new RuntimeException("ModelInfoRegistry entry already exists, failed to add model type: " + modelClass.getCanonicalName());
        }

        // create entry
        ModelInfo entry = new ModelInfo(modelClass);

        // add entry
        this.modelEntries.put(modelClass, entry);

        // if an entity, add to dictionary
        if(modelClass.isAnnotationPresent(Entity.class)){
            LOGGER.debug("addEntryFor binding dictionary entity: {}", modelClass);
            this.entityDictionary.bindEntity(modelClass);
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        LOGGER.debug("postProcessBeanDefinitionRegistry, basePath: {}, defaultParentPath: {}, base packages:: {}", this.basePath, this.defaultParentPath, this.BASEPACKAGES_DEFAULT);
        this.scanPackages(BASEPACKAGES_DEFAULT);
        ModelBasedComponentGenerator generator = new ModelBasedComponentGenerator(registry, this.modelEntries, BASEPACKAGES_DEFAULT, this.basePath, this.defaultParentPath);
        generator.createComponentsFor();

        for(ModelInfo info : this.getEntries()){
            if(!this.handlerModelTypes.containsKey(info.getModelControllerType())){
                if(info.getModelControllerType() != null && info.getModelType() != null) {
                    LOGGER.debug("postProcessBeanDefinitionRegistry, adding handlerModelType entry: {}:{}", info.getModelControllerType(), info.getModelType());
                    this.handlerModelTypes.put(info.getModelControllerType(), info.getModelType());
                }
            }
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public Class<?> getHandlerModelType(@NonNull Class<?> handlerType) {
        /*
        if(String.class.isAssignableFrom(handler.getClass())){

            LOGGER.debug("getHandlerModelType, handler is a spring bean name: {}", handler);
            handler = getApplicationContext().getBean(handler.toString());
        }
        Class<?> modelType = this.handlerModelTypes.get(AopProxyUtils.ultimateTargetClass(handler));
        LOGGER.debug("getHandlerModelType, modelType: {}", modelType);

        return modelType;
        */
        return this.handlerModelTypes.get(handlerType);
    }
}
