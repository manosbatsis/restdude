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

import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.registry.FieldInfo;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.util.ParamsAwarePage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
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
    public static final String MIME_APPLICATIOM_HAL_PLUS_JSON = " application/hal+json";

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

    public static List<Link> buileHateoasLinks(@NonNull PersistableModel model, @NonNull ModelInfo modelInfo) {
        List<Link> links = null;
        if (model.getPk() != null && modelInfo != null) {

            links = new LinkedList<>();

            // add link to self
            links.add(BasicLinkBuilder.linkToCurrentMapping()
                    .slash(modelInfo.getRequestMapping())
                    .slash(model.getPk()).withSelfRel());

            // add links to linkable relationships
            Set<String> relationshipFields = new HashSet<>();
            relationshipFields.addAll(modelInfo.getToOneFieldNames());
            relationshipFields.addAll(modelInfo.getToManyFieldNames());
            for(String fieldName : relationshipFields){
                FieldInfo fieldInfo = modelInfo.getField(fieldName);

                if(fieldInfo.isLinkableResource()){
                    links.add(BasicLinkBuilder.linkToCurrentMapping()
                            .slash(modelInfo.getRequestMapping())
                            .slash(model.getPk())
                            .slash("relationships")
                            .slash(fieldName).withRel(fieldName));
                }
            }
        }
        return links;
    }



}
