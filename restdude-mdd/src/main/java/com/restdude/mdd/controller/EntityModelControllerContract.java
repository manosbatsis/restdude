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
import com.restdude.hypermedia.jsonapi.JsonApiDocument;
import com.restdude.hypermedia.jsonapi.JsonApiResource;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.model.RawJson;
import com.restdude.mdd.service.PersistableModelService;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Set;


/**
 * REST controller interface for exposing entity model services to HTTP
 *
 * <p>Assumes 3-tiered architecture i.e. Repository, Service and Controller. </p>
 *
 * @param <T>  The model type
 * @param <PK> The model identifier type
 * @param <S>  The service class
 */
public abstract class EntityModelControllerContract<T extends PersistableModel<PK>, PK extends Serializable, S extends PersistableModelService<T, PK>> {


    /**
     * Inject the appropriate EntityService
     *
     * <pre>
     * {@code
     *
     * @Inject
     * void setService(S service);{
     *     this.service = service;
     * }
     *
     * }
     * </pre>
     *
     * @param service
     */
    abstract void setService(S service);

    /**
     * Create a new resource<br />
     * REST webservice published : POST /
     *
     * <pre>
     * {@code
     * 
     * @RequestMapping(method = RequestMethod.POST)
     * @ResponseStatus(HttpStatus.CREATED)
     * @ApiOperation(value = "Create a new resource")
     * @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
     * @ModelDrivenPreAuth
     * T plainJsonPost(@RequestBody T resource){
     *     ///
     * }
     *
     * }
     * </pre>
     *
     * @param resource The resource to create
     * @return the persisted state of the resource
     */
    abstract T create(T resource);

    /**
     * Update an existing resource<br/>
     * REST webservice published : PUT /{pk}
     *
     * <pre>
     * {@code
     * 
     * @RequestMapping(value = "{pk}", method = RequestMethod.PUT)
     * @ApiOperation(value = "Update a resource")
     * @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
     * @ModelDrivenPreAuth
     * T plainJsonPut(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T resource){
     *     ///
     * }
     *
     * }
     * </pre>
     *
     *
     * @param pk       The identifier of the resource to update, usually a Long or String identifier
     * @param resource The updated resource to persist
     * @return the persisted state of the resource
     */
    abstract T update(PK pk, T resource);

    /**
     * Perform a partial update of an existing resource<br/>
     * REST webservice published : PATCH /{pk}
     *
     * <pre>
     * {@code
     * 
     * @RequestMapping(value = "{pk}", method = RequestMethod.PATCH)
     * @ApiOperation(value = "Patch (partially update) a resource", notes = "....")
     * @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
     * @ModelDrivenPreAuth
     * T plainJsonPatch(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk, @RequestBody T resource){
     *     ///
     * }
     * 
     * }
     * </pre>
     *
     *
     * @param pk       The identifier of the resource to update, usually a Long or String identifier
     * @param resource The resource of which non-null values are to be applied
     * @return the persisted state of the resource
     */
    abstract T patch(PK pk, T resource);

    /**
     * Find all resources, and return the full collection (plain list not paginated)<br/>
     * REST webservice published : GET /?page=no
     *
     * <pre>
     * {@code
     * 
     * @RequestMapping(method = RequestMethod.GET, params = "page=no")
     * @ApiOperation(value = "Get the full collection of resources", notes = "...")
     * @ModelDrivenPreAuth
     * Iterable<T> plainJsonGetAll(){
     *     //...
     * }
     * 
     * }
     * </pre>
     *
     *
     * @return OK http status code if the request has been correctly processed, with the list of all resource enclosed in the body.
     * Be careful, this list should be big since it will return ALL resources. In this case, consider using paginated findAll method instead.
     */
    abstract Iterable<T> findAll();

