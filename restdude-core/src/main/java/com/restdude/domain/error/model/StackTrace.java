package com.restdude.domain.error.model;

import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractAssignedidPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 */
@ModelResource(path = "stackTraces", controllerSuperClass = AbstractReadOnlyModelController.class, apiName = "Stack Traces", apiDescription = "SystemError Stacktraces")

@Entity
@Table(name = "stack_trace")
@ApiModel(value = "StackTrace", description = "Used to persist SystemError stacktraces using the corresponding hash as the ID. "
		+ "The generated hash ignores line numbers and is thus tolerant of small code changes, like adding a comment line.")
public class StackTrace extends AbstractAssignedidPersistable<String> {

	@Formula(" (id) ")
	private String name;

	@ApiModelProperty(value = "The root cause message.")
	@Column(name = "root_cause_msg", length = AbstractError.MAX_MESSAGE_LENGTH)
	private String rootCauseMessage;

	@Column(name = "last_occurred")
	private Date lastOccurred;

	@Formula(" (select count(*) from error_system syserr where syserr.stacktrace_id = id) ")
	@ApiModelProperty(value = "The number of errors corresponding to this stacktrace.")
	private Integer errorCount = 0;

	@ApiModelProperty(value = "The actual stacktrace.")
	@Column(length = AbstractError.MAX_STACKTRACE_LENGTH)
	@NotNull
	private String stacktrace;

	public StackTrace() {
	}

	public StackTrace(String hash, String rootCauseMessage, Date lastOccured, String stacktrace) {
		this.setId(hash);
		this.rootCauseMessage = rootCauseMessage;
		this.lastOccurred = lastOccured;
		this.stacktrace = stacktrace;
	}

	@Override
	public void preSave() {
		if (StringUtils.isNotEmpty(this.rootCauseMessage)
				&& this.rootCauseMessage.length() > AbstractError.MAX_MESSAGE_LENGTH) {
			this.rootCauseMessage = StringUtils.abbreviate(this.rootCauseMessage, AbstractError.MAX_MESSAGE_LENGTH);
		}
		if (StringUtils.isNotEmpty(this.stacktrace) && this.stacktrace.length() > AbstractError.MAX_STACKTRACE_LENGTH) {
			this.stacktrace = StringUtils.abbreviate(this.stacktrace, AbstractError.MAX_STACKTRACE_LENGTH);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRootCauseMessage() {
		return rootCauseMessage;
	}

	public void setRootCauseMessage(String rootCauseMessage) {
		this.rootCauseMessage = rootCauseMessage;
	}

	public Date getLastOccurred() {
		return lastOccurred;
	}

	public void setLastOccurred(Date lastOccurred) {
		this.lastOccurred = lastOccurred;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public static class Builder {
		private String id;
		private String rootCauseMessage;
		private Date lastOccurred;
		private Integer errorCount;
		private String stacktrace;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder rootCauseMessage(String rootCauseMessage) {
			this.rootCauseMessage = rootCauseMessage;
			return this;
		}

		public Builder lastOccurred(Date lastOccurred) {
			this.lastOccurred = lastOccurred;
			return this;
		}

		public Builder errorCount(Integer errorCount) {
			this.errorCount = errorCount;
			return this;
		}

		public Builder stacktrace(String stacktrace) {
			this.stacktrace = stacktrace;
			return this;
		}

		public StackTrace build() {
			return new StackTrace(this);
		}
	}

	private StackTrace(Builder builder) {
		this.setId(builder.id);
		this.rootCauseMessage = builder.rootCauseMessage;
		this.lastOccurred = builder.lastOccurred;
		this.errorCount = builder.errorCount;
		this.stacktrace = builder.stacktrace;
	}
}
