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
import com.restdude.hypermedia.hateoas.ModelResource;
import com.restdude.hypermedia.hateoas.ModelResources;
import com.restdude.hypermedia.hateoas.PagedModelResources;
import com.restdude.hypermedia.jsonapi.JsonApiModelCollectionDocument;
import com.restdude.hypermedia.jsonapi.JsonApiModelDocument;
import com.restdude.hypermedia.jsonapi.util.JsonApiUtils;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.model.Model;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.RawJson;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.uischema.model.UiSchema;
import com.restdude.util.exception.http.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
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


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonPost(@RequestBody T model) {
        model = super.create(model);
        return toHateoasResource(model);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new JSON API Resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> jsonApiPost(@NonNull @RequestBody JsonApiModelDocument<T, PK> document) {

        // unwrap the submitted model and save
        T model = toModel(document);
        model = super.create(model);

        // repackage and return as a JSON API Document
        return this.toDocument(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonPut(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T model) {
        model = super.update(pk, model);
        return toHateoasResource(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public Resource<T> plainJsonPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T model) {
        model = super.patch(pk, model);
        return toHateoasResource(model);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
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

    @RequestMapping(method = RequestMethod.GET, params = "page=no")
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    @ModelDrivenPreAuth
    public ModelResources<T> plainJsonGetAll() {
        return toHateoasResources(super.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, params = "page=no", consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    public JsonApiModelCollectionDocument jsonApiGetAll() {

        // obtain result models
        Iterable<T> models = super.findAll();

        // repackage and return as a JSON API Document
        return this.toDocument(models);
    }

    //@Override
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated collection."
            + "Predefined paging properties are _pn (page number), _ps (page size) and sort. All serialized member names "
            + "of the resource are supported as search criteria in the form of HTTP URL parameters.")
    @ModelDrivenPreAuth
    public PagedModelResources<T> plainJsonGetPage(
            @ApiParam(name = "filter", value = "The RSQL/FIQL query to use. Simply URL param based search will be used if missing.")
            @RequestParam(value = "filter", required = false) String filter,
            @ApiParam(name = "_pn", value = "The page number", allowableValues = "range[0, infinity]", defaultValue = "0")
            @RequestParam(value = "_pn", required = false, defaultValue = "0") Integer page,
            @ApiParam(name = "_ps", value = "The page size", allowableValues = "range[1, infinity]", defaultValue = "10")
            @RequestParam(value = "_ps", required = false, defaultValue = "10") Integer size,
            @ApiParam(name = "sort", value = "Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise")
            @RequestParam(value = "sort", required = false, defaultValue = "pk") String sort) {

        Pageable pageable = PageableUtil.buildPageable(page, size, sort);
        return this.toHateoasPagedResources(super.findPaginated(pageable));
    }

    @RequestMapping(method = RequestMethod.GET, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated JSON API Document.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> jsonApiGetPage(
            @ApiParam(name = "filter", value = "The RSQL/FIQL query to use. Simply URL param based search will be used if missing.")
            @RequestParam(value = "filter", required = false) String filter,
            @ApiParam(name = "page[number]", value = "The page number", allowableValues = "range[0, infinity]", defaultValue = "0")
            @RequestParam(value = "page[number]", required = false, defaultValue = "0") Integer page,
            @ApiParam(name = "page[size]", value = "The page size", allowableValues = "range[1, infinity]", defaultValue = "10")
            @RequestParam(value = "page[size]", required = false, defaultValue = "10") Integer size,
            @ApiParam(name = "sort", value = "Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise")
            @RequestParam(value = "sort", required = false, defaultValue = "pk") String sort) {
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
        Pageable pageable = PageableUtil.buildPageable(page, size, sort);
        return toDocument(super.findPaginated(pageable));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.GET)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public ModelResource<T> plainJsonGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        LOGGER.debug("plainJsonGetById, pk: {}, model type: {}", pk, this.service.getDomainClass());
        T model = super.findById(pk);
        if (model == null) {
            throw new NotFoundException();
        }
        return toHateoasResource(model);
    }

    @RequestMapping(value = "{pk}/{relationName}", method = RequestMethod.GET)
    @ApiOperation(value = "Find related by root pk", notes = "Find the related resource for the given relation name and identifier")
    @JsonView(Model.ItemView.class)
    public Resource<PersistableModel> plainJsonGetRelatedEntityByOwnId(
            @ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk,
            @ApiParam(name = "relationName", required = true, value = "string") @PathVariable String relationName) {

        FieldInfo fieldInfo = this.getModelInfo().getField(relationName);
        PersistableModel related = super.findRelatedEntityByOwnId(pk, fieldInfo);

        return toHateoasResource(related, (Class<PersistableModel>)fieldInfo.getFieldType());
    }


    @RequestMapping(value = "{pk}/{relationName}", method = RequestMethod.GET, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Find related by root pk", notes = "Find the related resource for the given relation name and identifier")
    @JsonView(Model.ItemView.class)
    public JsonApiModelDocument jsonApiGetRelatedEntityByOwnId(
            @ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk,
            @ApiParam(name = "relationName", required = true, value = "string") @PathVariable String relationName) {

        FieldInfo fieldInfo = this.getModelInfo().getField(relationName);
        PersistableModel related = super.findRelatedEntityByOwnId(pk, fieldInfo);

        return toDocument(related, (Class<PersistableModel>)fieldInfo.getFieldType());
    }


    @RequestMapping(value = "{pk}", method = RequestMethod.GET, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelDocument<T, PK> jsonApiGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {

        return toDocument(super.findById(pk));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public ModelResources<T> plainJsonGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return this.toHateoasResources(super.findByIds(pks));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public JsonApiModelCollectionDocument<T, PK> jsonApiGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return toDocument(super.findByIds(pks));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void plainJsonDelete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        super.delete(pk);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void jsonApiDelete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        super.delete(pk);
    }

    @RequestMapping(value = "jsonschema", method = RequestMethod.GET, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get JSON Schema", notes = "Get the JSON Schema for the controller entity type")
    public RawJson plainJsonGetJsonSchema() throws JsonProcessingException {
        return super.getJsonSchema();
    }

    @RequestMapping(value = "uischema", method = RequestMethod.GET, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
    @Deprecated
    public UiSchema plainJsonGetUiSchema() {
        return super.getUiSchema();
    }

}
