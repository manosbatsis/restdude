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
package com.restdude.hypermedia.hateoas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restdude.mdd.model.Model;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.Collection;

/**
 * Created by manos on 21/2/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PagedModelResources<M extends Model> extends PagedResources<ModelResource<M>> {

    /**
     * {@inheritDoc}
     */
    public PagedModelResources() {
    }

    /**
     * {@inheritDoc}
     */
    public PagedModelResources(Collection<ModelResource<M>> content, PageMetadata metadata, Link... links) {
        super(content, metadata, links);
    }

    /**
     * {@inheritDoc}
     */
    public PagedModelResources(Collection<ModelResource<M>> content, PageMetadata metadata, Iterable<Link> links) {
        super(content, metadata, links);
    }
}
