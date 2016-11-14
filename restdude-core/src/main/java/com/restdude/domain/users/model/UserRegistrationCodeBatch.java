/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.mdd.annotation.CurrentPrincipal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ShallowReference
@Entity
@ApiModel(description = "UserRegistrationCodeBatch")
@Table(name = "registration_codes_batch")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserRegistrationCodeBatch extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final long serialVersionUID = 1L;

    public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('" + Role.ROLE_ADMIN + "', '" + Role.ROLE_SITE_OPERATOR + "')";
    public static final String PRE_AUTHORIZE_VIEW = PRE_AUTHORIZE_SEARCH;
    public static final String PRE_AUTHORIZE_CREATE = PRE_AUTHORIZE_SEARCH;
    public static final String PRE_AUTHORIZE_UPDATE = PRE_AUTHORIZE_CREATE;
    public static final String PRE_AUTHORIZE_PATCH = PRE_AUTHORIZE_UPDATE;

    @NotNull
    @ApiModelProperty(value = "Unique short code, non-updatable.", required = true, example = "CompanyName01")
    @Column(nullable = false, unique = true, updatable = false)
    private String name;

    @NotNull
    @ApiModelProperty(value = "The batch description.", required = true, example = "A batch for CompanyName")
    @Column(nullable = false)
    private String description;

    @NotNull
    @Min(1)
    @Max(20)
    @ApiModelProperty(value = "The number of codes to generate (1 to 20), non-updatable.", required = true, example = "10")
    @Column(name = "batch_size", nullable = false, updatable = false)
    private Integer batchSize;

    @ApiModelProperty(value = "The number of available codes in the batch", readOnly = true)
    @Formula(" (select count(*) from registration_code where registration_code.batch_id = id and registration_code.credentials_id IS NULL) ")
    private Integer available;

    @NotNull
    @ApiModelProperty(value = "Creation date, generated automatically.", readOnly = true)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate = new Date();

    @ApiModelProperty(value = "Expiration date.")
    @Column(name = "expiration_date")
    private Date expirationDate;

    @JsonIgnore
    @CurrentPrincipal
    @NotNull
    @ApiModelProperty(value = "The batch creator", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "createdby_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User createdBy;


    public UserRegistrationCodeBatch() {
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("name", this.getName())
                .append("description", this.getDescription())
                .append("size", this.getBatchSize())
                .append("createdDate", this.getCreatedDate())
                .append("expirationDate", this.getExpirationDate())
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 47).appendSuper(super.hashCode()).append(this.name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        if (!UserRegistrationCodeBatch.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        UserRegistrationCodeBatch other = (UserRegistrationCodeBatch) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(obj));
        builder.append(getName(), other.getName());
        return builder.isEquals();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}