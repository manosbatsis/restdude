/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.error.service.impl;

import com.restdude.domain.base.service.impl.AbstractModelServiceImpl;
import com.restdude.domain.error.model.UserAgent;
import com.restdude.domain.error.repository.UserAgentRepository;
import com.restdude.domain.error.service.UserAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Named;

@Named(UserAgentService.BEAN_ID)
public class UserAgentServiceImpl extends AbstractModelServiceImpl<UserAgent, String, UserAgentRepository>
        implements UserAgentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAgentServiceImpl.class);


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(UserAgent.PRE_AUTHORIZE_CREATE)
    public UserAgent create(@P("resource") UserAgent resource) {
        Assert.notNull(resource, "Resource can't be null");
        this.validate(resource);
        resource = repository.merge(resource);
        this.postCreate(resource);
        return resource;
    }

}