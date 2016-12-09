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
