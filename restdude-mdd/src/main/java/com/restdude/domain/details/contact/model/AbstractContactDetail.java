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
