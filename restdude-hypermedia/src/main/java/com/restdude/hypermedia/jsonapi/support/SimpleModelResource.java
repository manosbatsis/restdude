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

import com.fasterxml.jackson.annotation.*;
import com.restdude.hypermedia.jsonapi.JsonApiResource;
import com.restdude.mdd.model.Model;
import lombok.NonNull;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

/**
 * A model wrapper that allows serializing as a Resource according to JSON API  1.1
 *
 * @see <a href="http://jsonapi.org/format/#document-resource-objects">JSON API Resources</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "type", "attributes", "relationships", "links", "meta" })
public class SimpleModelResource<T extends Model<PK>, PK extends Serializable> extends ResourceSupport implements Serializable, JsonApiResource<T, PK> {

    @JsonProperty("id")
    private PK identifier;
    private String type;
    private T attributes;

    private SimpleModelResource(){
    }

    public SimpleModelResource(@NonNull T attributesModel, @NonNull String type){
        this.attributes = attributesModel;
        this.identifier = attributesModel.getPk();
        this.type = type;
    }

    @JsonCreator
    public SimpleModelResource(
            @JsonProperty("id") PK identifier,
            @NonNull @JsonProperty("attributes") T attributes,
            @NonNull @JsonProperty("type") String type){
        this.identifier = identifier;
        this.attributes = attributes;
        this.type = type;
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(value = { "identifier", "links" })
    public T getAttributes() {
        return attributes;
    }

    @Override
    @JsonProperty
    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }

    @Override
    public PK getIdentifier() {
        return identifier;
    }

    public void setIdentifier(PK identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
