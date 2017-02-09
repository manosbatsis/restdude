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
package com.restdude.domain.details.contact.service.impl;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.mdd.service.AbstractModelServiceImpl;
import com.restdude.domain.confirmationtoken.model.ConfirmationToken;
import com.restdude.domain.confirmationtoken.repository.ConfirmationTokenReposirory;
import com.restdude.domain.details.contact.model.ContactDetail;
import com.restdude.domain.details.contact.service.ContactDetailsService;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

;

public abstract class AbstractContactDetailServiceImpl<T extends ContactDetail<PK>, PK extends Serializable, R extends ModelRepository<T, PK>>
        extends AbstractModelServiceImpl<T, PK, R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContactDetailServiceImpl.class);

    protected ContactDetailsService contactDetailsService;
    protected ConfirmationTokenReposirory confirmationTokenReposirory;

    @Autowired
    public void setContactDetailsService(ContactDetailsService contactDetailsService) {
        this.contactDetailsService = contactDetailsService;
    }

    @Autowired
    public void setConfirmationTokenReposirory(ConfirmationTokenReposirory confirmationTokenReposirory) {
        this.confirmationTokenReposirory = confirmationTokenReposirory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public T create(@P("resource") T resource) {
        resource = super.create(resource);
        if (!resource.getVerified()) {
            ConfirmationToken token = this.confirmationTokenReposirory.persist(new ConfirmationToken.Builder().targetId(resource.getPk().toString()).build());
        }
        return resource;
    }

    /**
     * Reroute to {@link #patch(CalipsoPersistable)}
     *
     * @see #patch(CalipsoPersistable)
     */
    @Override
    @Transactional(readOnly = false)
    public T update(@P("resource") T resource) {
        return this.patch(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public T patch(@P("resource") T resource) {
        // verify?
        String verificationCode = resource.getVerificationToken();
        ConfirmationToken token = this.confirmationTokenReposirory.findByTokenValueAndRTargetId(resource.getVerificationToken(), resource.getPk().toString());
        if (token != null) {
            this.confirmationTokenReposirory.delete(token);
            resource.setVerified(true);
        }
        // note primary
        boolean primary = BooleanUtils.isTrue(resource.getPrimary());
        resource = super.patch(resource);
        resource.setPrimary(primary);

        // set as primary if appropriate
        resource.setContactDetails(this.contactDetailsService.setPrimary(resource.getContactDetails(), resource));

        return resource;

    }
}