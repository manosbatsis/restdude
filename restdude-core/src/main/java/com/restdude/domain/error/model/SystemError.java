package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.HttpUtil;
import com.restdude.util.exception.http.ConstraintViolationException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

@ModelResource(path = SystemError.API_PATH, controllerSuperClass = AbstractReadOnlyModelController.class,
		apiName = "System Errors", apiDescription = "System Error Operations (readonly)")
@Entity
@Table(name = "error_system")
@ApiModel(value = "SystemError", description = "System validationErrors are created exclusively by the system " +
		"(i.e. without manual intervention) to handle and inform the user about runtime exceptions. "
		+ "They may be persisted automatically according to calipso.validationErrors.system.persist* configuration properties. "
		+ "System validationErrors have a many-to-one relationship with StackTrace records, as those are shared based on their hash to save space. ")
@JsonPropertyOrder({"id", "message", "createdDate", "httpStatusCode", "requestMethod", "requestUrl",
		"validationErrors", "user"})
public class SystemError extends AbstractError {

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

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@Transient
	private Throwable throwable;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "stacktrace_id", updatable = false)
	private StackTrace stackTrace;

	public SystemError(HttpServletRequest request, Integer httpStatusCode, String message, Throwable throwable) {
		super(message);
		if (httpStatusCode == null) {
			throw new NullPointerException("httpStatusCode argument cannot be null.");
		}
		// set status
		this.httpStatusCode = httpStatusCode;

		// set default message if appropriate
		if (StringUtils.isBlank(message)) {
			this.setMessage(HttpStatus.valueOf(httpStatusCode).getReasonPhrase());
		}

		this.throwable = throwable;

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


	public SystemError(HttpServletRequest request, Integer httpStatusCode, Throwable throwable) {
		this(request, httpStatusCode, null, throwable);
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

	public Set<ConstraintViolationEntry> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(Set<ConstraintViolationEntry> validationErrors) {
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

	public static class Builder {
		private String requestMethod;
		private String requestUrl;
		private Integer httpStatusCode;
		private Set<ConstraintViolationEntry> validationErrors;
		private Throwable throwable;
		private StackTrace stackTrace;

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

		public Builder stackTrace(StackTrace stackTrace) {
			this.stackTrace = stackTrace;
			return this;
		}

		public SystemError build() {
			return new SystemError(this);
		}
	}

	private SystemError(Builder builder) {
		this.requestMethod = builder.requestMethod;
		this.requestUrl = builder.requestUrl;
		this.httpStatusCode = builder.httpStatusCode;
		this.validationErrors = builder.validationErrors;
		this.throwable = builder.throwable;
		this.stackTrace = builder.stackTrace;
	}
}