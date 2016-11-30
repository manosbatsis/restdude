package com.restdude.domain.fs;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.InputStream;

public class FileDTO {

	private long contentLength;
	private String contentType;
	private InputStream in;
	private String path;
	private File tmpFile;

	public FileDTO() {
		super();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("path", this.getPath())
				.append("contentLength", this.getContentLength())
				.append("contentType", this.getContentType())
				.toString();
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

	public File getTmpFile() {
		return tmpFile;
	}

	public void setTmpFile(File tmpFile) {
		this.tmpFile = tmpFile;
	}

	public static class Builder {
		private long contentLength;
		private String contentType;
		private InputStream in;
		private String path;
		private File tmpFile;

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

		public Builder tmpFile(File tmpFile) {
			this.tmpFile = tmpFile;
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
		this.tmpFile = builder.tmpFile;
	}
}
