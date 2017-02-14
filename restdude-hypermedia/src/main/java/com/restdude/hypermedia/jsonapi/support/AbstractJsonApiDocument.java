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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.hypermedia.jsonapi.JsonApiDocument;
import com.restdude.hypermedia.jsonapi.JsonApiResource;
import com.restdude.hypermedia.jsonapi.util.JsonApiModelBasedDocumentBuilder;
import com.restdude.mdd.model.ErrorModel;
import com.restdude.mdd.model.Model;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class implementation of {@JsonApiDocument}
 *
 * @see JsonApiModelBasedDocumentBuilder
 * @see <a href="http://jsonapi.org/format/upcoming/#document-structure">JSON API Resources</a>
 */
public abstract class AbstractJsonApiDocument<D extends Object, T extends Model<ID>, ID extends Serializable> implements JsonApiDocument<D, T, ID> {

    private D data;
    private Collection<ErrorModel> errors;
    private Map<String, Serializable> meta;
    private Collection<JsonApiResource> included;
    @JsonIgnore
    private ResourceSupport linksDelegate;

    public AbstractJsonApiDocument(){
        this.linksDelegate = new ResourceSupport();
    }

    public AbstractJsonApiDocument(D data){
        this();
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public D getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(D data) {
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<JsonApiResource> getIncluded() {
        return included;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIncluded(Collection<JsonApiResource> included) {
        this.included = included;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ErrorModel> getErrors() {
        return errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrors(Collection<ErrorModel> errors) {
        this.errors = errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Serializable> getMeta() {
        return meta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMeta(Map<String, Serializable> meta) {
        this.meta = meta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Link link) {
        linksDelegate.add(link);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Iterable<Link> links) {
        linksDelegate.add(links);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Link... links) {
        linksDelegate.add(links);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLinks() {
        return linksDelegate.hasLinks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLink(String rel) {
        return linksDelegate.hasLink(rel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @XmlElement(
        name = "link",
        namespace = "http://www.w3.org/2005/Atom"
    )
    @JsonProperty("links")
    public List<Link> getLinks() {
        return linksDelegate.getLinks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLinks() {
        linksDelegate.removeLinks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Link getLink(String rel) {
        return linksDelegate.getLink(rel);
    }



}
