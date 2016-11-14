package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@ModelResource(path = "errors", controllerSuperClass = AbstractModelController.class,
        apiName = "Errors", apiDescription = "Error Operations")
@Entity
@Table(name = "error_info")
@Inheritance(strategy = InheritanceType.JOINED)
@ApiModel(value = "ErrorInfo", description = "Used to persist error of the application or it's clients.")
public class ErrorInfo extends AbstractSystemUuidPersistable {

    public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_CREATE = "hasRole('ROLE_USER')";
    public static final String PRE_AUTHORIZE_UPDATE = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_PATCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_VIEW = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_DELETE = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";

    public static final String PRE_AUTHORIZE_DELETE_BY_ID = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_DELETE_ALL = "denyAll";
    public static final String PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
    public static final String PRE_AUTHORIZE_FIND_BY_IDS = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_FIND_ALL = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_COUNT = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";

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
    @JsonIgnore
    @Transient
    private List<String> errors;

    @ApiModelProperty(value = "The error exception")
    @JsonIgnore
    @Transient
    private final Throwable throwable;


    public ErrorInfo(String status, int code, String message, List<String> errors, Throwable throwable) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.throwable = throwable;

    }

    public ErrorInfo(String status, int code, String message, Throwable throwable) {
        this(status, code, message, null, throwable);
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
            return new ErrorInfo(this.status, this.code, this.message, this.errors, this.throwable);
        }
    }
}