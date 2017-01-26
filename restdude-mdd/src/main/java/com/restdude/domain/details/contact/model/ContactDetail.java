/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.details.contact.model;

import com.restdude.domain.base.model.CalipsoPersistable;

import java.io.Serializable;

public interface ContactDetail<PK extends Serializable> extends CalipsoPersistable<PK> {

    String getValue();

    Boolean getVerified();

    void setVerified(Boolean verified);

    Boolean getPrimary();

    void setPrimary(Boolean primary);

    ContactDetails getContactDetails();

    void setContactDetails(ContactDetails contactDetails);

    String getVerificationToken();

    void setVerificationToken(String verificationToken);
}
