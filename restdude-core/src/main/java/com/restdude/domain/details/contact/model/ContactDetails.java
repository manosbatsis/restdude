package com.restdude.domain.details.contact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.model.AbstractPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
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
public class ContactDetails extends AbstractPersistable<String> implements CalipsoPersistable<String> {

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
    public String getId() {
        return id;
    }

    /**
     * Set the entity's primary key
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getId();
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
