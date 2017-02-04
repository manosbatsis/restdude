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
package com.restdude.domain.base.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.restdude.domain.error.model.Error;
import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A Document according to JSON API 1.1
 *
 * @param <T> the JSON API Resource model type
 * @param <PK> the JSON API Resource model key type
 */
@JsonPropertyOrder({ "data", "errors", "meta", "jsonapi", "links", "included" })
public interface JsonApiResourceDocument<T extends CalipsoPersistable<PK>, PK extends Serializable> extends JsonApiDocument<JsonApiResource<T, PK>, T, PK> {

    JsonApiResource<T, PK> getData();

    void setData(JsonApiResource<T, PK> data);

    Iterable<JsonApiResource> getIncluded();

    void setIncluded(Iterable<JsonApiResource> included);

    Iterable<Error> getErrors();

    void setErrors(Iterable<Error> errors);

    Map<String, Serializable> getMeta();

    void setMeta(Map<String, Serializable> meta);

    void add(Link link);

    void add(Iterable<Link> links);

    void add(Link... links);

    boolean hasLinks();

    boolean hasLink(String rel);

    List<Link> getLinks();

    void removeLinks();

    Link getLink(String rel);
}
