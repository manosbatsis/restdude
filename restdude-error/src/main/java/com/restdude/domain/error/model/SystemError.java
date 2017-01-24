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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.HttpUtil;
import com.restdude.util.exception.http.ConstraintViolationException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ModelResource(path = SystemError.API_PATH, controllerSuperClass = AbstractModelController.class,
        apiName = "System Errors", apiDescription = "System Error Operations (readonly)")
@Entity
@Table(name = "error_system")
@ApiModel(value = "SystemError", description = "SystemErrors are created exclusively by the system " +
        "(i.e. without manual intervention) to handle and inform the user about runtime exceptions. "
        + "They may be persisted automatically according to restdude.validationErrors.system.persist* configuration properties. "
        + "System validationErrors have a many-to-one relationship with ErrorLog records, as those are shared based on their hash to save space. ")
@JsonPropertyOrder({"message", "createdDate", "httpStatusCode", "requestMethod", "requestUrl",
        "validationErrors", "user"})
@JsonIgnoreProperties("pk")
public class SystemError extends BaseError {

    public static final String API_PATH = "systemErrors";

    @ApiModelProperty(value = "The HTTP request method")
    @Column(name = "request_method", updatable = false)
    private String requestMethod;

    @ApiModelProperty(value = "The HTTP request URL, relative to system base URL")
    @Column(name = "request_url", updatable = false, length = 500)
    private String requestUrl;


    @ApiModelProperty(value = "The HTTP response status code ")
    @Column(name = "status_code", updatable = false)
    private Integer httpStatusCode;

    @ApiModelProperty(value = "Failed constraint validation errors, if any")
    @Transient
    private Set<ConstraintViolationEntry> validationErrors;

    @JsonIgnore
    @Transient
    private Map<String, String> responseHeaders;

    @JsonIgnore
    @Transient
    private Throwable throwable;


    private SystemError() {
        super();
    }

    public SystemError(HttpServletRequest request, String message, Integer httpStatusCode, Throwable throwable) {
        super(request, message);
        this.throwable = throwable;
        if (httpStatusCode == null) {
            throw new NullPointerException("httpStatusCode argument cannot be null.");
        }
        // set status
        this.httpStatusCode = httpStatusCode;

        // set default message if appropriate
        if (StringUtils.isBlank(message)) {
            this.setMessage(HttpStatus.valueOf(httpStatusCode).getReasonPhrase());
        }

        // add error log
        this.setErrorLog(new ErrorLog(throwable));

        // add validation errors, if any
        if (ConstraintViolationException.class.isAssignableFrom(throwable.getClass())) {
            Set<ConstraintViolation> violations = ((ConstraintViolationException) throwable).getConstraintViolations();
            if (CollectionUtils.isNotEmpty(violations)) {
                this.validationErrors = new HashSet<ConstraintViolationEntry>();
                for (ConstraintViolation violation : violations) {
                    this.validationErrors.add(new ConstraintViolationEntry(violation));
                }
            }
        }

    }

    public void addRequestInfo(HttpServletRequest request) {
        super.addRequestInfo(request);
        // request details
        if (request != null) {
            this.requestMethod = request.getMethod();
            String baseUrl = HttpUtil.setBaseUrl(request);
            StringBuffer reUrl = request.getRequestURL();

            // add query string if any
            String queryString = request.getQueryString();
            if (StringUtils.isNoneBlank(queryString)) {
                reUrl.append('?').append(queryString);
            }

            this.requestUrl = reUrl.substring(baseUrl.length());
        }
    }

    @JsonGetter("httpStatusMessage")
    @ApiModelProperty(value = "The phrase corresponding to the HTTP status")
    public String getHttpStatusMessage() {
        return HttpStatus.valueOf(this.getHttpStatusCode()).getReasonPhrase();
    }

    @JsonGetter("errorLogId")
    @ApiModelProperty(name = "errorLogId", value = "The corresponding log/stacktrace ID, if any")
    public String getStackTraceId() {
        return this.getErrorLog() != null ? this.getErrorLog().getPk() : null;
    }


    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }


    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Set<ConstraintViolationEntry> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Set<ConstraintViolationEntry> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public static class Builder {

        private String message;
        private User user;

        private String requestMethod;
        private String requestUrl;
        private Integer httpStatusCode;
        private Set<ConstraintViolationEntry> validationErrors;
        private Throwable throwable;
        private ErrorLog errorLog;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public Builder httpStatusCode(Integer httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public Builder validationErrors(Set<ConstraintViolationEntry> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public Builder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public Builder stackTrace(ErrorLog errorLog) {
            this.errorLog = errorLog;
            return this;
        }

        public SystemError build() {
            return new SystemError(this);
        }
    }

    private SystemError(Builder builder) {

        this.setMessage(builder.message);
        this.setUser(builder.user);

        this.requestMethod = builder.requestMethod;
        this.requestUrl = builder.requestUrl;
        this.httpStatusCode = builder.httpStatusCode;
        this.validationErrors = builder.validationErrors;
        this.setErrorLog(builder.errorLog);
    }
}