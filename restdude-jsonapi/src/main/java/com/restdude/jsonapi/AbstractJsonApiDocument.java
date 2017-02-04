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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.model.JsonApiDocument;
import com.restdude.domain.base.model.JsonApiResource;
import org.springframework.hateoas.Link;
import com.restdude.domain.error.model.Error;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.*;

/*
 * A wrapper class to s(de)erialize models as a JSON API Document
 *
 * @see <a href="http://jsonapi.org/format/upcoming/#document-structure">JSON API Resources</a>
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractJsonApiDocument<D extends Serializable, T extends CalipsoPersistable<ID>, ID extends Serializable> implements JsonApiDocument<D, T, ID> {

    private Iterable<Error> errors;
    private Map<String, Serializable> meta;
    private Iterable<JsonApiResource> included;
    @JsonIgnore
    private ResourceSupport linksDelegate;

    public AbstractJsonApiDocument(){
        this.linksDelegate = new ResourceSupport();
    }

    public AbstractJsonApiDocument(D data){
        this();
        this.setData(data);
    }

    @Override
    public Iterable<JsonApiResource> getIncluded() {
        return included;
    }

    @Override
    public void setIncluded(Iterable<JsonApiResource> included) {
        this.included = included;
    }

    @Override
    public Iterable<Error> getErrors() {
        return errors;
    }

    @Override
    public void setErrors(Iterable<Error> errors) {
        this.errors = errors;
    }

    @Override
    public Map<String, Serializable> getMeta() {
        return meta;
    }

    @Override
    public void setMeta(Map<String, Serializable> meta) {
        this.meta = meta;
    }

    @Override
    public void add(Link link) {
        linksDelegate.add(link);
    }

    @Override
    public void add(Iterable<Link> links) {
        linksDelegate.add(links);
    }

    @Override
    public void add(Link... links) {
        linksDelegate.add(links);
    }

    @Override
    public boolean hasLinks() {
        return linksDelegate.hasLinks();
    }

    @Override
    public boolean hasLink(String rel) {
        return linksDelegate.hasLink(rel);
    }

    @Override
    @XmlElement(
        name = "link",
        namespace = "http://www.w3.org/2005/Atom"
    )
    @JsonProperty("links")
    public List<Link> getLinks() {
        return linksDelegate.getLinks();
    }

    @Override
    public void removeLinks() {
        linksDelegate.removeLinks();
    }

    @Override
    public Link getLink(String rel) {
        return linksDelegate.getLink(rel);
    }
}
