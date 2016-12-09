package com.restdude.domain.details.contact.model;

import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Email;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ShallowReference
@Entity
@ApiModel(description = "EmailDetail")
@Table(name = "detail_contact_email")
@Inheritance(strategy = InheritanceType.JOINED)
public class EmailDetail extends AbstractContactDetail {

    @NotNull
    @Email
    @Column(name = "email", unique = true, nullable = false, updatable = false)
    private String email;

    public EmailDetail() {
        super();
    }

    public EmailDetail(String email) {
        this();
        this.email = email;
    }

    @Override
    public String getValue() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
