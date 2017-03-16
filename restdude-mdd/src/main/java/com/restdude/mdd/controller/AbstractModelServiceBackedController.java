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
package com.restdude.mdd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.restdude.domain.users.model.User;
import com.restdude.hypermedia.hateoas.ModelResource;
import com.restdude.hypermedia.hateoas.ModelResources;
import com.restdude.hypermedia.hateoas.PagedModelResources;
import com.restdude.hypermedia.jsonapi.JsonApiModelResource;
import com.restdude.hypermedia.jsonapi.JsonApiModelResourceCollectionDocument;
import com.restdude.hypermedia.jsonapi.JsonApiModelResourceDocument;
import com.restdude.hypermedia.util.HypermediaUtils;
import com.restdude.hypermedia.util.JsonApiModelBasedDocumentBuilder;
import com.restdude.mdd.annotation.model.CurrentPrincipal;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.RawJson;
import com.restdude.mdd.model.UserDetails;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.mdd.registry.ModelInfoRegistry;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.uischema.model.UiSchema;
import com.restdude.mdd.util.ParamsAwarePageImpl;
import com.restdude.rsql.RsqlUtils;
import com.restdude.util.ParamsAwarePage;
import lombok.NonNull;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Abstract REST controller using a service implementation
 * <p/>
 * <p>You should extend this class when you want to use a 3 layers pattern : Repository, Service and Controller
 * If you don't have a real service (also called business layer), consider using RepositoryBasedRestController</p>
 * <p/>
 * <p>Default implementation uses "pk" field (usually a Long) in order to identify resources in web request.
 * If your want to identity resources by a slug (human readable identifier), your should override plainJsonGetById() method with for example :
 * <p/>
 * <pre>
 * <code>
 * {@literal @}Override
 * public Sample plainJsonGetById({@literal @}PathVariable String pk) {
 * Sample sample = this.service.findByName(pk);
 * if (sample == null) {
 * throw new NotFoundException();
 * }
 * return sample;
 * }
 * </code>
 * </pre>
 *
 * @param <T>  Your resource class to manage, maybe an entity or DTO class
 * @param <PK> Resource pk type, usually Long or String
 * @param <S>  The service class
 */
