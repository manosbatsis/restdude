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


import com.fasterxml.jackson.annotation.JsonInclude;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.model.JsonApiResource;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * {@value #CLASS_DESCRIPTION}
 *
 * @see SimpleDocument
 * @see <a href="http://jsonapi.org/format/upcoming/#document-structure">JSON API Documents</a>
 * @param <T> the JSON API Resource model type
 * @param <PK> the JSON API Resource model key type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Collection Document (JSON-API)", description = SimpleDocument.CLASS_DESCRIPTION)
public class SimpleCollectionDocument<T extends CalipsoPersistable<PK>, PK extends Serializable> extends AbstractJsonApiDocument<JsonApiResource<T,PK>[], T, PK>{

    public static final String CLASS_DESCRIPTION = "A JSON API Document that may contain multiple Resource models";

    private JsonApiResource<T,PK>[] data;

    public SimpleCollectionDocument(){
        this.setData(new JsonApiResource[]{});
    }

    public SimpleCollectionDocument(SimpleResource[] data) {
        super(data);
    }


    @Override
    public JsonApiResource<T,PK>[] getData() {
        return this.data;
    }

    @Override
    public void setData(JsonApiResource<T,PK>[] data) {
        this.data = data;
    }
}
