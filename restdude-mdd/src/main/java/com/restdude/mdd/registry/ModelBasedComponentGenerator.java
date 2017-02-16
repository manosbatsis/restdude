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

import com.restdude.mdd.annotation.controller.ModelController;
import com.restdude.mdd.annotation.model.ModelRelatedResource;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractModelServiceBackedController;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.mdd.repository.ModelRepositoryFactoryBean;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.specifications.AnyToOnePredicateFactory;
import com.restdude.mdd.specifications.IPredicateFactory;
import com.restdude.mdd.specifications.SpecificationUtils;
import com.restdude.mdd.util.CreateClassCommand;
import com.restdude.mdd.util.JavassistUtil;
import com.restdude.mdd.util.ModelContext;
import com.restdude.util.ClassUtils;
import com.restdude.util.Mimes;
import io.swagger.annotations.Api;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Named;
import java.util.*;

/**
 * Generates <code>Repository</code>, <code>Service</code> and
 * <code>Controller</code> mdd for classes are annotated with
 * {@link ModelResource} or
 * {@link ModelRelatedResource}.
 */
public class ModelBasedComponentGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelBasedComponentGenerator.class);

    private Map<Class<?>, ModelContext> entityModelContextsMap = new HashMap<Class<?>, ModelContext>();

    private Map<Class<?>, ModelInfo> modelInfoEntries;
    private BeanDefinitionRegistry registry;
    private String[] basePackages;
    private String basePath;
    private String defaultParentPath;

    public ModelBasedComponentGenerator(BeanDefinitionRegistry registry, Map<Class<?>, ModelInfo> modelInfoEntries, String[] basePackages, String basePath, String defaultParentPath){
        this.modelInfoEntries = modelInfoEntries;
        this.registry = registry;
        this.basePackages = basePackages;
        this.basePath = basePath;
        this.defaultParentPath = defaultParentPath;
    }
    /**
     * Create and register missing model-based components
     */
    public void createComponentsFor() throws BeansException {
        try {
            createModelContexts();
            findExistingBeans();
            createMissingBeans();
            LOGGER.info("Completed generation");
        } catch (Exception e) {
            throw new FatalBeanException("Failed generating ApiResources", e);
        }
    }

    private void createMissingBeans() throws NotFoundException, CannotCompileException {

        for (Class<?> model : this.entityModelContextsMap.keySet()) {
            // TODO: add related, after ensuring we have the necessary parent config set...

            ModelContext modelContext = this.entityModelContextsMap.get(model);

            Class<IPredicateFactory> predicateFactoryType = createPredicateFactory(modelContext);
            IPredicateFactory predicateFactory = ClassUtils.newInstance(predicateFactoryType);

            modelContext.setPredicateFactory(predicateFactory);
            SpecificationUtils.addFactoryForClass(modelContext.getModelType(), predicateFactory);


            if (model.isAnnotationPresent(ModelResource.class) || model.isAnnotationPresent(ModelRelatedResource.class)) {
                createMissingBeans(model, modelContext);
            }
        }

    }

    private void createMissingBeans(Class<?> model, ModelContext modelContext)
            throws NotFoundException, CannotCompileException {
        Assert.notNull(modelContext, "No model context was found for model type " + model.getName());

        createRepository(modelContext);
        createService(modelContext);
        createController(modelContext);

    }

    /**
     * Creates an {@link IPredicateFactory} instance that is parameterized for a specific entity model. The
     * predicate factory is registered using {@link SpecificationUtils#addFactoryForClass(java.lang.Class, com.restdude.mdd.specifications.IPredicateFactory)}
     *
     * @param modelContext
     * @see SpecificationUtils#addFactoryForClass(java.lang.Class, com.restdude.mdd.specifications.IPredicateFactory)
     */
    protected Class<IPredicateFactory> createPredicateFactory(ModelContext modelContext) {
        Class<IPredicateFactory> factoryType = null;
        // only add if not explicitly set
        if (SpecificationUtils.getPredicateFactoryForClass(modelContext.getModelType()) == null && modelContext.getModelIdType() != null) {
            String className = "AnyToOne" + modelContext.getGeneratedClassNamePrefix() + "PredicateFactory";
            String fullClassName = new StringBuffer(modelContext.getBeansBasePackage())
                    .append(".specification.")
                    .append(className).toString();

            // gfire up a create command
            CreateClassCommand createPredicateCmd = new CreateClassCommand(fullClassName,
                    AnyToOnePredicateFactory.class);

            // grab the generic types
            List<Class<?>> genericTypes = modelContext.getGenericTypes();
            LOGGER.debug("Creating class " + fullClassName +
                    ", genericTypes: " + genericTypes);
            createPredicateCmd.setGenericTypes(genericTypes);

            // create and return the predicate class
            factoryType = (Class<IPredicateFactory>) JavassistUtil.createClass(createPredicateCmd);

        }
        return factoryType;
    }

    /**
     * Creates a controller for the given resource model. Consider the following
     * entity annotation:
     *
     * <pre>
     * {@code
     * &#64;ModelResource(pathFragment = "countries", apiName = "Countries", apiDescription = "Operations about countries") one
     * }
     * </pre>
     *
     * created for the Country class:
     *
     * <pre>
     * {
     * 	&#64;code
     * 	&#64;Controller
     * 	&#64;Api(tags = "Countries", description = "Operations about countries")
     * 	&#64;RequestMapping(pathFragment = "/api/rest/countries", produces = { "application/json",
     * 			"application/xml" }, consumes = { "application/json", "application/xml" })
     * 	public class CountryController extends AbstractPersistableModelController<Country, String, CountryService> {
     * 		private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);
     * 	}
     * }
     * </pre>
     *
     * @param modelContext
     */
    protected void createController(ModelContext modelContext) {

        Class<?> controllerClass = null;
        BeanDefinition beanDefinition;
        if (modelContext.getControllerDefinition() == null) {
            String className = "RestdudeGenerated" + modelContext.getGeneratedClassNamePrefix() + "Controller";
            String beanName = StringUtils.uncapitalize(className);
            String fullClassName = new StringBuffer(modelContext.getBeansBasePackage())
                    .append(".controller.")
                    .append(className).toString();

            // gfire up a create command
            CreateClassCommand createControllerCmd = new CreateClassCommand(fullClassName,
                    modelContext.getControllerSuperClass());

            // grab the generic types
            if(ArrayUtils.isNotEmpty(modelContext.getControllerSuperClass().getTypeParameters())){
                List<Class<?>> genericTypes = modelContext.getGenericTypes();
                genericTypes.add(modelContext.getServiceInterfaceType());
                createControllerCmd.setGenericTypes(genericTypes);
                LOGGER.debug("createController, Creating class " + fullClassName +
                        ", super: " + modelContext.getControllerSuperClass().getName() +
                        ", genericTypes: " + genericTypes);
            }

            // add @RestController stereotype annotation
            Map<String, Object> controllerMembers = new HashMap<String, Object>();
            controllerMembers.put("value", beanName);
            createControllerCmd.addTypeAnnotation(RestController.class, controllerMembers);

            // @RequestMapping
            ModelInfo modelInfo = this.modelInfoEntries.get(modelContext.getModelType());
            String modelUriComponent = modelInfo.getUriComponent();
            String modelParentPath = modelInfo.getParentPath(this.defaultParentPath);
            String modelBasePath = modelInfo.getBasePath(this.basePath);
            String pattern = new StringBuffer("/")
                    .append(modelBasePath)
                    .append("/")
                    .append(modelParentPath)
                    .append("/")
                    .append(modelUriComponent).toString();
            pattern = pattern.replaceAll("/{2,}", "/");
            LOGGER.debug("getMappedModelControllerClass adding pattern: {}", pattern);

            Map<String, Object> requestMappingMembers = new HashMap<>();
            requestMappingMembers.put("value", new String[]{pattern});
            // add JSON and HAL defaults
            String[] defaultMimes = {MimeTypeUtils.APPLICATION_JSON_VALUE, Mimes.MIME_APPLICATIOM_HAL_PLUS_JSON_VALUE};
            requestMappingMembers.put("consumes", defaultMimes);
            requestMappingMembers.put("produces", defaultMimes);
            createControllerCmd.addTypeAnnotation(RequestMapping.class, requestMappingMembers);

            // add HATEOAS links support?
            Map<String, Object> exposesResourceForMembers = new HashMap<>();
            exposesResourceForMembers.put("value", modelContext.getModelType());
            createControllerCmd.addTypeAnnotation(ExposesResourceFor.class, exposesResourceForMembers);


            // set swagger Api annotation
            Map<String, Object> apiMembers = modelContext.getApiAnnotationMembers();
            if (MapUtils.isNotEmpty(apiMembers)) {
                createControllerCmd.addTypeAnnotation(Api.class, apiMembers);
            }

            // create and register controller class
            controllerClass = JavassistUtil.createClass(createControllerCmd);

            // add service dependency
            String serviceDependency = StringUtils.uncapitalize(modelContext.getGeneratedClassNamePrefix()) + "Service";
            beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(controllerClass)
                    .addDependsOn(serviceDependency).setAutowireMode(Autowire.BY_NAME.value()).getBeanDefinition();

            LOGGER.info("createController, Registering bean " + beanName);
            registry.registerBeanDefinition(beanName, beanDefinition);

            modelContext.setControllerDefinition(beanDefinition);

        }

    }


    protected void createService(ModelContext modelContext)
            throws NotFoundException, CannotCompileException {
        if (modelContext.getServiceDefinition() == null) {

            String className = modelContext.getGeneratedClassNamePrefix() + "Service";
            String beanName = StringUtils.uncapitalize(className);
            String fullClassName = new StringBuffer(modelContext.getBeansBasePackage())
                    .append(".service.")
                    .append(className).toString();
            LOGGER.debug("createService interface: {}", fullClassName);


            // grab the generic types
            List<Class<?>> genericTypes = modelContext.getGenericTypes();

            // extend the base service interface
            Class<?> newServiceInterface = JavassistUtil.createInterface(fullClassName,
                    PersistableModelService.class, genericTypes);
            ArrayList<Class<?>> interfaces = new ArrayList<Class<?>>(1);
            interfaces.add(newServiceInterface);

            // create a service implementation bean
            String newBImpllassName = new StringBuffer(modelContext.getBeansBasePackage())
                    .append(".service.impl.")
                    .append(className)
                    .append("Impl").toString();
            LOGGER.debug("createService class: {}", newBImpllassName);
            CreateClassCommand createServiceCmd = new CreateClassCommand(newBImpllassName, AbstractPersistableModelServiceImpl.class);
            createServiceCmd.setInterfaces(interfaces);
            createServiceCmd.setGenericTypes(genericTypes);
            createServiceCmd.addGenericType(modelContext.getRepositoryType());
            HashMap<String, Object> named = new HashMap<String, Object>();
            named.put("pathFragment", beanName);
            createServiceCmd.addTypeAnnotation(Named.class, named);

            // create and register a service implementation bean
            Class<?> serviceClass = JavassistUtil.createClass(createServiceCmd);
            AbstractBeanDefinition def = BeanDefinitionBuilder.rootBeanDefinition(serviceClass).getBeanDefinition();
            registry.registerBeanDefinition(beanName, def);

            // note in context as a dependency to a controller
            modelContext.setServiceDefinition(def);
            modelContext.setServiceInterfaceType(newServiceInterface);
            modelContext.setServiceImplType(serviceClass);

        } else {
            Class<?> serviceType = ClassUtils.getClass(modelContext.getServiceDefinition().getBeanClassName());
            // grab the service interface
            if (!serviceType.isInterface()) {
                Class<?>[] serviceInterfaces = serviceType.getInterfaces();
                if (ArrayUtils.isNotEmpty(serviceInterfaces)) {
                    for (Class<?> interfaze : serviceInterfaces) {
                        if (PersistableModelService.class.isAssignableFrom(interfaze)) {
                            modelContext.setServiceInterfaceType(interfaze);
                            break;
                        }
                    }
                }
            }
            Assert.notNull(modelContext.getRepositoryType(),
                    "Found a service bean definition for " + modelContext.getGeneratedClassNamePrefix()
                            + "  but failed to figure out the service interface type.");
        }
    }

    protected void createRepository(ModelContext modelContext)
            throws NotFoundException, CannotCompileException {
        if (modelContext.getRepositoryDefinition() == null) {
            Class<?> repoSUperInterface = ModelRepository.class;

            String className = modelContext.getGeneratedClassNamePrefix() + "Repository";
            String fullClassName = new StringBuffer(modelContext.getBeansBasePackage())
                    .append(".repository.")
                    .append(className).toString();

            // grab the generic types
            List<Class<?>> genericTypes = modelContext.getGenericTypes();
            LOGGER.debug("#createRepository: create repository: {}, genericTypes: {}", fullClassName, genericTypes);

            // create the new interface
            Class<?> newRepoInterface = JavassistUtil.createInterface(
                    fullClassName
                    , repoSUperInterface,
                    genericTypes, modelContext.isAuditable());

            // register using the uncapitalised className as the key
            AbstractBeanDefinition def = BeanDefinitionBuilder.rootBeanDefinition(ModelRepositoryFactoryBean.class)
                    .addPropertyValue("repositoryInterface", newRepoInterface).getBeanDefinition();
            registry.registerBeanDefinition(StringUtils.uncapitalize(newRepoInterface.getSimpleName()), def);

            // note the repo in context
            modelContext.setRepositoryDefinition(def);
            modelContext.setRepositoryType(newRepoInterface);

        } else {
            LOGGER.debug("#createRepository: NOTE repository for model type: {}", modelContext.getModelType().getName());
            // mote the repository interface as a possible dependency to a
            // service
            Class<?> beanClass = ClassUtils.getClass(modelContext.getRepositoryDefinition().getBeanClassName());
            // get the actual interface in case of a factory
            if (ModelRepositoryFactoryBean.class.isAssignableFrom(beanClass)) {
                for (PropertyValue propertyValue : modelContext.getRepositoryDefinition().getPropertyValues()
                        .getPropertyValueList()) {
                    if (propertyValue.getName().equals("repositoryInterface")) {
                        Object obj = propertyValue.getValue();
                        modelContext.setRepositoryType(String.class.isAssignableFrom(obj.getClass())
                                ? ClassUtils.getClass(obj.toString()) : (Class<?>) obj);
                    }
                }
            }
            Assert.notNull(modelContext.getRepositoryType(),
                    "Found a repository (factory) bean definition for " + modelContext.getGeneratedClassNamePrefix()
                            + "  but was unable to figure out the repository type.");
        }
    }

    /**
     * Iterate over registered beans to find any manually-created components
     * (Controllers, Services, Repositories) we can skipp from generating.
     *
     */
    protected void findExistingBeans() {
        for (String name : registry.getBeanDefinitionNames()) {

            BeanDefinition d = registry.getBeanDefinition(name);

            if (d instanceof AbstractBeanDefinition && d.getBeanClassName() != null) {
                AbstractBeanDefinition def = (AbstractBeanDefinition) d;
                Class<?> beanType = ClassUtils.getClass(def.getBeanClassName());

                // if model controller
                if (beanType.isAnnotationPresent(ModelController.class) || isOfType(def, AbstractModelServiceBackedController.class)) {

                    Class<?> modelType = getaControllerModelType(beanType);

                    ModelContext modelContext = entityModelContextsMap.get(modelType);
                    if (modelContext != null) {
                        modelContext.setControllerDefinition(def);
                    }
                    else{
                        throw new RuntimeException("Invalid model type " + modelType.getCanonicalName() + " specified for controller class " + beanType.getCanonicalName() + "");
                    }
                }
                /*
                if (isOfType(def, AbstractPersistableModelController.class)) {
                    Class<?> entity = GenericTypeResolver.resolveTypeArguments(
                            beanType, AbstractPersistableModelController.class)[0];

                    ModelContext modelContext = entityModelContextsMap.get(entity);
                    if (modelContext != null) {
                        modelContext.setControllerDefinition(def);
                        LOGGER.debug("findExistingBeans, found existing model controller class: {}, mapping: {}", beanType.getCanonicalName(), beanType.getAnnotation(RequestMapping.class));
                    }
                }
                */
                // if service
                else if (isOfType(def, AbstractPersistableModelServiceImpl.class)) {
                    Class<?> entity = GenericTypeResolver
                            .resolveTypeArguments(ClassUtils.getClass(def.getBeanClassName()), PersistableModelService.class)[0];
                    ModelContext modelContext = entityModelContextsMap.get(entity);
                    if (modelContext != null) {
                        modelContext.setServiceDefinition(def);
                    }
                }
                // if repository
                else if (isOfType(def, JpaRepositoryFactoryBean.class) || isOfType(def, JpaRepository.class)) {
                    String repoName = (String) def.getPropertyValues().get("repositoryInterface");

                    Class<?> repoInterface = ClassUtils.getClass(repoName);
                    LOGGER.debug("#findExistingBeans found repository bean: {}, class: {}", repoName, repoInterface.getCanonicalName());
                    if (JpaRepository.class.isAssignableFrom(repoInterface)) {
                        LOGGER.debug("#findExistingBeans repository bean: {}, is assignable", repoName);
                        Class<?> entity = GenericTypeResolver.resolveTypeArguments(repoInterface,
                                JpaRepository.class)[0];
                        ModelContext modelContext = entityModelContextsMap.get(entity);
                        if (modelContext != null) {
                            LOGGER.debug("#findExistingBeans repository bean: {}, added to modelContext", repoName);
                            modelContext.setRepositoryDefinition(def);
                        }
                    }
                }

            }

        }
    }

    private Class<?> getaControllerModelType(Class<?> controllerType) {
        Class<?> modelType = null;
        boolean isModelController = controllerType.isAnnotationPresent(ModelController.class);
        boolean isControllerSubclass = AbstractModelServiceBackedController.class.isAssignableFrom(controllerType);
        if(isControllerSubclass){
            modelType = GenericTypeResolver.resolveTypeArguments(
                    controllerType, AbstractModelServiceBackedController.class)[0];
        }
        else if(isModelController){
            ModelController ann = controllerType.getAnnotation(ModelController.class);
            modelType = ann.modelType();
            if(Object.class.equals(modelType)){
                throw new RuntimeException("Cannot determine model type for controller class " + controllerType.getCanonicalName() + " as ns class is not a recognized subclass and no modelType was specified in the annotation");
            }
        }
        return modelType;
    }

    /**
     * Checks if the given BeanDefinition extends/impleents the given target
     * type
     *
     * @param beanDef
     * @param targetType
     * @return
     */
    protected boolean isOfType(BeanDefinition beanDef, Class<?> targetType) {
        if (beanDef.getBeanClassName() != null) {
            Class<?> beanClass = ClassUtils.getClass(beanDef.getBeanClassName());
            return targetType.isAssignableFrom(beanClass);
        }
        return false;
    }

    // @Override
    protected void createModelContexts() throws Exception {
        Collection<ModelInfo> modelRegistryEntries = this.modelInfoEntries.values();
        for (ModelInfo modelInfo : modelRegistryEntries) {
            Class<?> modelType = modelInfo.getModelType();
            LOGGER.info("Found resource model class {}", modelType.getCanonicalName());
            entityModelContextsMap.put(modelType, new ModelContext(modelInfo));
        }
    }

}
