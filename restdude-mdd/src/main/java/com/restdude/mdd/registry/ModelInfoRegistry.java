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

import com.restdude.domain.Model;
import com.restdude.mdd.util.EntityUtil;
import com.restdude.util.ClassUtils;
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

import java.io.Serializable;
import java.lang.reflect.Modifier;
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

    //@Getter
    //private EntityDictionary entityDictionary = new EntityDictionary(new ConcurrentHashMap());

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
            Set<BeanDefinition> entityBeanDefs = EntityUtil.findAllModels(basePackage);
            for (BeanDefinition beanDef : entityBeanDefs) {
                Class<? extends Model> modelType = (Class<? extends Model>) ClassUtils.getClass(beanDef.getBeanClassName());
                this.addEntryFor(modelType);
            }
        }

        // field > model ref
        for(ModelInfo modelInfo : this.getEntries()){
            setRelatedFieldsModelInfo(modelInfo, modelInfo.getToOneFieldNames());
            setRelatedFieldsModelInfo(modelInfo, modelInfo.getToManyFieldNames());
        }
    }

    /**
     * Set the reverse entity ModelInfo for each relationship field
     * @param modelInfo
     * @param fNames
     */
    private void setRelatedFieldsModelInfo(ModelInfo modelInfo, Set<String> fNames) {
        for(String fieldName : fNames){
            FieldInfo field = modelInfo.getField(fieldName);
            LOGGER.debug("setRelatedFieldModelInfo, model: {}, fieldName: {}, field: {}", modelInfo.getModelType(), fieldName, field);
            Class<?> fieldModelType = field.getFieldModelType();

            ModelInfo relatedModelInfo = fieldModelType != null ? this.getEntryFor(field.getFieldModelType()) : null;
            if(relatedModelInfo != null){
                field.setRelatedModelInfo(relatedModelInfo);
            }
        }
    }

    protected <T extends Model<PK>, PK extends Serializable> void addEntryFor(Class<T> modelClass){
        Assert.notNull(modelClass, "Parameter modelClass cannot be null");

        // ignore abstract classes
        if(Modifier.isAbstract(modelClass.getModifiers())){
            LOGGER.warn("addEntryFor, given model class is abstract: {}", modelClass);
        }

        LOGGER.info("addEntryFor model class {}", modelClass.getCanonicalName());
        // check for existing
        if(this.modelEntries.containsKey(modelClass)){
            throw new RuntimeException("ModelInfoRegistry entry already exists, failed to add model type: " + modelClass.getCanonicalName());
        }

        // create entry
        ModelInfo<T, PK> entry = new ModelInfoImpl<>(modelClass);

        // add entry
        this.modelEntries.put(modelClass, entry);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        LOGGER.debug("postProcessBeanDefinitionRegistry, basePath: {}, defaultParentPath: {}, base packages:: {}", this.basePath, this.defaultParentPath, this.BASEPACKAGES_DEFAULT);
        this.scanPackages(BASEPACKAGES_DEFAULT);
        ModelBasedComponentGenerator generator = new ModelBasedComponentGenerator(registry, this.modelEntries, BASEPACKAGES_DEFAULT, this.basePath, this.defaultParentPath);
        generator.createComponentsFor();

        for(ModelInfo info : this.getEntries()){
            this.resolveInverseFields(info);
            if(!this.handlerModelTypes.containsKey(info.getModelControllerType())){
                if(info.getModelControllerType() != null && info.getModelType() != null) {
                    LOGGER.debug("postProcessBeanDefinitionRegistry, adding handlerModelType entry: {}:{}, linkable: {}", info.getModelControllerType(), info.getModelType(), info.isLinkableResource());
                    this.handlerModelTypes.put(info.getModelControllerType(), info.getModelType());
                }
            }
        }

    }

    protected void resolveInverseFields(ModelInfo modelInfo) {
        resolveInverseFields(modelInfo, modelInfo.getToOneFieldNames());
        resolveInverseFields(modelInfo, modelInfo.getToManyFieldNames());
    }

    protected void resolveInverseFields(ModelInfo modelInfo, Set<String> fieldNames) {
        for(String fieldName : fieldNames){
            LOGGER.debug("resolveInverseFields for model type: {}, field: {}", modelInfo.getModelType(), fieldName);
            FieldInfo field = modelInfo.getField(fieldName);
            if(!field.isInverse()){
                // scan ModelInfo on the other side to find an inverse JPA mapping, if any
                ModelInfo inverseModelInfo = this.getEntryFor(field.getFieldModelType());

                // warn if the inverse field type model info does not exist
                if(inverseModelInfo == null){
                    LOGGER.warn("resolveInverseFields: No model info entry found for type: {}", field.getFieldModelType());
                }
                else{
                    Set<String> inversePropertyNames = inverseModelInfo.getInverseFieldNames();

                    // go over inverse fields to find a match, if any
                    for(String inversePropertyName: inversePropertyNames){
                        FieldInfo inverseField = inverseModelInfo.getField(inversePropertyName);
                        if(inverseField != null && fieldName.equals(inverseField.getReverseFieldName())){
                            field.setReverseFieldName(inverseField.getFieldName());
                            break;
                        }
                    }
                }

            }
            // break if found
            Optional<String> reverseFieldName = field.getReverseFieldName();
            if(reverseFieldName.isPresent()){
                LOGGER.debug("resolveInverseFields, resolved field: {}, reverse: {}", field.getFieldName(), reverseFieldName.get());
                break;
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
