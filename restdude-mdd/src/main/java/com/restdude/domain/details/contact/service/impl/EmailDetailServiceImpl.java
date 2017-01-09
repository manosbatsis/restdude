/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.details.contact.service.impl;

import com.restdude.domain.confirmationtoken.model.ConfirmationToken;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.details.contact.repository.EmailDetailRepository;
import com.restdude.domain.details.contact.service.EmailDetailService;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named(EmailDetailService.BEAN_ID)
public class EmailDetailServiceImpl extends AbstractContactDetailServiceImpl<EmailDetail, String, EmailDetailRepository> implements EmailDetailService {


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public EmailDetail forceVerify(@P("resource") EmailDetail resource) {
        if (!resource.getVerified()) {
            ConfirmationToken token = this.confirmationTokenReposirory.findByTargetId(resource.getId());
            if (token != null) {
                this.confirmationTokenReposirory.delete(token);
            }
            resource.setVerified(true);
            resource = this.repository.save(resource);
        }
        return resource;
    }
}
