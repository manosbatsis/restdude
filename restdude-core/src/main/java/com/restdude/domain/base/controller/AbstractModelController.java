package com.restdude.domain.base.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.CurrentPrincipal;
import com.restdude.mdd.annotation.CurrentPrincipalField;
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
 * @see IRestController
 */
public abstract class AbstractModelController<T extends CalipsoPersistable<ID>, ID extends Serializable, S extends ModelService<T, ID>> implements
		ModelController<T, ID, S>, IRestController<T, ID>, BuildPageable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelController.class);


	@Autowired
	protected HttpServletRequest request;

	protected S service;

	@Inject
	public void setService(S service) {
		this.service = service;
	}


	/**
	 * @see com.restdude.domain.base.controller.ModelController#getService()
	 */
	@Override
	public S getService(){
		return this.service;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Create a new resource")
	@JsonView(AbstractSystemUuidPersistable.ItemView.class)
	public T create(@RequestBody T resource) {
		applyCurrentPrincipal(resource);
		return this.service.create(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "Update a resource")
	@JsonView(AbstractSystemUuidPersistable.ItemView.class)
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
	@ResponseBody
	@ApiOperation(value = "Patch (partially update) a resource", notes = "Partial updates will apply all given properties (ignoring null values) to the persisted entity.")
	@JsonView(AbstractSystemUuidPersistable.ItemView.class)
	public T patch(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id, @RequestBody T resource) {
		applyCurrentPrincipal(resource);
		resource.setId(id);
		return this.service.patch(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET, params = "page=no", produces = "application/json")
	@ResponseBody
	@ApiOperation(value = "Get the full collection of resources (no paging or criteria)", notes = "Find all resources, and return the full collection (i.e. VS a page of the total results)")
	public Iterable<T> findAll() {
		return service.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Search for resources (paginated).", notes = "Find all resources matching the given criteria and return a paginated collection."
			+ " Besides the predefined paging properties (page, size, properties, direction) all serialized member names "
			+ "of the resource are supported as search criteria in the form of HTTP URL parameters.")
	public Page<T> findPaginated(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "properties", required = false, defaultValue = "id") String sort,
			@RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction) {
		boolean applyCurrentPrincipalIdPredicate = true;

		if(BooleanUtils.toBoolean(request.getParameter("skipCurrentPrincipalIdPredicate"))){
			applyCurrentPrincipalIdPredicate = false;
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Skipping CurrentPrincipalField");
			}
		}

		return findPaginated(page, size, sort, direction, request.getParameterMap(), applyCurrentPrincipalIdPredicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Find by id", notes = "Find a resource by it's identifier")
	@JsonView(AbstractSystemUuidPersistable.ItemView.class)
	public T findById(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id) {
		T resource = this.service.findById(id);
		if (resource == null) {
			throw new NotFoundException();
		}
		return resource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(params = "ids", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Search by ids", notes = "Find the set of resources matching the given identifiers.")
	public Iterable<T> findByIds(@RequestParam(value = "ids[]") Set<ID> ids) {
		Assert.notNull(ids, "ids list cannot be null");
		return this.service.findByIds(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete a resource", notes = "Delete a resource by its identifier. ", httpMethod = "DELETE")
	public void delete(@ApiParam(name = "id", required = true, value = "string") @PathVariable ID id) {
		T resource = this.findById(id);
		this.service.delete(resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RequestMapping(method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete all resources")
	public void delete() {
		this.service.deleteAllWithCascade();
	}

	@RequestMapping(value = "uischema", produces = {"application/json"}, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
	public UiSchema getSchema() {
		UiSchema schema = new UiSchema(this.service.getDomainClass());
		return schema;
	}

	@RequestMapping(produces = {"application/json"}, method = RequestMethod.OPTIONS)
	@ResponseBody
	@ApiOperation(value = "Get UI schema", notes = "Get the UI achema for the controller entity type, including fields, use-cases etc.")
	public UiSchema getSchemas() {
		return this.getSchema();
	}

	protected Page<T> findPaginated(Integer page, Integer size, String sort,
									String direction, Map<String, String[]> paramsMap, boolean applyImplicitPredicates) {

		// add implicit criteria?
		Map<String, String[]> parameters = null;
		if(applyImplicitPredicates){
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Adding implicit predicates");
			}
			parameters = new HashMap<String, String[]>();
			parameters.putAll(paramsMap);
			CurrentPrincipalField predicate = (CurrentPrincipalField) this.service.getDomainClass().getAnnotation(CurrentPrincipalField.class);
			if(predicate != null){
				ICalipsoUserDetails principal = this.service.getPrincipal();
				String[] excludeRoles = predicate.ignoreforRoles();
				boolean skipPredicate = this.hasAnyRoles(predicate.ignoreforRoles());
				if(!skipPredicate){
					String id = principal != null ? principal.getId() : "ANONYMOUS";
					String[] val = {id};
					if(LOGGER.isDebugEnabled()){
						LOGGER.debug("Adding implicit predicate, name: " + predicate.value() + ", value: " + id);
					}
					parameters.put(predicate.value(), val);
				}

			}
		}
		else{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Skipping implicit predicates");
			}
			parameters = paramsMap;
		}

		Pageable pageable = buildPageable(page, size, sort, direction, parameters);
		return this.service.findAll(pageable);

	}

	protected boolean hasAnyRoles(String[] roles) {
		boolean skipPredicate = false;
		for(int i = 0; i < roles.length; i++) {
			if (request.isUserInRole(roles[i])){
				skipPredicate = true;
				break;
			}
		}
		return skipPredicate;
	}

	protected void applyCurrentPrincipal(T resource) {
		Field[] fields = FieldUtils.getFieldsWithAnnotation(this.service.getDomainClass(), CurrentPrincipal.class);
		//ApplyPrincipalUse predicate = this.service.getDomainClass().getAnnotation(CurrentPrincipalField.class);
		if(fields.length > 0){
			ICalipsoUserDetails principal = this.service.getPrincipal();
			for(int i = 0; i < fields.length; i++){
				Field field = fields[i];
				CurrentPrincipal applyRule = field.getAnnotation(CurrentPrincipal.class);

				// if property is not already set
				try {
					if(PropertyUtils.getProperty(resource, field.getName()) == null){
						boolean skipApply = this.hasAnyRoles(applyRule.ignoreforRoles());
						// if role is not ignored
						if(!skipApply){
							String id = principal != null ? principal.getId() : null;
							if(id != null){
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