public class AbstractModelServiceBackedController<T extends PersistableModel<PK>, PK extends Serializable, S extends PersistableModelService<T, PK>> implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelServiceBackedController.class);

    protected static final String PARAM_RELATION_NAME = "relationName";
    protected static final String PARAM_FILTER = "filter";
    protected static final String PARAM_JSONAPI_PAGE_NUMBER = "page[number]";
    protected static final String PARAM_JSONAPI_PAGE_SIZE = "page[size]";
    protected static final String PARAM_SORT = "sort";
    protected static final String PARAM_PK = "pk";
    protected static final String PARAM_PAGE_NUMBER = "_pn";
    protected static final String PARAM_PAGE_SIZE = "_ps";
    protected static final String[] PARAMS_IGNORE_FOR_CRITERIA = {PARAM_RELATION_NAME, PARAM_FILTER, PARAM_JSONAPI_PAGE_NUMBER, PARAM_JSONAPI_PAGE_SIZE, PARAM_SORT, PARAM_PK, PARAM_PAGE_NUMBER, PARAM_PAGE_SIZE};

    private ModelInfo modelInfo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected ModelInfoRegistry mmdelInfoRegistry;

    @Autowired
    protected EntityLinks entityLinks;

    protected S service;
    protected Class<T> modelType;
    protected Boolean isResourceSupport = false;

    @Inject
    public void setService(S service) {
        this.service = service;
    }


    public S getService() {
        return this.service;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.modelType = this.service.getDomainClass();
        this.isResourceSupport = ResourceSupport.class.isAssignableFrom(this.modelType);
    }

    /**
     * Get the ModelInfo for this Controller's Model type
     */
    protected ModelInfo getModelInfo() {
        if(this.modelInfo == null){
            this.modelInfo = this.mmdelInfoRegistry.getEntryFor(this.modelType);
            LOGGER.debug("toHateoasResource, modelInfo: {}", this.modelInfo);
        }
        return this.modelInfo;
    }


    /**
     * Wrap the given model in a {@link Resource} and add {@link org.springframework.hateoas.Link}s
     *
     * @param model
     */
    protected ModelResource<T> toHateoasResource(@NonNull T model) {
        return HypermediaUtils.toHateoasResource(model, this.getModelInfo());
    }


    /**
     * Wrap the given models in a {@link Resources} and add {@link org.springframework.hateoas.Link}s
     *
     * @param models
     */
    protected ModelResources<T> toHateoasResources(@NonNull Iterable<T> models) {
        Class<T> modelType = this.modelType;
        return HypermediaUtils.toHateoasResources(models, modelType);
    }

    /**
     * Convert the given {@link Page} to a {@link PagedResources} object and add {@link org.springframework.hateoas.Link}s
     *
     * @param page
     */
    protected PagedModelResources<T> toHateoasPagedResources(@NonNull ParamsAwarePageImpl<T> page, @NonNull String pageNumberParamName) {
        Class<T> modelType = this.modelType;
        return toHateoasPagedResources(page, modelType, pageNumberParamName);
    }

    /**
     * Convert the given {@link Page} to a {@link PagedResources} object and add {@link org.springframework.hateoas.Link}s
     *
     * @param page
     */
    protected <RT extends PersistableModel> PagedModelResources<RT> toHateoasPagedResources(@NonNull ParamsAwarePage<RT> page, @NonNull Class<RT> modelType, @NonNull String pageNumberParamName) {
        ModelInfo rootModelInfo = this.mmdelInfoRegistry.getEntryFor(modelType);

        // long size, long number, long totalElements, long totalPages
        PagedModelResources<RT> pagedResources = PagedModelResources.create(page, request, pageNumberParamName);


        return pagedResources;
    }

    /**
     * Wrap the given model in a JSON API Document
     * @param model the model to wrap
     * @return
     */
    protected JsonApiModelResourceDocument<T, PK> toDocument(T model) {
        return HypermediaUtils.toDocument(model, this.getModelInfo());
    }


    /**
     * Wrap the given collection of models in a JSON API Document
     * @param models the models to wrap
     * @return
     */
    protected JsonApiModelResourceCollectionDocument<T, PK> toDocument(Collection<T> models) {
        return new JsonApiModelBasedDocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(models)
                .buildModelCollectionDocument();
    }

    /**
     * Wrap the given iterable of models of models in a JSON API Document
     * @param models the models to wrap
     * @return
     */
    protected JsonApiModelResourceCollectionDocument<T, PK> toDocument(Iterable<T> models) {
        return new JsonApiModelBasedDocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(models)
                .buildModelCollectionDocument();
    }

    /**
     * Wrap the given {@link Page} of models in a JSON API Document
     * @param page the page to wrap
     * @return
     */
    protected JsonApiModelResourceCollectionDocument<T, PK> toPageDocument(ParamsAwarePage<T> page) {

        Class<T> modelType = this.modelType;
        return toPageDocument(page, this.getModelInfo(), "page[number]");
    }

    /*
     * Wrap the given {@link Page} of models in a JSON API Document
     * @param page the page to wrap
     * @param modelInfo
     * @param pageNumberParamName
     * @param <RT>
     * @param <RPK>
     * @return
     */
    protected <RT extends PersistableModel<RPK>, RPK extends Serializable> JsonApiModelResourceCollectionDocument<RT, RPK> toPageDocument(@NonNull ParamsAwarePage<RT> page, @NonNull ModelInfo<RT, RPK> modelInfo, @NonNull String pageNumberParamName) {
        JsonApiModelResourceCollectionDocument<RT, RPK> doc = new JsonApiModelBasedDocumentBuilder<RT, RPK>(this.getModelInfo().getUriComponent())
                .withData(page)
                .buildModelCollectionDocument();
        List<Link> tmp = HypermediaUtils.buileHateoasLinks(page, request, pageNumberParamName);
        if(CollectionUtils.isNotEmpty(tmp)){
            for(Link l : tmp){
                doc.add(l.getRel(), l.getHref());
            }
        }
        return doc;
    }

    /**
     * Unwrap the single model given as a JSON API Document
     * @param document
     * @return
     */
    protected T toModel(@NonNull @RequestBody JsonApiModelResourceDocument<T, PK> document) {
        T entity = null;
        JsonApiModelResource<T, PK> resource = document.getData();
        if(resource != null ){
            entity = resource.getAttributes();
            entity.setPk(resource.getIdentifier());
        }
        return entity;
    }


    
    protected T create(@NonNull T resource) {
        applyCurrentPrincipal(resource);
        return this.service.create(resource);
    }


    
    protected T update(PK pk, T resource) {
        Assert.notNull(pk, "pk cannot be null");
        resource.setPk(pk);
        applyCurrentPrincipal(resource);
        resource = this.service.update(resource);
        return resource;
    }

    
    protected T patch(PK pk, T resource) {
        applyCurrentPrincipal(resource);
        resource.setPk(pk);
        resource = this.service.patch(resource);
        return resource;
    }

    
    protected Iterable<T> findAll() {
        return service.findAll();
    }

    protected T findById(PK pk) {
        LOGGER.debug("plainJsonGetById, pk: {}, model type: {}", pk, this.service.getDomainClass());
        T resource = this.service.findById(pk);
        return resource;
    }

    
    protected Iterable<T> findByIds(@NonNull Set<PK> pks) {
        Assert.notNull(pks, "pks list cannot be null");
        return this.service.findByIds(pks);
    }

    
    protected void delete(@NonNull PK pk) {
        T resource = this.findById(pk);
        this.service.delete(resource);
    }

    
    protected void deleteAll() {
        this.service.deleteAllWithCascade();
    }

    protected RawJson getJsonSchema() throws JsonProcessingException {
        JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper, config);

        JsonNode jsonSchema = generator.generateJsonSchema(this.getService().getDomainClass());

        String jsonSchemaAsString = objectMapper.writeValueAsString(jsonSchema);
        return new RawJson(jsonSchemaAsString);
    }

    protected UiSchema getUiSchema() {
        UiSchema schema = new UiSchema(this.service.getDomainClass());
        return schema;
    }

    /**
     * Find the other end of a ToOne relationship
     * @param pk the root entity ID
     * @param fieldInfo the member/relation name
     * @return the single related entity, if any
     * @see PersistableModelService#findRelatedSingle(java.io.Serializable, com.restdude.mdd.registry.FieldInfo)
     */
    protected PersistableModel findRelatedSingle(PK pk, FieldInfo fieldInfo) {
        PersistableModel resource = this.service.findRelatedSingle(pk, fieldInfo);
        return resource;
    }

    /**
     * Find a page of results matching the other end of a ToMany relationship
     * @param pk the root entity ID
     * @param pageable the page config
     * @param fieldInfo the member/relation name
     * @return the page of results, may be <code>null</code>
     * @see PersistableModelService#findRelatedPaginated(java.lang.Class, org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
     */
    protected <M extends PersistableModel> ParamsAwarePageImpl<M> findRelatedPaginated(PK pk, Pageable pageable, FieldInfo fieldInfo) {
        ParamsAwarePageImpl<M> page = null;
        Optional<String> reverseFieldName = fieldInfo.getReverseFieldName();
        if(reverseFieldName.isPresent()) {
            Map<String, String[]> params = request.getParameterMap();
            Map<String, String[]> implicitCriteria = new HashMap<>();
            implicitCriteria.put(reverseFieldName.get(), new String[]{pk.toString()});

            ModelInfo relatedModelInfo = fieldInfo.getRelatedModelInfo();
            // optionally create a query specification
            Specification<M> spec = RsqlUtils.buildtSpecification(relatedModelInfo, this.service.getConversionService(), params, implicitCriteria, PARAMS_IGNORE_FOR_CRITERIA);
            // get the page of related children
            Page<M> tmp = this.service.findRelatedPaginated(relatedModelInfo.getModelType(), spec, pageable);
            page = new ParamsAwarePageImpl<M>(params, tmp.getContent(), pageable, tmp.getTotalElements());
        }
        else{
            throw new IllegalArgumentException("Related field info has no reverse field name");
        }
        return page;
    }

    protected ParamsAwarePageImpl<T> findPaginated(Pageable pageable, Map<String, String[]> implicitCriteria) {
        // TODO: add support for query dialects: RequestParam MultiValueMap<String, String> httpParams
        /*
        SearchRequest searchRequest = new SearchRequest(
                //...
        );
        LOGGER.debug("findResourcesPaginated, searchRequest: {}", searchRequest);
         */

        Map<String, String[]> params = request.getParameterMap();
        // optionally create a query specification
        Specification<T> spec = RsqlUtils.buildtSpecification(this.getModelInfo(), this.service.getConversionService(), params, implicitCriteria, PARAMS_IGNORE_FOR_CRITERIA);
        Page<T> tmp = this.service.findPaginated(spec, pageable);

        return new ParamsAwarePageImpl<T>(params, tmp.getContent(), pageable, tmp.getTotalElements());
    }

        /*
    protected Page<T> getPage(Pageable pageable, Map<String, String[]> paramsMap, Map<String, String> implicitCriteria) {

        // add implicit criteria?
        Map<String, String[]> parameters = null;
        if (applyImplicitPredicates) {
            LOGGER.debug("Adding implicit predicates");
            parameters = new HashMap<String, String[]>();
            parameters.putAll(paramsMap);
            CurrentPrincipalField predicate = (CurrentPrincipalField) this.service.getDomainClass().getAnnotation(CurrentPrincipalField.class);
            if (predicate != null) {
                UserDetailsModel principal = this.service.getPrincipal();
                // TODO
                String[] excludeRoles = predicate.ignoreforRoles();
                boolean skipPredicate = this.hasAnyRole(predicate.ignoreforRoles());
                if (!skipPredicate) {
                    String pk = principal != null ? principal.getPk() : "ANONYMOUS";
                    String[] val = {pk};
                    LOGGER.debug("Adding implicit predicate, name: {}, pathFragment: {}", predicate.value(), pk);
                    parameters.put(predicate.value(), val);
                }

            }
        } else {
            LOGGER.debug("Skipping implicit predicates");
            parameters = paramsMap;
        }


        return this.service.findPaginated(null, pageable);

    }
        */



    protected boolean hasAnyRole(String... roles) {
        boolean hasOne = false;
        for (int i = 0; i < roles.length; i++) {
            if (request.isUserInRole(roles[i])) {
                hasOne = true;
                break;
            }
        }
        return hasOne;
    }

    protected void applyCurrentPrincipal(T resource) {
        Field[] fields = FieldUtils.getFieldsWithAnnotation(this.service.getDomainClass(), CurrentPrincipal.class);
        //ApplyPrincipalUse predicate = this.service.getDomainClass().getAnnotation(CurrentPrincipalField.class);
        if (fields.length > 0) {
            UserDetails principal = this.service.getPrincipal();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                CurrentPrincipal applyRule = field.getAnnotation(CurrentPrincipal.class);

                // if property is not already set
                try {
                    if (PropertyUtils.getProperty(resource, field.getName()) == null) {
                        boolean skipApply = this.hasAnyRole(applyRule.ignoreforRoles());
                        // if role is not ignored
                        if (!skipApply) {
                            String pk = principal != null ? principal.getPk() : null;
                            if (pk != null) {
                                User user = new User();
                                user.setPk(pk);
                                LOGGER.debug("Applying principal to field: {}, pathFragment: {}", pk, field.getName());
                                PropertyUtils.setProperty(resource, field.getName(), user);
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to apply CurrentPrincipal annotation", e);
                }

            }


        }
    }
}
