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
package com.restdude.domain.details.contact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.model.PersistableModel;
import com.restdude.domain.users.model.User;
import io.swagger.annotations.ApiModel;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import java.util.List;

@ShallowReference
@Entity
@ApiModel(description = "ContactDetails")
@Table(name = "details_contact")
@Inheritance(strategy = InheritanceType.JOINED)
public class ContactDetails implements PersistableModel<String> {

    @Id
    private String id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    private User user;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_email_id")
    private EmailDetail primaryEmail;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_cellphone_id")
    private CellphoneDetail primaryCellphone;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_postaladdress_id")
    private PostalAddressDetail primaryAddress;

    @JsonIgnore
    @OneToMany(mappedBy = "contactDetails", orphanRemoval = true)
    private List<EmailDetail> emails;

    @JsonIgnore
    @OneToMany(mappedBy = "contactDetails", orphanRemoval = true)
    private List<CellphoneDetail> cellphones;

    @JsonIgnore
    @OneToMany(mappedBy = "contactDetails", orphanRemoval = true)
    private List<PostalAddressDetail> adresses;

    public ContactDetails() {
    }

    public ContactDetails(User user) {
        this.user = user;
    }

    /**
     * Get the entity's primary key
     *
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public String getPk() {
        return id;
    }

    /**
     * Set the entity's primary key
     *
     * @param id the pk to set
     */
    public void setPk(String id) {
        this.id = id;
    }

    @Override
    public void preSave() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return null == getPk();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EmailDetail getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(EmailDetail primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public CellphoneDetail getPrimaryCellphone() {
        return primaryCellphone;
    }

    public void setPrimaryCellphone(CellphoneDetail primaryCellphone) {
        this.primaryCellphone = primaryCellphone;
    }

    public PostalAddressDetail getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(PostalAddressDetail primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public List<EmailDetail> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailDetail> emails) {
        this.emails = emails;
    }

    public List<CellphoneDetail> getCellphones() {
        return cellphones;
    }

    public void setCellphones(List<CellphoneDetail> cellphones) {
        this.cellphones = cellphones;
    }

    public List<PostalAddressDetail> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<PostalAddressDetail> adresses) {
        this.adresses = adresses;
    }

    public static class Builder {
        private String id;
        private User user;
        private EmailDetail primaryEmail;
        private CellphoneDetail primaryCellphone;
        private PostalAddressDetail primaryAddress;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder primaryEmail(EmailDetail primaryEmail) {
            this.primaryEmail = primaryEmail;
            return this;
        }

        public Builder primaryCellphone(CellphoneDetail primaryCellphone) {
            this.primaryCellphone = primaryCellphone;
            return this;
        }

        public Builder primaryAddress(PostalAddressDetail primaryAddress) {
            this.primaryAddress = primaryAddress;
            return this;
        }

        public ContactDetails build() {
            return new ContactDetails(this);
        }
    }

    private ContactDetails(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.primaryEmail = builder.primaryEmail;
        this.primaryCellphone = builder.primaryCellphone;
        this.primaryAddress = builder.primaryAddress;
    }
}
