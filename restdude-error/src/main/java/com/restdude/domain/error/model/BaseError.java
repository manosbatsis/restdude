/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-error, https://manosbatsis.github.io/restdude/restdude-error
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.auth.spel.annotations.*;
import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.error.ErrorUtil;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.annotation.CurrentPrincipal;
import com.restdude.mdd.annotation.CurrentPrincipalField;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@ModelResource(path = BaseError.API_PATH, controllerSuperClass = AbstractReadOnlyModelController.class,
        apiName = "Errors", apiDescription = "Generic error information (readonly)")
@ApiModel(value = "BaseError", description = "Generic error superclass")
@Entity
@Table(name = "error_abstract")
@Inheritance(strategy = InheritanceType.JOINED)
@CurrentPrincipalField(ignoreforRoles = {"ROLE_ADMIN", "ROLE_SITE_OPERATOR"})
@JsonIgnoreProperties("pk")


@PreAuthorizeCreate(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)

@PreAuthorizeFindByIds(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizePatch(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindById(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeUpdate(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
public class BaseError extends AbstractSystemUuidPersistable implements PersistableError<String> {
    public static final String API_PATH = "allErrors";

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

    @CreatedBy
    @DiffIgnore
    @JsonIgnore
    @CurrentPrincipal
    @ApiModelProperty(value = "Created by", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby_id", referencedColumnName = "pk", updatable = false)
    private User createdBy;

    @NotNull
    @ApiModelProperty(value = "Message for users")
    @Column(name = "error_message", nullable = false, updatable = false, length = MAX_MESSAGE_LENGTH)
    private String message;

    @ApiModelProperty(value = "The address the request originated from")
    @Column(name = "remote_address", updatable = false, length = MAX_DESCRIPTION_LENGTH)
    private String remoteAddress;

    @ApiModelProperty(value = "User in context")
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @CurrentPrincipal
    private User user;

    @ApiModelProperty(value = "The UA string if provided with a request")
    @ManyToOne
    @JoinColumn(name = "user_agent_id", updatable = false)
    private UserAgent userAgent;

    @ManyToOne
    @JoinColumn(name = "error_log_id", updatable = false)
    private ErrorLog errorLog;

    public BaseError() {

    }

    protected BaseError(HttpServletRequest request, String message) {
        this.message = message;
        // note reguest details
        this.addRequestInfo(request);


    }

    public void addRequestInfo(HttpServletRequest request) {
        // get request info
        if (request != null) {

            if (this.remoteAddress == null) {
                this.remoteAddress = ErrorUtil.getRemoteAddress(request);
            }
            if (this.userAgent == null) {
                this.userAgent = ErrorUtil.getUserAgent(request);
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pk", this.getPk())
                .append("message", this.getMessage())
                .append("remoteAddress", this.getRemoteAddress())
                .toString();
    }

    @Override
    public void preSave() {
        super.preSave();
        if (this.getCreatedBy() != null && this.getCreatedBy().getPk() == null) {
            this.setCreatedBy(null);
        }
        if (StringUtils.isNotEmpty(this.message) && this.message.length() > BaseError.MAX_MESSAGE_LENGTH) {
            this.message = StringUtils.abbreviate(this.message, BaseError.MAX_MESSAGE_LENGTH);
        }
        if (StringUtils.isNotEmpty(this.remoteAddress) && this.remoteAddress.length() > BaseError.MAX_DESCRIPTION_LENGTH) {
            this.remoteAddress = StringUtils.abbreviate(this.remoteAddress, BaseError.MAX_DESCRIPTION_LENGTH);
        }
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }


    @JsonProperty("user")
    public UserDTO getUserInfo() {
        return UserDTO.fromUser(this.getUser());
    }


    @JsonIgnore
    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }

    @JsonIgnore
    @Override
    public ErrorLog getErrorLog() {
        return errorLog;
    }

    @JsonProperty
    @Override
    public void setErrorLog(ErrorLog errorLog) {
        this.errorLog = errorLog;
    }
}