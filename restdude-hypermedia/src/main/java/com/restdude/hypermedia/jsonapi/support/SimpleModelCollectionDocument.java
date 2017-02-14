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
package com.restdude.hypermedia.jsonapi.support;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.restdude.hypermedia.jsonapi.JsonApiModelCollectionDocument;
import com.restdude.hypermedia.jsonapi.JsonApiResource;
import com.restdude.mdd.model.Model;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Collection;

/**
 * {@value #CLASS_DESCRIPTION}
 *
 * @see SimpleModelDocument
 * @see <a href="http://jsonapi.org/format/upcoming/#document-structure">JSON API Documents</a>
 * @param <T> the JSON API Resource model type
 * @param <PK> the JSON API Resource model key type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Collection Document (JSON-API)", description = SimpleModelDocument.CLASS_DESCRIPTION)
public class SimpleModelCollectionDocument<T extends Model<PK>, PK extends Serializable>
        extends AbstractJsonApiDocument<Collection<JsonApiResource<T, PK>>, T, PK>
        implements JsonApiModelCollectionDocument<T, PK> {

    public static final String CLASS_DESCRIPTION = "A JSON API Document that may contain multiple model-based Resources";

    private Collection<JsonApiResource<T,PK>> data;

    public SimpleModelCollectionDocument(){super();}

    public SimpleModelCollectionDocument(Collection<JsonApiResource<T, PK>> data) {
        super(data);
    }

}
