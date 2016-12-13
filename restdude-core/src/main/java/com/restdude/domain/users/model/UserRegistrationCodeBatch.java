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
import com.restdude.auth.spel.annotations.PreAuthorizeCreate;
import com.restdude.auth.spel.annotations.PreAuthorizeFindPaginated;
import com.restdude.auth.spel.annotations.PreAuthorizePatch;
import com.restdude.auth.spel.annotations.PreAuthorizeUpdate;
import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.mdd.annotation.CurrentPrincipal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ShallowReference
@Entity
@ApiModel(description = "UserRegistrationCodeBatch")
@Table(name = "registration_codes_batch")
@Inheritance(strategy = InheritanceType.JOINED)

@PreAuthorizeCreate(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeUpdate(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizePatch(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
//TODO
@PreAuthorizeFindPaginated(controller = SpelUtil.PERMIT_ALL/*HAS_ROLE_ADMIN*/, service = SpelUtil.PERMIT_ALL)
public class UserRegistrationCodeBatch extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final long serialVersionUID = 1L;

    @CreatedDate
    @DiffIgnore
    @ApiModelProperty(value = "Date created")
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @DiffIgnore
    @ApiModelProperty(value = "Date last modified")
    @Column(name = "date_last_modified", nullable = false)
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @DiffIgnore
    @JsonIgnore
    @ApiModelProperty(value = "Created by", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", referencedColumnName = "id", updatable = false)
    private User lastModifiedBy;

    @CreatedBy
    @DiffIgnore
    @JsonIgnore
    @CurrentPrincipal
    @ApiModelProperty(value = "Created by", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby_id", referencedColumnName = "id", updatable = false)
    private User createdBy;

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

    @ApiModelProperty(value = "Expiration date.")
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}