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

import com.restdude.domain.base.service.AbstractModelServiceImpl;
import com.restdude.domain.error.model.StackTrace;
import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.repository.StackTraceRepository;
import com.restdude.domain.error.repository.SystemErrorRepository;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.util.exception.http.HttpException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.util.Date;

@Named("systemErrorService")
public class SystemErrorServiceImpl extends AbstractModelServiceImpl<SystemError, String, SystemErrorRepository>
        implements SystemErrorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemErrorServiceImpl.class);

    private StackTraceRepository stackTraceRepository;

    @Autowired
    public void setRepository(StackTraceRepository stackTraceRepository) {
        this.stackTraceRepository = stackTraceRepository;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    @PreAuthorize(SystemError.PRE_AUTHORIZE_CREATE)
    public SystemError create(SystemError resource) throws HttpException {

        // init timstamp
        Date now = new Date();
        resource.setCreatedDate(now);
        // assign new or existing stacktrace
        resource.setStackTrace(this.getStackTrace(now, resource.getThrowable()));

        // save and return
        return super.create(resource);
    }

    private StackTrace getStackTrace(Date now, Throwable ex) {
        StackTrace stackTrace = null;
        try {
            com.github.tkawachi.exhash.core.IStacktrace hashStacktrace = com.github.tkawachi.exhash.core.Stacktrace.getInstance(ex);
            com.github.tkawachi.exhash.core.IStacktraceHash h = new com.github.tkawachi.exhash.core.StacktraceHash();
            String hash = h.hash(hashStacktrace);
            stackTrace = this.stackTraceRepository.findOne(hash);
            if (stackTrace != null) {
                stackTrace.setLastOccurred(now);
                stackTrace = this.stackTraceRepository.save(stackTrace);
            } else {
                stackTrace = this.stackTraceRepository.save(new StackTrace(hash, ExceptionUtils.getRootCauseMessage(ex), now, ExceptionUtils.getStackTrace(ex)));
            }
        } catch (com.github.tkawachi.exhash.core.StacktraceHashException e) {
            LOGGER.error("Failed creating exception hash", e);
        }
        return stackTrace;
    }

}