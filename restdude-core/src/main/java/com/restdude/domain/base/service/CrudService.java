/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.service;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Set;

/**
 * CRUD Service interface.
 *
 * @param <T>
 *            Your resource POJO to manage, maybe an entity or DTO class
 * @param <ID>
 *            Resource id type, usually Long or String
 */
public interface CrudService<T extends CalipsoPersistable<ID>, ID extends Serializable> {


    /**
     * Create a new resource.
     *
     * @param resource Resource to create
     * @return new resource
     */
    T create(T resource);

    /**
     * Override to handle post-create
     *
     * @param resource The created resource
     */
    void postCreate(T resource);

    /**
     * Update an existing resource.
     *
     * @param resource Resource to update
     * @return resource updated
     */
    T update(T resource);


    /**
     * Partially update an existing resource.
     *
     * @param resource Resource to update
     * @return resource updated
     */
    T patch(T resource);

    /**
     * Delete an existing resource.
     *
     * @param resource Resource to delete
     */
    void delete(T resource);

    /**
     * Delete an existing resource.
     *
     * @param id Resource id
     */
    void delete(ID id);

    /**
     * Delete all existing resource. Do not use cascade remove (not a choice -> JPA specs)
     */
    void deleteAll();

    /**
     * Delete all existing resource, including linked entities with cascade delete
     */
    void deleteAllWithCascade();

    /**
     * Find resource by id.
     *
     * @param id Resource id
     * @return resource
     */
    T findById(ID id);

    /**
     * Find resources by their ids.
     *
     * @param ids Resource ids
     * @return a list of retrieved resources, empty if no resource found
     */
    Iterable<T> findByIds(Set<ID> ids);

    /**
     * Find all resources.
     *
     * @return a list of all resources.
     */
    Iterable<T> findAll();

    /**
     * Find resources page-by-page
     *
     * @param pageRequest page request
     * @return resources
     */
    Page<T> findPaginated(Pageable pageRequest);

    /**
     * Count all resources.
     *
     * @return number of resources
     */
    Long count();

    /**
     * @see ModelRepository#validateConstraints(com.restdude.domain.base.model.CalipsoPersistable)
     */
    Set<ConstraintViolation<T>> validateConstraints(T resource);

}
