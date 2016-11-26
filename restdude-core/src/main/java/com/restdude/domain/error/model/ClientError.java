package com.restdude.domain.error.model;

import com.restdude.domain.fs.FilePersistence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "error_client")
@ApiModel(value = "ClientError", description = "Client errors are created upon client request and refer to exceptions occurred " +
		"specifically within client application code. ")
public class ClientError extends AbstractError {

	public static final String API_PATH = "clientErrors";

	@ApiModelProperty(value = "A client application screenshot demonstrating the issue.")
	@FilePersistence(maxWidth = 1920, maxHeight = 1080)
	@Column(name = "screenshot_url")
	private String screenshotUrl;

	@ApiModelProperty(value = "The error description provided by the user, if any.")
	@Column(length = AbstractError.MAX_STACKTRACE_LENGTH)
	private String description;

	@ApiModelProperty(value = "The client application error log")
	@Column(length = AbstractError.MAX_STACKTRACE_LENGTH)
	@NotNull
	private String errorLog;

	public ClientError() {
	}

	@Override
	public void preSave() {
		if (StringUtils.isNotEmpty(this.description) && this.description.length() > AbstractError.MAX_MESSAGE_LENGTH) {
			this.description = StringUtils.abbreviate(this.description, AbstractError.MAX_MESSAGE_LENGTH);
		}
		if (StringUtils.isNotEmpty(this.errorLog) && this.errorLog.length() > AbstractError.MAX_STACKTRACE_LENGTH) {
			this.errorLog = StringUtils.abbreviate(this.errorLog, AbstractError.MAX_STACKTRACE_LENGTH);
		}
	}

	public String getScreenshotUrl() {
		return screenshotUrl;
	}

	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public static class Builder {
		private String message;
		private String description;
		private String errorLog;

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder errorLog(String errorLog) {
			this.errorLog = errorLog;
			return this;
		}

		public ClientError build() {
			return new ClientError(this);
		}
	}

	private ClientError(Builder builder) {
		this.setMessage(builder.message);
		this.description = builder.description;
		this.errorLog = builder.errorLog;
	}
}