    /**
     * Find all resources, and return a paginated and optionaly sorted collection
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(method = RequestMethod.GET)
     * @ApiOperation(value = "Search for resources (paginated).", notes = "...")
     * @ModelDrivenPreAuth
     * Page<T> buildPage(
     *      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
     *      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
     *      @RequestParam(value = "properties", required = false, defaultValue = "pk") String sort,
     *      @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction){
     *     //...
     * }
     *
     * }
     * </pre>
     *
     *
     * @param page       Page number starting from 0. default to 0
     * @param size       Number of resources by pages. default to 10
     * @param direction  Optional sort direction, could be "ASC" or "DESC"
     * @param direction Ordered list of comma separated property names used for sorting results. At least one property should be provided if direction is specified
     * @return OK http status code if the request has been correctly processed, with the a paginated collection of all resource enclosed in the body.
     */
    abstract Page<T> findPaginated(Integer page, Integer size, String sort, String direction);

    /**
     * Find a resource by its identifier<br/>
     * REST webservice published : GET /{pk}
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(value = "{pk}", method = RequestMethod.GET)
     * @ApiOperation(value = "Find by identifier", notes = "Find a resource by it's identifier")
     * @JsonView(AbstractSystemUuidPersistableResource.ItemView.class)
     * @ModelDrivenPreAuth
     * T plainJsonGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk){
     *     //...
     * }
     *
     * }
     * </pre>
     *
     *
     * @param pk The identifier of the resouce to find
     * @return OK http status code if the request has been correctly processed, with resource found enclosed in the body
     */
    abstract T findById(PK pk);

    /**
     * Find a resource by its identifier<br/> and obtain as a JSON-API Document
     * REST webservice published : GET /{pk}
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(value = "{pk}", method = RequestMethod.GET, consumes = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON, produces = JsonApiUtils.MIME_APPLICATION_VND_PLUS_JSON)
     * JsonApiDocument<JsonApiResource<T, PK>, T, PK> jsonApiGetById(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk){
     *     //...
     * }
     *
     * }
     * </pre>
     *
     *
     * @param pk The identifier of the resouce to find
     * @return OK http status code if the request has been correctly processed, with resource found enclosed in the body
     */
    abstract JsonApiDocument<JsonApiResource<T, PK>, T, PK> findResourceById(PK pk);

    /**
     * Find multiple resources by their identifiers<br/>
     * REST webservice published : GET /?ids[]=
     * <p/>
     * example : /?pks[]=1&pks[]=2&pks[]=3
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(params = "pks", method = RequestMethod.GET)
     * @ApiOperation(value = "Search by pks", notes = "Find the set of resources matching the given identifiers.")
     * @ModelDrivenPreAuth
     * Iterable<T> plainJsonGetByIds(@RequestParam(value = "pks[]") Set<PK> pks){
     *     //...
     * }
     *
     * }
     * </pre>
     *
     *
     * @param pks List of ids to retrieve
     * @return OK http status code with list of retrieved resources. Not found resources are ignored:
     * no Exception thrown. List is empty if no resource found with any of the given ids.
     */
    abstract Iterable<T> findByIds(Set<PK> pks);

    /**
     * Delete a resource by its identifier<br />
     * REST webservice published : DELETE /{pk}<br />
     * Return No Content http status code if the request has been correctly processed
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(value = "{pk}", method = RequestMethod.DELETE)
     * @ResponseStatus(HttpStatus.NO_CONTENT)
     * @ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
     * @ModelDrivenPreAuth
     * void delete(@ApiParam(name = "pk", required = true, value = "string") @PathVariable PK pk){
     *     //...
     * }
     *
     * }
     * </pre>
     *
     *
     * @param pk The identifier of the resource to delete
     */
    abstract void delete(PK pk);

    /**
     * Delete all resources<br/>
     * REST webservice published : DELETE /<br/>
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(method = RequestMethod.DELETE)
     * @ApiOperation(value = "Delete all resources")
     * @ModelDrivenPreAuth
     * void delete(){
     *     //...
     * }
     *
     * }
     * </pre>
     *
     * Return No Content http status code if the request has been correctly processed
     */
    abstract void delete();

    abstract RawJson getJsonSchema() throws JsonProcessingException;
}
