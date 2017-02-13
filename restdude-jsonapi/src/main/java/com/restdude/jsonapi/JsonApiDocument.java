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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.restdude.mdd.model.Model;
import com.restdude.mdd.model.ErrorModel;
import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A Document according to JSON API 1.1
 *
 * @param <T> the JSON API Resource model type
 * @param <PK> the JSON API Resource model key type
 *
 * @see <a href="http://jsonapi.org/format/upcoming/#document-structure">JSON API Resources</a>
 *
 */
@JsonPropertyOrder({ "data", "errors", "meta", "jsonapi", "links", "included" })
public interface JsonApiDocument<D extends Object, T extends Model<PK>, PK extends Serializable> extends LinksModel {

    /**
     * Get the Document data, i.e. embedded resource(s)
     * @return
     */
    D getData();

    /**
     * Set the Document data, i.e. embedded resource(s)
     * @return
     */
    void setData(D data);

    /**
     * Get the included resources
     * @return
     */
    Collection<JsonApiResource> getIncluded();

    /**
     * Get the included resources
     * @return
     */
    void setIncluded(Collection<JsonApiResource> included);

    /**
     * Get the errors resulting from processing the request
     * @return
     */
    Collection<ErrorModel> getErrors();

    /**
     * Set the errors resulting from processing the request
     * @return
     */
    void setErrors(Collection<ErrorModel> errors);

    /**
     * Get the associated metadata
     * @return
     */
    Map<String, Serializable> getMeta();

    /**
     * Set the associated metadata
     * @return
     */
    void setMeta(Map<String, Serializable> meta);

    /**
     * Adds the given link to the document.
     *
     * @param link
     */
    void add(Link link);

    /**
     * Adds all given {@link Link}s to the document.
     *
     * @param links
     */
    void add(Iterable<Link> links);

    /**
     * Adds all given {@link Link}s to the document.
     *
     * @param links must not be {@literal null}.
     */
    void add(Link... links);

    /**
     * Returns whether the document contains {@link Link}s at all.
     *
     * @return
     */
    boolean hasLinks();

    /**
     * Returns whether the document contains a {@link Link} with the given rel.
     *
     * @param rel
     * @return
     */
    boolean hasLink(String rel);

    /**
     * Returns all {@link Link}s contained in this document.
     *
     * @return
     */
    List<Link> getLinks();

    /**
     * Removes all {@link Link}s added to the document so far.
     */
    void removeLinks();

    /**
     * Returns the link with the given rel.
     *
     * @param rel
     * @return the link with the given rel or {@literal null} if none found.
     */
    Link getLink(String rel);

}
