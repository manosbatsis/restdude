package com.restdude.domain.details.contact.model;

import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Email;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ShallowReference
@Entity
@ApiModel(description = "CellphoneDetail")
@Table(name = "detail_contact_cellphone")
@Inheritance(strategy = InheritanceType.JOINED)
public class CellphoneDetail extends AbstractContactDetail {

    @Email
    @NotNull
    @Column(name = "number", unique = true, nullable = false, updatable = false)
    private String number;

    public CellphoneDetail() {
    }

    @Override
    public String getValue() {
        return number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
