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
import com.restdude.domain.Model;
import com.restdude.domain.PersistableModel;
import com.restdude.domain.RawJson;
import com.restdude.hypermedia.hateoas.ModelResource;
import com.restdude.hypermedia.hateoas.ModelResources;
import com.restdude.hypermedia.hateoas.PagedModelResources;
import com.restdude.hypermedia.jsonapi.JsonApiDocument;
import com.restdude.hypermedia.jsonapi.JsonApiModelResourceCollectionDocument;
import com.restdude.hypermedia.jsonapi.JsonApiModelResourceDocument;
import com.restdude.hypermedia.util.HypermediaUtils;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.mdd.uischema.model.UiSchema;
import com.restdude.mdd.util.ParamsAwarePageImpl;
import com.restdude.rsql.RsqlUtils;
import com.restdude.util.HttpUtil;
import com.restdude.util.exception.http.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    @RequestMapping(method = RequestMethod.POST, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new JSON API Resource")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelResourceDocument<T, PK> jsonApiPost(@NonNull @RequestBody JsonApiModelResourceDocument<T, PK> document) {

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

    @RequestMapping(value = "{pk}", method = RequestMethod.PATCH, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Patch (partially plainJsonPut) a resource given as a JSON API Document", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelResourceDocument<T, PK> jsonApiPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody JsonApiModelResourceDocument<T, PK> document) {

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

    @RequestMapping(method = RequestMethod.GET, params = "page=no", consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
    public JsonApiModelResourceCollectionDocument jsonApiGetAll() {

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
            @ApiParam(name = PARAM_FILTER, value = "The RSQL/FIQL query to use. Simply URL param based search will be used if missing.")
            @RequestParam(value = PARAM_FILTER, required = false) String filter,
            @ApiParam(name = PARAM_PAGE_NUMBER, value = "The page number", allowableValues = "range[0, infinity]", defaultValue = "0")
            @RequestParam(value = PARAM_PAGE_NUMBER, required = false, defaultValue = "0") Integer page,
            @ApiParam(name = PARAM_PAGE_SIZE, value = "The page size", allowableValues = "range[1, infinity]", defaultValue = "10")
            @RequestParam(value = PARAM_PAGE_SIZE, required = false, defaultValue = "10") Integer size,
            @ApiParam(name = PARAM_SORT, value = "Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise")
            @RequestParam(value = PARAM_SORT, required = false, defaultValue = "pk") String sort) {
        LOGGER.debug("plainJsonGetPage");
        Pageable pageable = PageableUtil.buildPageable(page, size, sort);
        return this.toHateoasPagedResources(super.findPaginated(pageable, null), "_pn");
    }

    @RequestMapping(method = RequestMethod.GET, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated JSON API Document.")
    @ModelDrivenPreAuth
    public JsonApiModelResourceCollectionDocument<T, PK> jsonApiGetPage(
            @ApiParam(name = PARAM_FILTER, value = "The RSQL/FIQL query to use. Simply URL param based search will be used if missing.")
            @RequestParam(value = PARAM_FILTER, required = false) String filter,
            @ApiParam(name = PARAM_JSONAPI_PAGE_NUMBER, value = "The page number", allowableValues = "range[0, infinity]", defaultValue = "0")
            @RequestParam(value = PARAM_JSONAPI_PAGE_NUMBER, required = false, defaultValue = "0") Integer page,
            @ApiParam(name = PARAM_JSONAPI_PAGE_SIZE, value = "The page size", allowableValues = "range[1, infinity]", defaultValue = "10")
            @RequestParam(value = PARAM_JSONAPI_PAGE_SIZE, required = false, defaultValue = "10") Integer size,
            @ApiParam(name = PARAM_SORT, value = "Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise")
            @RequestParam(value = PARAM_SORT, required = false, defaultValue = "pk") String sort) {

        LOGGER.debug("jsonApiGetPage");
        Pageable pageable = PageableUtil.buildPageable(page, size, sort);
        return toPageDocument(super.findPaginated(pageable, null));
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

    /**
     * GET has the same effect to both member and relationship endpoints
     */
    @RequestMapping(value = {"{pk}/{relationName}", "{pk}/relationships/{relationName}"}, method = RequestMethod.GET)
    @ApiOperation(value = "Find related by root pk", notes = "Find the related resource for the given relation name and identifier")
    @JsonView(Model.ItemView.class)
    public ResponseEntity plainJsonGetRelated(
            @ApiParam(name = PARAM_PK, required = true, value = "string") @PathVariable PK pk,
            @ApiParam(name = PARAM_RELATION_NAME, required = true, value = "string") @PathVariable String relationName,
            @ApiParam(name = PARAM_FILTER, value = "The RSQL/FIQL query to use. Simply URL param based search will be used if missing.")
            @RequestParam(value = PARAM_FILTER, required = false) String filter,
            @ApiParam(name = PARAM_PAGE_NUMBER, value = "The page number", allowableValues = "range[0, infinity]", defaultValue = "0")
            @RequestParam(value = PARAM_PAGE_NUMBER, required = false, defaultValue = "0") Integer page,
            @ApiParam(name = PARAM_PAGE_SIZE, value = "The page size", allowableValues = "range[1, infinity]", defaultValue = "10")
            @RequestParam(value = PARAM_PAGE_SIZE, required = false, defaultValue = "10") Integer size,
            @ApiParam(name = PARAM_SORT, value = "Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise")
            @RequestParam(value = PARAM_SORT, required = false, defaultValue = "pk") String sort) {

        // get the field info for the relation, if any
        FieldInfo fieldInfo = this.getModelInfo().getField(relationName);

        // throw error if not valid or linkable relationship
        if(fieldInfo == null || !fieldInfo.isLinkableResource()){
            throw new IllegalArgumentException("Invalid relationship: " + relationName);
        }

        // use response entity to accommodate different return types
        ResponseEntity responseEntity = null;

        // if ToOne
        if(fieldInfo.isToOne()){
            PersistableModel related = this.findRelatedSingle(pk, fieldInfo);
            // if found
            Resource res = HypermediaUtils.toHateoasResource(related, fieldInfo.getRelatedModelInfo());
            responseEntity = new ResponseEntity(res, HttpStatus.OK);
        }
        else if(fieldInfo.isOneToMany()){
            Pageable pageable = PageableUtil.buildPageable(page, size, sort);
            ParamsAwarePageImpl resultsPage = this.findRelatedPaginated(pk, pageable, fieldInfo);
            PagedResources resources = this.toHateoasPagedResources(resultsPage, "_pn");
            responseEntity = new ResponseEntity(resources, HttpStatus.OK);

        }


        return responseEntity;
    }

    /**
     * GET has the same effect to both member and relationship endpoints
     */
    @RequestMapping(value = {"{pk}/{relationName}", "{pk}/relationships/{relationName}"}, method = RequestMethod.GET, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Find related by root pk", notes = "Find the related resource for the given relation name and identifier")
    @JsonView(Model.ItemView.class)
    public JsonApiDocument jsonApiGetRelated(
            @ApiParam(name = PARAM_PK, required = true, value = "string") @PathVariable PK pk,
            @ApiParam(name = PARAM_RELATION_NAME, required = true, value = "string") @PathVariable String relationName,
            @ApiParam(name = PARAM_FILTER, value = "The RSQL/FIQL query to use. Simply URL param based search will be used if missing.")
            @RequestParam(value = PARAM_FILTER, required = false) String filter,
            @ApiParam(name = PARAM_JSONAPI_PAGE_NUMBER, value = "The page number", allowableValues = "range[0, infinity]", defaultValue = "0")
            @RequestParam(value = PARAM_JSONAPI_PAGE_NUMBER, required = false, defaultValue = "0") Integer page,
            @ApiParam(name = PARAM_JSONAPI_PAGE_SIZE, value = "The page size", allowableValues = "range[1, infinity]", defaultValue = "10")
            @RequestParam(value = PARAM_JSONAPI_PAGE_SIZE, required = false, defaultValue = "10") Integer size,
            @ApiParam(name = PARAM_SORT, value = "Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise")
            @RequestParam(value = PARAM_SORT, required = false, defaultValue = "pk") String sort) {

        // get the field info for the relation, if any
        FieldInfo fieldInfo = this.getModelInfo().getField(relationName);

        // throw error if not valid or linkable relationship
        if(fieldInfo == null || !fieldInfo.isLinkableResource()){
            throw new IllegalArgumentException("Invalid relationship: " + relationName);
        }

        // use JSON API Document to accommodate different return types
        JsonApiDocument document = null;

        // if ToOne
        if(fieldInfo.isToOne()){
            PersistableModel related = this.findRelatedSingle(pk, fieldInfo);
            // if found
            if(related != null) {
                document = HypermediaUtils.toDocument(related, fieldInfo.getRelatedModelInfo());
            }
        }
        else if(fieldInfo.isOneToMany()){
            Pageable pageable = PageableUtil.buildPageable(page, size, sort);
            ParamsAwarePageImpl resultsPage = this.findRelatedPaginated(pk, pageable, fieldInfo);
            document = this.toPageDocument(resultsPage, fieldInfo.getRelatedModelInfo(), "page[number]");

        }


        return document;
    }


    @RequestMapping(value = "{pk}", method = RequestMethod.GET, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Find by pk", notes = "Find a resource by it's identifier")
    @JsonView(Model.ItemView.class)
    @ModelDrivenPreAuth
    public JsonApiModelResourceDocument<T, PK> jsonApiGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {

        return toDocument(super.findById(pk));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public ModelResources<T> plainJsonGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return this.toHateoasResources(super.findByIds(pks));
    }

    @RequestMapping(params = "pks", method = RequestMethod.GET, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
    @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
    @ModelDrivenPreAuth
    public JsonApiModelResourceCollectionDocument<T, PK> jsonApiGetByIds(@RequestParam(value = "pks[]") Set<PK> pks) {
        return toDocument(super.findByIds(pks));
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
    @ModelDrivenPreAuth
    public void plainJsonDelete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk) {
        super.delete(pk);
    }

    @RequestMapping(value = "{pk}", method = RequestMethod.DELETE, consumes = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON)
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

    @RequestMapping(method = RequestMethod.OPTIONS, consumes = {HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, MimeTypeUtils.APPLICATION_JSON_VALUE}, produces = {HypermediaUtils.MIME_APPLICATION_VND_PLUS_JSON, MimeTypeUtils.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Get CORS headers", notes = "Get the CORS headers for the given path")
    public void options(HttpServletResponse response) {
        response.setHeader(HttpUtil.ACESS_CONTROL_CREDENTIALS_NAME, "true");
        response.setHeader(HttpUtil.ACESS_CONTROL_ORIGIN_NAME, "http://localhost:9000");
        response.setHeader(HttpUtil.ACESS_CONTROL_METHODS_NAME, "GET, OPTIONS, POST, PUT, DELETE");
        response.setHeader(HttpUtil.ACESS_CONTROL_HEADERS_NAME, "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader(HttpUtil.ACESS_CONTROL_MAX_AGE_NAME, "3600");

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

}
