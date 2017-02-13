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

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.restdude.jsonapi.*;
import com.restdude.jsonapi.util.JsonApiUtils;
import com.restdude.mdd.model.UserDetailsModel;
import com.restdude.jsonapi.util.JsonApiModelBasedDocumentBuilder;
import com.restdude.mdd.model.AbstractSystemUuidPersistableResource;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.RawJson;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.CurrentPrincipal;
import com.restdude.mdd.annotation.model.CurrentPrincipalField;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.registry.ModelInfoRegistry;
import com.restdude.mdd.uischema.model.UiSchema;
import com.restdude.util.exception.http.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
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
public class AbstractPersistableModelController<T extends PersistableModel<PK>, PK extends Serializable, S extends PersistableModelService<T, PK>> implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistableModelController.class);

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
     * Add HATEOAS links
     *
     * @param resource
     */
    protected void addResourceLinks(@RequestBody T resource) {
        String[] params = {"/api/rest/" + this.getModelInfo() + "/" + resource.getPk()};
        if (this.isResourceSupport && resource.getPk() != null) {
            ResourceSupport resourceSupport = (ResourceSupport) resource;
            resourceSupport.add(this.entityLinks.linkFor(this.modelType, params).withSelfRel());
        }
    }

    /**
     * Get the ModelInfo for this Controller's Model type
     */
    protected ModelInfo getModelInfo() {
        if(this.modelInfo == null){
            this.modelInfo = this.mmdelInfoRegistry.getEntryFor(this.modelType);
            LOGGER.debug("addResourceLinks, modelInfo: {}", this.modelInfo);
        }
        return this.modelInfo;
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

    @RequestMapping(method = RequestMethod.POST, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new resource")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T plainJsonPost(@NonNull @RequestBody T resource) {
        applyCurrentPrincipal(resource);
        addResourceLinks(resource);
        return this.service.create(resource);
    }

    @RequestMapping(method = RequestMethod.POST, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new JSON API Resource")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> jsonApiPost(@NonNull @RequestBody JsonApiModelDocument<T, PK> document) {

        // unwrap the submitted model and save
        T model = toModel(document);
        model = this.plainJsonPost(model);

        // repackage and return as a JSON API Document
        return this.toDocument(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PUT, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update a resource")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T plainJsonPut(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T resource) {
        Assert.notNull(pk, "pk cannot be null");
        resource.setPk(pk);
        applyCurrentPrincipal(resource);
        resource = this.service.update(resource);
        addResourceLinks(resource);
        return resource;
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T plainJsonPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T resource) {
        applyCurrentPrincipal(resource);
        resource.setPk(pk);
        resource = this.service.patch(resource);
        addResourceLinks(resource);
        return resource;
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Patch (partially plainJsonPut) a resource given as a JSON API Document", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> jsonApiPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody JsonApiModelDocument<T, PK> document) {

        // unwrap the submitted model and save changes
        T model = toModel(document);
        model = this.plainJsonPatch(pk, model);

        // repackage and return as a JSON API Document
        return this.toDocument(model);
    }

    @RequestMapping(method = RequestMethod.GET, params = "page=no", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public Iterable<T> plainJsonGetAll() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, params = "page=no", produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument jsonApiGetAll() {

        // obtain result models
        Iterable<T> models = this.plainJsonGetAll();

        // repackage and return as a JSON API Document
        return this.toDocument(models);
    }

    //@Override
    @RequestMapping(method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated collection."
            + " Besides the predefined paging properties (page, size, properties, direction) all serialized member names "
            + "of the resource are supported as search criteria in the form of HTTP URL parameters.")
    @ModelDrivenPreAuth
    public Page<T> plainJsonGetPage(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "properties", required = false, defaultValue = "pk") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {
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

        return plainJsonGetPage(page, size, sort, direction, request.getParameterMap(), applyCurrentPrincipalIdPredicate);
    }

    @RequestMapping(method = RequestMethod.GET, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated JSON API Document.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> jsonApiGetPage(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "properties", required = false, defaultValue = "pk") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {

        return toDocument(this.plainJsonGetPage(page, size, sort, direction));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T plainJsonGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        LOGGER.debug("plainJsonGetById, pk: {}, model type: {}", pk, this.service.getDomainClass());
        T resource = this.service.findById(pk);
        if (resource == null) {
            throw new NotFoundException();
        }
        addResourceLinks(resource);
        return resource;
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.GET, consumes = JsonApiUtils.JSONAPI_CONTENT_TYPE, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    public JsonApiModelDocument<T, PK> jsonApiGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {

        return toDocument(this.plainJsonGetById(pk));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public Iterable<T> plainJsonGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        Assert.notNull(pks, "pks list cannot be null");
        return this.service.findByIds(pks);
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET, consumes = JsonApiUtils.JSONAPI_CONTENT_TYPE, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> jsonApiGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return toDocument(this.plainJsonGetByIds(pks));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE, consumes = {JsonApiUtils.JSONAPI_CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE}, produces = {JsonApiUtils.JSONAPI_CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void delete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        T resource = this.plainJsonGetById(pk);
        this.service.delete(resource);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = {JsonApiUtils.JSONAPI_CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE}, produces = {JsonApiUtils.JSONAPI_CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all resources")
    @ModelDrivenPreAuth
    public void delete() {
        this.service.deleteAllWithCascade();
    }

    @RequestMapping(value = "jsonschema", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get JSON Schema", notes = "Get the JSON Schema for the controller entity type")
    public RawJson getJsonSchema() throws JsonProcessingException {
        JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper, config);

        JsonNode jsonSchema = generator.generateJsonSchema(this.getService().getDomainClass());

        String jsonSchemaAsString = objectMapper.writeValueAsString(jsonSchema);
        return new RawJson(jsonSchemaAsString);
    }

    @RequestMapping(value = "uischema", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema getSchema() {
        UiSchema schema = new UiSchema(this.service.getDomainClass());
        return schema;
    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema getSchemas() {
        return this.getSchema();
    }

    protected Page<T> plainJsonGetPage(Integer page, Integer size, String sort,
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
