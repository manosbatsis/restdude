package com.restdude.domain.details.contact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class AbstractContactDetail extends AbstractSystemUuidPersistable implements CalipsoPersistable<String>, ContactDetail<String> {


    @Transient
    private Boolean primary;

    @Transient
    private String verificationToken;

    @NotNull
    @Column(nullable = false)
    private Boolean verified;

    @Size(max = 255)
    @Column()
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "details_contact_id", nullable = false, updatable = false)
    private ContactDetails contactDetails;

    public AbstractContactDetail() {
    }

    @Override
    public void preSave() {
        // JSON deserialization forces null on create/update,
        // service forces patch on update,
        // hence only programmatic control is allowed
        // with false set as default bellow
        if (this.getVerified() == null) {
            this.setVerified(false);
        }
        super.preSave();
    }

    @JsonProperty
    @Override
    public Boolean getVerified() {
        return verified;
    }

    @JsonIgnore
    @Override
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    @Override
    public String getVerificationToken() {
        return verificationToken;
    }

    @Override
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    @Override
    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }
}
