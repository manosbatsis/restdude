package com.restdude.domain.details.contact.model;

import com.restdude.domain.base.model.CalipsoPersistable;

import java.io.Serializable;

public interface ContactDetail<ID extends Serializable> extends CalipsoPersistable<ID> {

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
