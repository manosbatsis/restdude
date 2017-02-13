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
package com.restdude.jsonapi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.restdude.jsonapi.support.SimpleModelDocument;
import com.restdude.mdd.model.Model;

import java.io.Serializable;

/**
 * A Document that may contain up to a single model-based Resource according to JSON API 1.1.
 * Configured for deserialization as a {@link SimpleModelDocument}
 *
 * @param <T> the JSON API Resource model type
 * @param <PK> the JSON API Resource model key type
 *
 */
@JsonDeserialize(as=SimpleModelDocument.class)
public interface JsonApiModelDocument<T extends Model<PK>, PK extends Serializable> extends JsonApiResourceDocument<JsonApiResource<T, PK>, T, PK> {

}
