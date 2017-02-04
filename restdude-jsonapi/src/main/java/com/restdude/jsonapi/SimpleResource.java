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

import com.fasterxml.jackson.annotation.*;
import com.restdude.domain.base.model.JsonApiResource;
import org.springframework.hateoas.ResourceSupport;
import com.restdude.domain.base.model.CalipsoPersistable;

import java.io.Serializable;

/**
 * A model wrapper that allows serializing as a Resource according to JSON API  1.1
 *
 * @see <a href="http://jsonapi.org/format/#document-resource-objects">JSON API Resources</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "attributes", "relationships", "links", "meta" })
public class SimpleResource<T extends CalipsoPersistable<PK>, PK extends Serializable> extends ResourceSupport implements Serializable, JsonApiResource<T, PK> {

    private T attributes;

    public SimpleResource(T attributesModel){
        this.attributes = attributesModel;
    }

    @Override
    @JsonGetter("id")
    public PK getPk(){
return attributes != null ? attributes.getPk() : null;
    }

    @Override
    @JsonGetter("type")
    public String getJsonApiType(){
        return attributes != null ? attributes.getClass().getName() : null;
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(value = { "pk", "links" })
    public T getAttributes() {
        return attributes;
    }

    @Override
    @JsonProperty
    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }
}
