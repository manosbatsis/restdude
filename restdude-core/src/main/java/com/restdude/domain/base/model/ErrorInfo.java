package com.restdude.domain.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

@ApiModel(value = "ErrorInfo", description = "DTO for error information responses")
public class ErrorInfo {

    @ApiModelProperty(value = "The HTTP status code ")
    private final int code;

    @ApiModelProperty(value = "The phrase corresponding to the HTTP status")
    private final String status;

    /**
     * Message for end users
     **/
    @ApiModelProperty(value = "Message for user")
    private final String message;

    @ApiModelProperty(value = "List of specific error items, if any")
    private List<String> errors;

    @ApiModelProperty(value = "Message for technical staff")
    private final String developerMessage;

    @ApiModelProperty(value = "URL where technical staff can get more information")
    private final String moreInfoUrl;

    @ApiModelProperty(value = "The error exception")
    private final Throwable throwable;


    public ErrorInfo(String status, int code, String message, String developerMessage, String moreInfoUrl, List<String> errors, Throwable throwable) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.moreInfoUrl = moreInfoUrl;
        this.errors = errors;
        this.throwable = throwable;

    }

    public ErrorInfo(String status, int code, String message, String developerMessage, String moreInfoUrl, Throwable throwable) {
        this(status, code, message, developerMessage, moreInfoUrl, null, throwable);
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return StringUtils.isNotBlank(this.message) ? this.message : this.status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", this.code)
                .append("status", this.status)
                .append("message", this.message)
                .append("errors", this.errors)
                .toString();
    }

    public static class Builder {

        private int code;
        private String status;
        private String message;
        private String developerMessage;
        private String moreInfoUrl;
        private List<String> errors;
        private Throwable throwable;

        public Builder() {
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public Builder moreInfoUrl(String moreInfoUrl) {
            this.moreInfoUrl = moreInfoUrl;
            return this;
        }

        public Builder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public Builder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public Builder error(String error) {
            if (this.errors == null) {
                this.errors = new LinkedList<String>();
            }
            this.errors.add(error);
            return this;
        }

        public ErrorInfo build() {
            if (this.status == null) {
                this.status = "Internal Server Error";
            }
            return new ErrorInfo(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl, this.errors, this.throwable);
        }
    }
}