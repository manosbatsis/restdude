/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.base.service;


import com.restdude.domain.base.model.AbstractAssignedIdPersistableResource;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Provides SCRUD and utility operations for {@link T} entities
 * @author manos
 *
 * @param <T> the entity type
 * @param <PK> the entity PK type
 */
@Service
public interface AbstractAssignedIdModelService<T extends AbstractAssignedIdPersistableResource<PK>, PK extends Serializable>
        extends ModelService<T, PK> {

    /**
     * Return the entity matching the PK of the given resource if any, or the newly persisted instance otherwise
     * @param resource
     * @return
     */
    public T findOrCreate(@P("resource") T resource);

}