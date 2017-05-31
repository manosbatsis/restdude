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
package com.restdude.hypermedia.util;

import com.restdude.domain.Model;
import com.restdude.hypermedia.hateoas.ModelResource;
import com.restdude.hypermedia.hateoas.ModelResources;
import com.restdude.hypermedia.jsonapi.JsonApiModelResourceDocument;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.util.ParamsAwarePage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Provides utilities for working with JSON API
 */
@Slf4j
public class HypermediaUtils {

    public static final String MIME_APPLICATION_VND_PLUS_JSON = "application/vnd.api+json";
    public static final String MIME_APPLICATIOM_HAL_PLUS_JSON = "application/hal+json";
    public static final String MIME_APPLICATIOM_JSON = "application/json";

    public static List<Link> buileHateoasLinks(@NonNull ParamsAwarePage page, @NonNull HttpServletRequest request, @NonNull String pageNumberParamName) {
        List<Link> links = new LinkedList<>();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL()+"?"+request.getQueryString());
        // add first
        if(!page.isFirst()){
            uriComponentsBuilder.replaceQueryParam(pageNumberParamName, 0);
            // create the link builder
            links.add(new UriComponentsBuilderAdapterLinkBuilder(uriComponentsBuilder).withRel("first"));
        }
        // add previous
        if(page.hasPrevious()){
            uriComponentsBuilder.replaceQueryParam(pageNumberParamName, 1);
            // create the link builder
            links.add(new UriComponentsBuilderAdapterLinkBuilder(uriComponentsBuilder).withRel("previous"));
        }

        // add next
        if(page.hasNext()){
            uriComponentsBuilder.replaceQueryParam(pageNumberParamName, page.getNumber() + 1);
            // create the link builder
            links.add(new UriComponentsBuilderAdapterLinkBuilder(uriComponentsBuilder).withRel("next"));
        }

        // add last
        if(!page.isLast()){
            uriComponentsBuilder.replaceQueryParam(pageNumberParamName, page.getTotalPages() - 1);
            // create the link builder
            links.add(new UriComponentsBuilderAdapterLinkBuilder(uriComponentsBuilder).withRel("last"));
        }
        return links;
    }

    public static List<Link> buileHateoasLinks(@NonNull Model model, ModelInfo modelInfo) {
        List<Link> links = null;
        if (model.getId() != null && modelInfo != null) {

            links = new LinkedList<>();

            // add link to self
            links.add(BasicLinkBuilder.linkToCurrentMapping()
                    .slash(modelInfo.getRequestMapping())
                    .slash(model.getId()).withSelfRel());

            // add links to linkable relationships
            Set<String> relationshipFields = new HashSet<>();
            relationshipFields.addAll(modelInfo.getToOneFieldNames());
            relationshipFields.addAll(modelInfo.getToManyFieldNames());
            for(String fieldName : relationshipFields){
                FieldInfo fieldInfo = modelInfo.getField(fieldName);

                if(fieldInfo.isLinkableResource()){
                    links.add(BasicLinkBuilder.linkToCurrentMapping()
                            .slash(modelInfo.getRequestMapping())
                            .slash(model.getId())
                            .slash("relationships")
                            .slash(fieldName).withRel(fieldName));
                }
            }
        }
        return links;
    }

    public static <RT extends Model<RID>, RID extends Serializable> ModelResource<RT> toHateoasResource(RT model, ModelInfo<RT, RID> modelInfo) {
        Class<RT> modelType = (modelInfo != null) ? modelInfo.getModelType() : (Class<RT>) model.getClass();
        ModelResource<RT> resource = new ModelResource<>(model);
        List<Link> links = HypermediaUtils.buileHateoasLinks(model, modelInfo);
        log.debug("toHateoasResource, model: {}, modelType: {}, modelInfo: {}", model, modelType, modelInfo);
        if(CollectionUtils.isNotEmpty(links)) {
            resource.add(links);
        }
        return resource;
    }

    /**
     * Wrap the given models in a {@link Resources} and add {@link org.springframework.hateoas.Link}s
     *
     * @param models
     */
    public static <RT extends Model> ModelResources<RT> toHateoasResources(@NonNull Iterable<RT> models, Class<RT> modelType) {
        LinkedList<ModelResource<RT>> wrapped = new LinkedList<>();
        for(RT model : models){
            wrapped.add(new ModelResource<RT>(model));
        }
        ModelResources<RT> resources = new ModelResources<>(wrapped);
        return resources;
    }

    /**
     * Wrap the given model in a JSON API Document
     * @param model the model to wrap
     * @return
     */
    public static <RT extends Model<RID>, RID extends Serializable> JsonApiModelResourceDocument<RT, RID> toDocument(RT model, ModelInfo<RT, RID> modelInfo) {
        JsonApiModelResourceDocument<RT, RID> doc = new JsonApiModelBasedDocumentBuilder<RT, RID>(modelInfo.getUriComponent())
                .withData(model)
                .buildModelDocument();
        List<Link> tmp = HypermediaUtils.buileHateoasLinks(model, modelInfo);
        if(CollectionUtils.isNotEmpty(tmp)){
            for(Link l : tmp){
                doc.add(l.getRel(), l.getHref());
            }
        }
        return doc;
    }
}
