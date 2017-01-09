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

import com.restdude.domain.geography.model.PostalCode;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ShallowReference
@Entity
@ApiModel(description = "PostalAddressDetail")
@Table(name = "detail_contact_postaladdress")
@Inheritance(strategy = InheritanceType.JOINED)
public class PostalAddressDetail extends AbstractContactDetail {

    @NotNull
    @Column(name = "addressline_1", nullable = false, updatable = true)
    private String addressLine1;

    @NotNull
    @Column(name = "addressline_2", updatable = true)
    private String addressLine2;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posralcode_id", referencedColumnName = "id", nullable = false)
    private PostalCode postCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private ContactDetails contactDetails;

    public PostalAddressDetail() {

    }

    @Override
    public String getValue() {
        StringBuffer s = new StringBuffer(this.getAddressLine1());
        s.append(",");
        if (StringUtils.isNoneBlank(this.getAddressLine1())) {
            s.append(this.getAddressLine2()).append(",");

        }
        s.append(this.getPostCode().getName()).append(' ').append(this.getPostCode().getParent().getName());
        return s.toString();
    }


    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public PostalCode getPostCode() {
        return postCode;
    }

    public void setPostCode(PostalCode postCode) {
        this.postCode = postCode;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }
}
