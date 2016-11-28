package com.restdude.domain.error.model;

import com.restdude.domain.fs.FilePersistence;
import com.restdude.domain.fs.FilePersistencePreview;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

@ModelResource(path = ClientError.API_PATH,
		apiName = "Client Errors", apiDescription = "Client Error Operations")
@Entity
@Table(name = "error_client")
@ApiModel(value = "ClientError", description = "Client errors are created upon client request and refer to exceptions occurred " +
		"specifically within client application code. ")
public class ClientError extends BaseError implements PersistableError<String> {

	public static final String API_PATH = "clientErrors";

	@ApiModelProperty(value = "A client application screenshot demonstrating the issue.")
	@FilePersistence(maxWidth = 1920, maxHeight = 1080)
	@FilePersistencePreview(maxWidth = 200, maxHeight = 200)
	@Column(name = "screenshot_url")
	private String screenshotUrl;

	@ApiModelProperty(value = "The error description provided by the user, if any.")
	@Column(length = BaseError.MAX_MESSAGE_LENGTH)
	private String description;

	public ClientError() {
	}

	public ClientError(HttpServletRequest request, String message, String screenshotUrl, String description) {
		super(request, message);
		this.screenshotUrl = screenshotUrl;
		this.description = description;
	}

	@Override
	public void addRequestInfo(HttpServletRequest request) {
		super.addRequestInfo(request);
	}

	@Override
	public void preSave() {
		super.preSave();
		if (StringUtils.isNotEmpty(this.description) && this.description.length() > BaseError.MAX_MESSAGE_LENGTH) {
			this.description = StringUtils.abbreviate(this.description, BaseError.MAX_MESSAGE_LENGTH);
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

	public static class Builder {
		private String message;
		private User user;
		private String screenshotUrl;
		private String description;

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public Builder screenshotUrl(String screenshotUrl) {
			this.screenshotUrl = screenshotUrl;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public ClientError build() {
			return new ClientError(this);
		}
	}

	private ClientError(Builder builder) {
		this.setMessage(builder.message);
		this.setUser(builder.user);
		this.screenshotUrl = builder.screenshotUrl;
		this.description = builder.description;
	}
}