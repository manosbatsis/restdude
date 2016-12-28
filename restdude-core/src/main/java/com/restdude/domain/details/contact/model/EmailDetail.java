package com.restdude.domain.details.contact.model;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        if (!EmailDetail.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        EmailDetail other = (EmailDetail) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(obj));
        builder.append(StringUtils.lowerCase(getEmail()), StringUtils.lowerCase(other.getEmail()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 13).appendSuper(super.hashCode()).append(StringUtils.lowerCase(getEmail())).toHashCode();
    }

    @Override
    public void preSave() {
        if (StringUtils.isNoneEmpty(this.getEmail())) {
            this.setEmail(this.getEmail().toLowerCase());
        }
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
