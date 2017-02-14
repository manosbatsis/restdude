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
import com.restdude.jsonapi.JsonApiModelCollectionDocument;
import com.restdude.jsonapi.JsonApiModelDocument;
import com.restdude.jsonapi.JsonApiResource;
import com.restdude.jsonapi.util.JsonApiModelBasedDocumentBuilder;
import com.restdude.mdd.annotation.model.CurrentPrincipal;
import com.restdude.mdd.annotation.model.CurrentPrincipalField;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.RawJson;
import com.restdude.mdd.model.UserDetailsModel;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.mdd.registry.ModelInfoRegistry;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.uischema.model.UiSchema;
import lombok.NonNull;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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
    protected Resource<T> toHateoasResource(@NonNull T model) {
        Resource<T> resource = new Resource<>(model);
        if (this.isResourceSupport && model.getPk() != null) {
            String[] params = {"/api/rest/" + this.getModelInfo() + "/" + model.getPk()};
            resource.add(this.entityLinks.linkFor(this.modelType, params).withSelfRel());
        }
        return resource;
    }

    /**
     * Wrap the given models in a {@link Resources} and add {@link org.springframework.hateoas.Link}s
     *
     * @param models
     */
    protected Resources<T> toHateoasResources(@NonNull Iterable<T> models) {
        Resources<T> resources = new Resources<>(models);
        return resources;
    }

    /**
     * Convert the given {@link Page} to a {@link PagedResources} object and add {@link org.springframework.hateoas.Link}s
     *
     * @param page
     */
    protected PagedResources<T> toHateoasPagedResources(@NonNull Page<T> page) {
        // long size, long number, long totalElements, long totalPages
        PagedResources.PageMetadata meta = new PagedResources.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedResources<T> pagedResources = new PagedResources<T>(page.getContent(), meta);
        return pagedResources;
    }

    /**
     * Wrap the given model in a JSON API Document
     * @param model the model to wrap
     * @return
     */
    protected JsonApiModelDocument<T, PK> toDocument(T model) {
        return new JsonApiModelBasedDocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(model)
                .buildModelDocument();
    }

    /**
     * Wrap the given collection of models in a JSON API Document
     * @param models the models to wrap
     * @return
     */
    protected JsonApiModelCollectionDocument<T, PK> toDocument(Collection<T> models) {
        return new JsonApiModelBasedDocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(models)
                .buildModelCollectionDocument();
    }

    /**
     * Wrap the given iterable of models of models in a JSON API Document
     * @param models the models to wrap
     * @return
     */
    protected JsonApiModelCollectionDocument<T, PK> toDocument(Iterable<T> models) {
        return new JsonApiModelBasedDocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(models)
                .buildModelCollectionDocument();
    }

    /**
     * Wrap the given {@link Page} of models in a JSON API Document
     * @param page the page to wrap
     * @return
     */
    protected JsonApiModelCollectionDocument<T, PK> toDocument(Page<T> page) {
        return new JsonApiModelBasedDocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(page)
                .buildModelCollectionDocument();
    }

    /**
     * Unwrap the single model given as a JSON API Document
     * @param document
     * @return
     */
    protected T toModel(@NonNull @RequestBody JsonApiModelDocument<T, PK> document) {
        T entity = null;
        JsonApiResource<T, PK> resource = document.getData();
        if(resource != null ){
            entity = resource.getAttributes();
            entity.setPk(resource.getIdentifier());
        }
        return entity;
    }


    protected T post(@NonNull T resource) {
        applyCurrentPrincipal(resource);
        return this.service.create(resource);
    }


    protected T put(PK pk, T resource) {
        Assert.notNull(pk, "pk cannot be null");
        resource.setPk(pk);
        applyCurrentPrincipal(resource);
        resource = this.service.update(resource);
        toHateoasResource(resource);
        return resource;
    }

    protected T patch(PK pk, T resource) {
        applyCurrentPrincipal(resource);
        resource.setPk(pk);
        resource = this.service.patch(resource);
        toHateoasResource(resource);
        return resource;
    }

    protected Iterable<T> getAll() {
        return service.findAll();
    }


    protected Page<T> getPage(
            Integer page,
            Integer size,
            String sort,
            String direction) {
        // TODO: add support for query dialects: RequestParam MultiValueMap<String, String> httpParams
        /*
        MultivaluedHashMap<String, String> queryParams = new MultivaluedHashMap<>();
        for(String key : httpParams.keySet()){
            queryParams.put(key, httpParams.get(key));
        }
        SearchRequest searchRequest = new SearchRequest(
                request.getServletPath(),
                this.mmdelInfoRegistry.getEntityDictionary(),
                queryParams,
                true
        );
        LOGGER.debug("findResourcesPaginated, searchRequest: {}", searchRequest);
         */

        boolean applyCurrentPrincipalIdPredicate = true;

        if (BooleanUtils.toBoolean(request.getParameter("skipCurrentPrincipalIdPredicate"))) {
            applyCurrentPrincipalIdPredicate = false;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Skipping CurrentPrincipalField");
            }
        }

        return buildPage(page, size, sort, direction, request.getParameterMap(), applyCurrentPrincipalIdPredicate);
    }


    protected T getById(PK pk) {
        LOGGER.debug("plainJsonGetById, pk: {}, model type: {}", pk, this.service.getDomainClass());
        T resource = this.service.findById(pk);
        return resource;
    }

    protected Iterable<T> getByIds(@NonNull Set<PK> pks) {
        Assert.notNull(pks, "pks list cannot be null");
        return this.service.findByIds(pks);
    }

    protected void delete(@NonNull PK pk) {
        T resource = this.getById(pk);
        this.service.delete(resource);
    }

    protected void delete() {
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

    protected Page<T> buildPage(Integer page, Integer size, String sort,
                                String direction, Map<String, String[]> paramsMap, boolean applyImplicitPredicates) {

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

        Pageable pageable = PageableUtil.buildPageable(page, size, sort, direction, parameters);
        return this.service.findPaginated(pageable);

    }


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
            UserDetailsModel principal = this.service.getPrincipal();
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
