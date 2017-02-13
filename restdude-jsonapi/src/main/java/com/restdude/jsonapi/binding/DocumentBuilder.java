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
package com.restdude.jsonapi.binding;

import com.restdude.jsonapi.*;
import com.restdude.jsonapi.support.SimpleModelCollectionDocument;
import com.restdude.jsonapi.support.SimpleModelDocument;
import com.restdude.jsonapi.support.SimpleModelResource;
import com.restdude.mdd.model.Model;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.*;

/**
 * Utility class to construct {@link com.yahoo.elide.jsonapi.models.JsonApiDocument} instances
 */
public class DocumentBuilder<T extends Model<PK>, PK extends Serializable> {


    private String jsonType;
    private Collection<Error> errors;
    private Map<String, Serializable> meta;
    private Collection<JsonApiResource> included;
    private List<Link> links;
    private SimpleModelResource<T, PK> resource;
    private List<SimpleModelResource<T, PK>> resources;

    // TODO: add config options for links, included
    public DocumentBuilder(){}

    public DocumentBuilder(String jsonType) {
        this.jsonType = jsonType;
    }

    public DocumentBuilder withErrors(Collection<Error> errors) {
        if(this.resource != null || CollectionUtils.isNotEmpty(this.resources)){
            throw new IllegalStateException("Cannot include both errors and resources in the same document");
        }
        this.errors = errors;
        return this;
    }

    public DocumentBuilder withMeta(Map<String, Serializable> meta) {
        this.meta = meta;
        return this;
    }

    public DocumentBuilder withIncluded(Collection<JsonApiResource> included) {
        this.included = included;
        return this;
    }

    public DocumentBuilder withLinks(Collection<Link> links) {
        if(this.links == null) {
            this.links = new LinkedList<>();
        }
        this.links.addAll(links);
        return this;
    }

    public void withMeta(String name, Serializable value){
        if(this.meta == null){
            this.meta = new HashMap<>();
        }
        this.meta.put(name, value);

    }

    public DocumentBuilder withPage(Page<T> page){
        this.withData(page.getContent());

        this.withMeta("first", page.isFirst());
        this.withMeta("last", page.isLast());
        this.withMeta("size", page.getSize());
        this.withMeta("number", page.getNumber());
        this.withMeta("numberOfElements", page.getNumberOfElements());
        this.withMeta("totalElements", page.getTotalElements());
        this.withMeta("totalPages", page.getTotalPages());
        this.withMeta("sort", page.getSort());

        return this;
    }

    public DocumentBuilder withData(Collection<T> models) {

        if(this.resource != null){
            throw new IllegalStateException("Was given a collection of models but a single resource was already set");
        }
        if(CollectionUtils.isNotEmpty(models) && CollectionUtils.isNotEmpty(this.errors)){
            throw new IllegalStateException("Was given a non-empty collection of models but errors are already set");
        }
        if(CollectionUtils.isNotEmpty(this.resources)){
            throw new IllegalStateException("Was given a collection of models but resources are already set");
        }

        this.resources = new ArrayList(models.size());
        for(T model : models){
            this.resources.add(new SimpleModelResource<>(model, this.jsonType));
        }

        return this;
    }

    private DocumentBuilder withData(SimpleModelResource<T, PK>[] data) {

        if(this.resource != null){
            throw new IllegalStateException("Was given a collection of resources but a single resource was already set");
        }
        if(ArrayUtils.isNotEmpty(data) && CollectionUtils.isNotEmpty(this.errors)){
            throw new IllegalStateException("Was given a non-empty collection of resources but errors are already set");
        }
        if(CollectionUtils.isNotEmpty(this.resources)){
            throw new IllegalStateException("Was given a collection of resources but resources are already set");
        }

        this.resources = new ArrayList(data.length);
        for(SimpleModelResource<T, PK> rs : data){
            this.resources.add(rs);
        }

        return this;
    }

    public DocumentBuilder withData(T model) {

        if(this.resource != null){
            throw new IllegalStateException("Was given a model but a single resource was already set");
        }
        if(CollectionUtils.isNotEmpty(this.errors)){
            throw new IllegalStateException("Was given a model but errors are already set");
        }
        if(CollectionUtils.isNotEmpty(this.resources)){
            throw new IllegalStateException("Was given a model but resources are already set");
        }
        this.resource = new SimpleModelResource<>(model, this.jsonType);
        return this;
    }

    public DocumentBuilder withData(SimpleModelResource<T, PK> resource) {

        if(this.resource != null){
            throw new IllegalStateException("Was given a resource but one was already set");
        }
        if(CollectionUtils.isNotEmpty(this.errors)){
            throw new IllegalStateException("Was given a resource but errors are already set");
        }
        if(CollectionUtils.isNotEmpty(this.resources)){
            throw new IllegalStateException("Was given a resource but resources are already set");
        }
        this.resource = resource;
        return this;
    }

    public JsonApiDocument build() {
        JsonApiDocument document;
        if(this.resources != null) {
            document = new SimpleModelCollectionDocument(this.resources);
            document.setErrors(this.errors);
        }
        else if(this.resource != null) {
            document = new SimpleModelDocument(this.resource);
        }
        else if(this.errors != null) {
            document = new SimpleModelDocument();
        }
        else {
            throw new IllegalStateException("Cannot build a JsonApiDocument without a single resource, resource collection or errors");
        }

        if(CollectionUtils.isNotEmpty(this.links)){
            document.add(this.links);
        }
        if(CollectionUtils.isNotEmpty(this.included)){
            document.setIncluded(this.included);
        }
        if(MapUtils.isNotEmpty(this.meta)){
            document.setMeta(this.meta);
        }

        return document;
    }

    public JsonApiModelDocument<T, PK> buildModelDocument(){
        if(CollectionUtils.isNotEmpty(this.resources)){
            throw new IllegalStateException("Cannot build a JsonApiModelDocument as multiple resources are set");
        }
        return (JsonApiModelDocument<T, PK>) this.build();
    }

    public JsonApiModelCollectionDocument<T, PK> buildModelCollectionDocument(){
        if(this.resource != null){
            throw new IllegalStateException("Cannot build a JsonApiModelCollectionDocument as a single resource is set");
        }
        return (JsonApiModelCollectionDocument<T, PK>) this.build();
    }
}
