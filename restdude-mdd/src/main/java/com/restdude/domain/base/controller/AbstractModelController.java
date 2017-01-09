/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.model.RawJson;
import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.CurrentPrincipal;
import com.restdude.mdd.annotation.CurrentPrincipalField;
import com.restdude.mdd.annotation.ModelDrivenPreAuth;
import com.restdude.mdd.uischema.model.UiSchema;
import com.restdude.util.exception.http.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
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
 * <p>Default implementation uses "id" field (usually a Long) in order to identify resources in web request.
 * If your want to identity resources by a slug (human readable identifier), your should override findById() method with for example :
 * <p/>
 * <pre>
 * <code>
 * {@literal @}Override
 * public Sample findById({@literal @}PathVariable String id) {
 * Sample sample = this.service.findByName(id);
 * if (sample == null) {
 * throw new NotFoundException();
 * }
 * return sample;
 * }
 * </code>
 * </pre>
 *
 * @param <T>  Your resource class to manage, maybe an entity or DTO class
 * @param <ID> Resource id type, usually Long or String
 * @param <S>  The service class
 */

public class AbstractModelController<T extends CalipsoPersistable<ID>, ID extends Serializable, S extends ModelService<T, ID>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected HttpServletRequest request;

    protected S service;

    @Inject
    public void setService(S service) {
        this.service = service;
    }


    public S getService() {
        return this.service;
    }


    /**
     * Create a new resource<br />
     * REST webservice published : POST /
     *
     * @param resource The resource to create
     * @return CREATED http status code if the request has been correctly processed, with updated resource enclosed in the body, usually with and additional identifier automatically created by the database
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new resource")
    @JsonView(AbstractSystemUuidPersistable.ItemView.class)
    @ModelDrivenPreAuth
    public T create(@RequestBody T resource) {
        applyCurrentPrincipal(resource);
        return this.service.create(resource);
    }

    /**
     * Update an existing resource<br/>
     * REST webservice published : PUT /{id}
     *
     * @param id       The identifier of the resource to update, usually a Long or String identifier. It is explicitely provided in order to handle cases where the identifier could be changed.
     * @param resource The resource to update
     * @return OK http status code if the request has been correctly processed, with the updated resource enclosed in the body
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a resource")
    @JsonView(AbstractSystemUuidPersistable.ItemView.class)
    @ModelDrivenPreAuth
    public T update(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id, @RequestBody T resource) {
        Assert.notNull(id, "id cannot be null");
        resource.setId(id);
        applyCurrentPrincipal(resource);

        return this.service.update(resource);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(AbstractSystemUuidPersistable.ItemView.class)
    @ModelDrivenPreAuth
    public T patch(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id, @RequestBody T resource) {
        applyCurrentPrincipal(resource);
        resource.setId(id);
        return this.service.patch(resource);
    }

    /**
     * Find all resources, and return the full collection (plain list not paginated)<br/>
     * REST webservice published : GET /?page=no
     *
     * @return OK http status code if the request has been correctly processed, with the list of all resource enclosed in the body.
     * Be careful, this list should be big since it will return ALL resources. In this case, consider using paginated findAll method instead.
     */
    @RequestMapping(method = RequestMethod.GET, params = "page=no", produces = "application/json")
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public Iterable<T> findAll() {
        return service.findAll();
    }

    /**
     * Find all resources, and return a paginated and optionaly sorted collection
     *
     * @param page       Page number starting from 0. default to 0
     * @param size       Number of resources by pages. default to 10
     * @param direction  Optional sort direction, could be "ASC" or "DESC"
     * @param direction Ordered list of comma separated property names used for sorting results. At least one property should be provided if direction is specified
     * @return OK http status code if the request has been correctly processed, with the a paginated collection of all resource enclosed in the body.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated collection."
            + " Besides the predefined paging properties (page, size, properties, direction) all serialized member names "
            + "of the resource are supported as search criteria in the form of HTTP URL parameters.")
    @ModelDrivenPreAuth
    public Page<T> findPaginated(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "properties", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {
        boolean applyCurrentPrincipalIdPredicate = true;

        if (BooleanUtils.toBoolean(request.getParameter("skipCurrentPrincipalIdPredicate"))) {
            applyCurrentPrincipalIdPredicate = false;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Skipping CurrentPrincipalField");
            }
        }

        return findPaginated(page, size, sort, direction, request.getParameterMap(), applyCurrentPrincipalIdPredicate);
    }

    /**
     * Find a resource by its identifier<br/>
     * REST webservice published : GET /{id}
     *
     * @param id The identifier of the resouce to find
     * @return OK http status code if the request has been correctly processed, with resource found enclosed in the body
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find by id", notes = "Find a resource by it's identifier")
    @JsonView(AbstractSystemUuidPersistable.ItemView.class)
    @ModelDrivenPreAuth
    public T findById(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id) {
        T resource = this.service.findById(id);
        if (resource == null) {
            throw new NotFoundException();
        }
        return resource;
    }

    /**
     * Find multiple resources by their identifiers<br/>
     * REST webservice published : GET /?ids[]=
     * <p/>
     * example : /?ids[]=1&ids[]=2&ids[]=3
     *
     * @param ids List of ids to retrieve
     * @return OK http status code with list of retrieved resources. Not found resources are ignored:
     * no Exception thrown. List is empty if no resource found with any of the given ids.
     */
    @RequestMapping(params = "ids", method = RequestMethod.GET)
    @ApiOperation(value = "Search by ids", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public Iterable<T> findByIds(@RequestParam(value = "ids[]") Set<ID> ids) {
        Assert.notNull(ids, "ids list cannot be null");
        return this.service.findByIds(ids);
    }

    /**
     * Delete a resource by its identifier<br />
     * REST webservice published : DELETE /{id}<br />
     * Return No Content http status code if the request has been correctly processed
     *
     * @param id The identifier of the resource to delete
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void delete(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id) {
        T resource = this.findById(id);
        this.service.delete(resource);
    }

    /**
     * Delete all resources<br/>
     * REST webservice published : DELETE /<br/>
     * Return No Content http status code if the request has been correctly processed
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete all resources")
    @ModelDrivenPreAuth
    public void delete() {
        this.service.deleteAllWithCascade();
    }

    @RequestMapping(value = "jsonschema", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "Get JSON Schema", notes = "Get the JSON Schema for the controller entity type")
    public RawJson getJsonSchema() throws JsonProcessingException {
        JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper, config);

        JsonNode jsonSchema = generator.generateJsonSchema(this.getService().getDomainClass());

        String jsonSchemaAsString = objectMapper.writeValueAsString(jsonSchema);
        return new RawJson(jsonSchemaAsString);
    }

    @RequestMapping(value = "uischema", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema getSchema() {
        UiSchema schema = new UiSchema(this.service.getDomainClass());
        return schema;
    }

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.OPTIONS)
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding implicit predicates");
            }
            parameters = new HashMap<String, String[]>();
            parameters.putAll(paramsMap);
            CurrentPrincipalField predicate = (CurrentPrincipalField) this.service.getDomainClass().getAnnotation(CurrentPrincipalField.class);
            if (predicate != null) {
                ICalipsoUserDetails principal = this.service.getPrincipal();
                String[] excludeRoles = predicate.ignoreforRoles();
                boolean skipPredicate = this.hasAnyRoles(predicate.ignoreforRoles());
                if (!skipPredicate) {
                    String id = principal != null ? principal.getId() : "ANONYMOUS";
                    String[] val = {id};
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Adding implicit predicate, name: " + predicate.value() + ", value: " + id);
                    }
                    parameters.put(predicate.value(), val);
                }

            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Skipping implicit predicates");
            }
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
            ICalipsoUserDetails principal = this.service.getPrincipal();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                CurrentPrincipal applyRule = field.getAnnotation(CurrentPrincipal.class);

                // if property is not already set
                try {
                    if (PropertyUtils.getProperty(resource, field.getName()) == null) {
                        boolean skipApply = this.hasAnyRoles(applyRule.ignoreforRoles());
                        // if role is not ignored
                        if (!skipApply) {
                            String id = principal != null ? principal.getId() : null;
                            if (id != null) {
                                User user = new User();
                                user.setId(id);
                                LOGGER.debug("Applying principal to field: {}, value: {}", id, field.getName());
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
