package com.restdude.domain.fs;

import java.io.InputStream;

public class FileDTO {

	private long contentLength;
	private String contentType;
	private InputStream in;
	private String path;

	public FileDTO() {
		super();
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static class Builder {
		private long contentLength;
		private String contentType;
		private InputStream in;
		private String path;

		public Builder contentLength(long contentLength) {
			this.contentLength = contentLength;
			return this;
		}

		public Builder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder in(InputStream in) {
			this.in = in;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public FileDTO build() {
			return new FileDTO(this);
		}
	}

	private FileDTO(Builder builder) {
		this.contentLength = builder.contentLength;
		this.contentType = builder.contentType;
		this.in = builder.in;
		this.path = builder.path;
	}
}
