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
package com.restdude.mdd.specifications;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.geography.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public abstract class AbstractPredicateFactory<T extends Serializable> implements IPredicateFactory<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPredicateFactory.class);

	public AbstractPredicateFactory() {
	}

	public Path<T> getPath(Root<Country> root, String propertyName, Class<?> fieldType) {
		Path<?> path = null;
		if (propertyName.contains(".")) {
			String[] pathSteps = propertyName.split("\\.");

			String step = pathSteps[0];
			LOGGER.debug("getPath, step: {}", step);
			path = pathSteps.length == 1
					? root.<T>get(step)
					: root.<CalipsoPersistable>get(step);
			LOGGER.debug("getPath0, added path step: {}, result path: {}", step, path);

			for (int i = 1; i < pathSteps.length - 2; i++) {
				step = pathSteps[i];
				path = path.<CalipsoPersistable>get(step);
				LOGGER.debug("getPath{}, added path step: {}, result path: {}", i, step, path);
			}

			if (pathSteps.length > 1) {
				step = pathSteps[pathSteps.length - 2];
				path = path.<T>get(step);
				LOGGER.debug("getPath{}, added path step: {}, result path: {}", (pathSteps.length - 2), step, path);
			}


		} else {
			LOGGER.debug("getPath, single-step propertyName: {}", propertyName);
			path = root.<T>get(propertyName);
			LOGGER.debug("getPath, single-step propertyName: {}, result path: {}", propertyName, path);
		}
		return (Path<T>) path;
	}
}