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

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.impl.AbstractModelServiceImpl;
import com.restdude.domain.error.model.ErrorLog;
import com.restdude.domain.error.model.PersistableError;
import com.restdude.domain.error.model.UserAgent;
import com.restdude.domain.error.repository.ErrorLogRepository;
import com.restdude.domain.error.service.UserAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public abstract class AbstractErrorServiceImpl<T extends PersistableError<ID>, ID extends Serializable, R extends ModelRepository<T, ID>>
        extends AbstractModelServiceImpl<T, ID, R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractErrorServiceImpl.class);


    private UserAgentService userAgentService;
    private ErrorLogRepository errorLogService;

    @Autowired
    public void setErrorLogRepository(ErrorLogRepository errorLogService) {
        this.errorLogService = errorLogService;
    }

    @Autowired
    public void setUUserAgentService(UserAgentService userAgentService) {
        this.userAgentService = userAgentService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(T.PRE_AUTHORIZE_CREATE)
    public T create(T resource) {
        LOGGER.debug("create PersistableError: {}", resource);

        // merge the UserAgent based on it's hash/id
        UserAgent ua = resource.getUserAgent();
        if (ua != null) {
            resource.setUserAgent(this.userAgentService.create(ua));
        }
        // merge the ErrorLog based on it's hash/id
        ErrorLog log = resource.getErrorLog();
        if (log != null) {
            resource.setErrorLog(this.errorLogService.merge(log));
        }

        // save and return
        return super.create(resource);
    }

}