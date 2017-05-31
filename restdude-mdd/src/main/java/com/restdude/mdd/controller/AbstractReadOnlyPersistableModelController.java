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

import com.restdude.domain.PersistableModel;
import com.restdude.mdd.service.PersistableModelService;
import com.restdude.util.exception.http.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Base class for read-only model controllers, i.e. with no support for HTTP PUT, PATCH or DELETE.
 */
public abstract class AbstractReadOnlyPersistableModelController<T extends PersistableModel<PK>, PK extends Serializable, S extends PersistableModelService<T, PK>>
        extends AbstractNoDeletePersistableModelController<T, PK, S> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReadOnlyPersistableModelController.class);

	@Override
    public T create(T resource) {
        throw new NotImplementedException("Method is unsupported.");
    }

	@Override
    public T update(PK id, T resource) {
        throw new NotImplementedException("Method is unsupported.");
    }

	@Override
    public T patch(PK id, T resource) {
        throw new NotImplementedException("Method is unsupported.");
    }

}
