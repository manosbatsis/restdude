package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.restdude.util.HttpUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Entity
@Table(name = "system_error")
@ApiModel(value = "SystemError", description = "System validationErrors are created exclusively by the system (i.e. without manual intervention) to handle and inform the user about runtime exceptions. " +
        "They may be persisted automatically according to calipso.validationErrors.system.persist* configuration properties. " +
        "System validationErrors have a many-to-one relationship with StackTrace records, as those are shared based on their hash to save space. ")
@JsonPropertyOrder({"id", "message", "createdDate", "httpStatusCode", "requestMethod", "requestUrl", "validationErrors", "user"})
public class SystemError extends AbstractError {

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
    private Set<ConstraintViolation> validationErrors;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Transient
    private Throwable throwable;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "stacktrace_id", updatable = false)
    private StackTrace stackTrace;


    public SystemError(HttpServletRequest request, Integer httpStatusCode, String message, Set<ConstraintViolation> validationErrors, Throwable throwable) {
        super(message);
        if (httpStatusCode == null) {
            throw new NullPointerException("httpStatusCode argument cannot be null.");
        }
        // set status
        this.httpStatusCode = httpStatusCode;
        if (StringUtils.isBlank(message)) {
            this.setMessage(HttpStatus.valueOf(httpStatusCode).getReasonPhrase());
        }
        this.validationErrors = validationErrors;
        this.throwable = throwable;
        // request details
        if (request != null) {
            this.requestMethod = request.getMethod();
            String baseUrl = HttpUtil.setBaseUrl(request);
            this.requestUrl = request.getRequestURL().append('?').append(request.getQueryString()).substring(baseUrl.length());
        }

    }

    public SystemError(HttpServletRequest request, Integer httpStatusCode, String message, Throwable throwable) {
        this(request, httpStatusCode, message, null, throwable);
    }

    public SystemError(HttpServletRequest request, Integer httpStatusCode, Throwable throwable) {
        this(request, httpStatusCode, null, null, throwable);
    }

    public SystemError() {
        super();
    }

    @JsonGetter("httpStatusMessage")
    @ApiModelProperty(value = "The phrase corresponding to the HTTP status")
    public String getHttpStatusMessage() {
        return HttpStatus.valueOf(this.getHttpStatusCode()).getReasonPhrase();
    }

    @JsonGetter("stackTraceId")
    @ApiModelProperty(value = "The corresponding stacktrace ID, if any")
    public String getStackTraceId() {
        return this.getStackTrace() != null ? this.getStackTrace().getId() : null;
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

    public Set<ConstraintViolation> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Set<ConstraintViolation> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public StackTrace getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTrace stackTrace) {
        this.stackTrace = stackTrace;
    }

}