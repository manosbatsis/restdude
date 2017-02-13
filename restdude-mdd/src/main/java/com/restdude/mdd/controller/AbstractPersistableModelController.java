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
import com.restdude.jsonapi.support.JsonApiUtils;
import com.restdude.mdd.model.UserDetailsModel;
import com.restdude.jsonapi.binding.DocumentBuilder;
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
 * If your want to identity resources by a slug (human readable identifier), your should override findById() method with for example :
 * <p/>
 * <pre>
 * <code>
 * {@literal @}Override
 * public Sample findById({@literal @}PathVariable String pk) {
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

    //@Override
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new resource")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T create(@NonNull @RequestBody T resource) {
        applyCurrentPrincipal(resource);
        addResourceLinks(resource);
        return this.service.create(resource);
    }

    //@Override
    @RequestMapping(method = RequestMethod.POST, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new JSON API Resource")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> createResource(@NonNull @RequestBody JsonApiModelDocument<T, PK> document) {
        T entity = null;

        // obtain submitted entity model
        JsonApiResource<T, PK> resource = document.getData();
        if(resource != null ){
            entity = resource.getAttributes();
            entity.setPk(resource.getIdentifier());
        }

        // persist
        entity = this.create(entity);

        // repackage as a JSON API Document
        document = new DocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(entity)
                .buildModelDocument();

        // return
        return document;
    }

    //@Override
    @RequestMapping(value = "{pk}", method = RequestMethod.PUT, produces = "application/json")
    @ApiOperation(value = "Update a resource")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T update(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T resource) {
        Assert.notNull(pk, "pk cannot be null");
        resource.setPk(pk);
        applyCurrentPrincipal(resource);
        resource = this.service.update(resource);
        addResourceLinks(resource);
        return resource;
    }

    //@Override
    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, produces = "application/json")
    @ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T patch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T resource) {
        applyCurrentPrincipal(resource);
        resource.setPk(pk);
        resource = this.service.patch(resource);
        addResourceLinks(resource);
        return resource;
    }

    //@Override
        @RequestMapping(method = RequestMethod.GET, params = "page=no", produces = "application/json")
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public Iterable<T> findAll() {
        return service.findAll();
    }

    //@Override
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated collection."
            + " Besides the predefined paging properties (page, size, properties, direction) all serialized member names "
            + "of the resource are supported as search criteria in the form of HTTP URL parameters.")
    @ModelDrivenPreAuth
    public Page<T> findPaginated(
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

        return findPaginated(page, size, sort, direction, request.getParameterMap(), applyCurrentPrincipalIdPredicate);
    }

    @RequestMapping(method = RequestMethod.GET, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated JSON API Document.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> findResourcesPaginated(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "properties", required = false, defaultValue = "pk") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {
        Page<T> resultsPage = this.findPaginated(page, size, sort, direction);

        JsonApiModelCollectionDocument document = new DocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withPage(resultsPage)
                .buildModelCollectionDocument();

        return document;
    }

    //@Override
    @RequestMapping(value = "{pk}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    @ModelDrivenPreAuth
    public T findById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        LOGGER.debug("findById, pk: {}, model type: {}", pk, this.service.getDomainClass());
        T resource = this.service.findById(pk);
        if (resource == null) {
            throw new NotFoundException();
        }
        addResourceLinks(resource);
        return resource;
    }

    //@Override
    @RequestMapping(value = "{pk}", method = RequestMethod.GET, consumes = JsonApiUtils.JSONAPI_CONTENT_TYPE, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
    public JsonApiModelDocument<T, PK> findResourceById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        T resource = this.findById(pk);
        if (resource == null) {
            throw new NotFoundException();
        }

        JsonApiModelDocument document = new DocumentBuilder<T, PK>(this.getModelInfo().getUriComponent())
                .withData(resource)
                .buildModelDocument();

        return document;
    }


    //@Override
    @RequestMapping(params = "pks", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public Iterable<T> findByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        Assert.notNull(pks, "pks list cannot be null");
        return this.service.findByIds(pks);
    }

    //@Override
    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void delete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        T resource = this.findById(pk);
        this.service.delete(resource);
    }

    //@Override
    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    @ApiOperation(value = "Delete all resources")
    @ModelDrivenPreAuth
    public void delete() {
        this.service.deleteAllWithCascade();
    }

    //@Override
    @RequestMapping(value = "jsonschema", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get JSON Schema", notes = "Get the JSON Schema for the controller entity type")
    public RawJson getJsonSchema() throws JsonProcessingException {
        JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper, config);

        JsonNode jsonSchema = generator.generateJsonSchema(this.getService().getDomainClass());

        String jsonSchemaAsString = objectMapper.writeValueAsString(jsonSchema);
        return new RawJson(jsonSchemaAsString);
    }

    @RequestMapping(value = "uischema", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema getSchema() {
        UiSchema schema = new UiSchema(this.service.getDomainClass());
        return schema;
    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = "application/json")
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema getSchemas() {
        return this.getSchema();
    }

    protected Page<T> findPaginated(Integer page, Integer size, String sort,
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
                boolean skipPredicate = this.hasAnyRoles(predicate.ignoreforRoles());
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


    protected boolean hasAnyRoles(String[] roles) {
        boolean skipPredicate = false;
        for (int i = 0; i < roles.length; i++) {
            if (request.isUserInRole(roles[i])) {
                skipPredicate = true;
                break;
            }
        }
        return skipPredicate;
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
                        boolean skipApply = this.hasAnyRoles(applyRule.ignoreforRoles());
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
