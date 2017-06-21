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
package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.auth.spel.annotations.*;
import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.domain.cases.model.BaseCase;
import com.restdude.domain.error.ErrorUtil;
import com.restdude.mdd.annotation.model.CurrentPrincipalField;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractReadOnlyPersistableModelController;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;


import static com.restdude.domain.CommentableModel.*;


import static com.restdude.domain.CommentableModel.*;

@Entity
@Table(name = "error_base")
@ModelResource(pathFragment = BaseError.API_PATH, controllerSuperClass = AbstractReadOnlyPersistableModelController.class,
        apiName = "Errors", apiDescription = "Generic error information (readonly)")
@ApiModel(value = "BaseError", description = "Generic error superclass")
@CurrentPrincipalField(ignoreforRoles = {"ROLE_ADMIN", "ROLE_SITE_OPERATOR"})

@PreAuthorizeCreate(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindByIds(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizePatch(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindById(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeUpdate(controller = SpelUtil.HAS_ROLE_ADMIN_OR_OPERATOR, service = SpelUtil.PERMIT_ALL)
@Slf4j
@JsonIgnoreProperties("description")
public class BaseError extends BaseCase<BaseError, ErrorComment> implements  PersistableError {
    public static final String API_PATH = "allErrors";


    @ApiModelProperty(value = "The address the request originated from", readOnly = true)
    @Column(name = "remote_address", updatable = false, length = MAX_DETAIL_LENGTH)
    @Getter @Setter
    private String remoteAddress;

    @ApiModelProperty(value = "The UA string if provided with a request")
    @ManyToOne
    @JoinColumn(name = "user_agent_id", updatable = false)
    @Getter @Setter
    private UserAgent userAgent;

    @ManyToOne
    @JoinColumn(name = "error_log_id", updatable = false)
    private ErrorLog errorLog;

    public BaseError() {
        super();
    }

    protected BaseError(HttpServletRequest request, String title) {
        this();
        this.setTitle(title);
        // note reguest details
        this.addRequestInfo(request);
    }

    protected BaseError(HttpServletRequest request, String title, String detail) {
        super(title, detail);
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
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("remoteAddress", this.getRemoteAddress())
                .toString();
    }

    @Override
    public void preSave() {
        super.preSave();
        if (StringUtils.isNotEmpty(this.remoteAddress) && this.remoteAddress.length() > MAX_DETAIL_LENGTH) {
            this.remoteAddress = StringUtils.abbreviate(this.remoteAddress, MAX_DETAIL_LENGTH);
        }
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