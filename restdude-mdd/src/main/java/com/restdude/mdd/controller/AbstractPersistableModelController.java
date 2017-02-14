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
import com.restdude.jsonapi.*;
import com.restdude.jsonapi.util.JsonApiUtils;
import com.restdude.mdd.model.Model;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.RawJson;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.uischema.model.UiSchema;
import com.restdude.util.exception.http.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
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
public class AbstractPersistableModelController<T extends PersistableModel<PK>, PK extends Serializable, S extends PersistableModelService<T, PK>>
        extends AbstractModelServiceBackedController<T, PK, S> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistableModelController.class);


    @RequestMapping(method = RequestMethod.POST, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonPost(@RequestBody T model) {
        model = super.post(model);
        return toHateoasResource(model);
    }

    @RequestMapping(method = RequestMethod.POST, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new JSON API Resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> jsonApiPost(@NonNull @RequestBody JsonApiModelDocument<T, PK> document) {

        // unwrap the submitted model and save
        T model = toModel(document);
        model = super.post(model);

        // repackage and return as a JSON API Document
        return this.toDocument(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PUT, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update a resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonPut(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T model) {
        model = super.put(pk, model);
        return toHateoasResource(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T model) {
        model = super.patch(pk, model);
        return toHateoasResource(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Patch (partially plainJsonPut) a resource given as a JSON API Document", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> jsonApiPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody JsonApiModelDocument<T, PK> document) {

        // unwrap the submitted model and save changes
        T model = toModel(document);
        model = super.patch(pk, model);

        // repackage and return as a JSON API Document
        return this.toDocument(model);
    }

    @RequestMapping(method = RequestMethod.GET, params = "page=no", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public Resources<T> plainJsonGetAll() {
        return toHateoasResources(super.getAll());
    }

    @RequestMapping(method = RequestMethod.GET, params = "page=no", produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument jsonApiGetAll() {

        // obtain result models
        Iterable<T> models = super.getAll();

        // repackage and return as a JSON API Document
        return this.toDocument(models);
    }

    //@Override
    @RequestMapping(method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated collection."
            + " Besides the predefined paging properties (page, size, properties, direction) all serialized member names "
            + "of the resource are supported as search criteria in the form of HTTP URL parameters.")
    @ModelDrivenPreAuth
    public PagedResources<T> plainJsonGetPage(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "properties", required = false, defaultValue = "pk") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {

        return this.toHateoasPagedResources(super.getPage(page, size, sort, direction));
    }

    @RequestMapping(method = RequestMethod.GET, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated JSON API Document.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> jsonApiGetPage(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "properties", required = false, defaultValue = "pk") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {

        return toDocument(super.getPage(page, size, sort, direction));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        LOGGER.debug("plainJsonGetById, pk: {}, model type: {}", pk, this.service.getDomainClass());
        T model = super.getById(pk);
        if (model == null) {
            throw new NotFoundException();
        }
        return toHateoasResource(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.GET, consumes = JsonApiUtils.JSONAPI_CONTENT_TYPE, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(Model.ItemView.class)
    public JsonApiModelDocument<T, PK> jsonApiGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {

        return toDocument(super.getById(pk));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public Resources<T> plainJsonGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return this.toHateoasResources(super.getByIds(pks));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET, consumes = JsonApiUtils.JSONAPI_CONTENT_TYPE, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> jsonApiGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return toDocument(super.getByIds(pks));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void plainJsonDelete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        super.delete(pk);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE, consumes = JsonApiUtils.JSONAPI_CONTENT_TYPE, produces = JsonApiUtils.JSONAPI_CONTENT_TYPE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void jsonApiDelete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        super.delete(pk);
    }

    @RequestMapping(value = "jsonschema", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get JSON Schema", notes = "Get the JSON Schema for the controller entity type")
    public RawJson plainJsonGetJsonSchema() throws JsonProcessingException {
        return super.getJsonSchema();
    }

    @RequestMapping(value = "uischema", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema plainJsonGetUiSchema() {
        return super.getUiSchema();
    }

}
