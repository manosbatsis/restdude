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

import com.restdude.mdd.model.PersistableModel;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.domain.details.contact.model.*;
import com.restdude.domain.details.contact.repository.ContactDetailsRepository;
import com.restdude.domain.details.contact.repository.EmailDetailRepository;
import com.restdude.domain.details.contact.service.ContactDetailsService;
import com.restdude.domain.users.model.User;
import com.restdude.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named(ContactDetailsService.BEAN_ID)
public class ContactDetailsServiceImpl extends AbstractPersistableModelServiceImpl<ContactDetails, String, ContactDetailsRepository> implements ContactDetailsService {

    protected EmailDetailRepository emailDetailRepository;

    @Autowired
    public void setEmailDetailRepository(EmailDetailRepository emailDetailRepository) {
        this.emailDetailRepository = emailDetailRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public ContactDetails create(@P("resource") ContactDetails resource) {
        // note email if provided
        EmailDetail emailDetail = resource.getPrimaryEmail();
        resource.setPrimaryEmail(null);
        resource = super.create(resource);

        // persist email
        if (emailDetail != null) {
            emailDetail.setContactDetails(resource);
            emailDetail = this.emailDetailRepository.persist(emailDetail);
            resource.setPrimaryEmail(emailDetail);
            resource = this.repository.save(resource);
        }

        return resource;
    }

    /**
     * Reroute to {@link #patch(PersistableModel)}
     *
     * @see #patch(PersistableModel)
     */
    @Override
    @Transactional(readOnly = false)
    public ContactDetails update(@P("resource") ContactDetails resource) {
        return super.patch(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public ContactDetails setPrimary(@P("resource") ContactDetails resource, @P("detail") ContactDetail detail) {
        if (EmailDetail.class.isAssignableFrom(detail.getClass())) {
            resource = this.setPrimary(resource, (EmailDetail) detail);
        } else if (CellphoneDetail.class.isAssignableFrom(detail.getClass())) {
            resource = this.setPrimary(resource, (CellphoneDetail) detail);
        } else if (PostalAddressDetail.class.isAssignableFrom(detail.getClass())) {
            resource = this.setPrimary(resource, (CellphoneDetail) detail);
        } else {
            throw new RuntimeException("Unexpected domain type instance to set as primary detail: " + detail);
        }
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public ContactDetails setPrimary(@P("resource") ContactDetails resource, @P("detail") EmailDetail detail) {
        ContactDetails contactDetails = this.repository.getOne(resource.getPk());
        if (detail.getPrimary() || resource.getPrimaryEmail() == null) {
            contactDetails.setPrimaryEmail(detail);
            User u = contactDetails.getUser();
            u.setEmailHash(HashUtils.md5Hex(detail.getValue()));
            this.userRepository.save(u);
            contactDetails = this.repository.save(contactDetails);
        }
        return contactDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public ContactDetails setPrimary(@P("resource") ContactDetails resource, @P("detail") CellphoneDetail detail) {
        ContactDetails contactDetails = this.repository.getOne(resource.getPk());
        if (detail.getPrimary() || resource.getPrimaryCellphone() == null) {
            contactDetails.setPrimaryCellphone(detail);
            contactDetails = this.repository.save(contactDetails);
        }
        return contactDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public ContactDetails setPrimary(@P("resource") ContactDetails resource, @P("detail") PostalAddressDetail detail) {
        ContactDetails contactDetails = this.repository.getOne(resource.getPk());
        if (detail.getPrimary() || resource.getPrimaryCellphone() == null) {
            contactDetails.setPrimaryAddress(detail);
            contactDetails = this.repository.save(contactDetails);
        }
        return contactDetails;
    }
}
