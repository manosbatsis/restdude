package com.restdude.domain.error.model;

import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.CurrentPrincipal;
import com.restdude.mdd.annotation.CurrentPrincipalField;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@ModelResource(path = AbstractError.API_PATH, controllerSuperClass = AbstractReadOnlyModelController.class,
        apiName = "Errors", apiDescription = "Error Operations (readonly)")
@ApiModel(value = "AbstractError", description = "Generic error superclass")
@Entity
@Table(name = "error_abstract")
@Inheritance(strategy = InheritanceType.JOINED)
@CurrentPrincipalField(ignoreforRoles = {"ROLE_ADMIN", "ROLE_SITE_OPERATOR"})
public class AbstractError extends AbstractSystemUuidPersistable {
    public static final String API_PATH = "allErrors";

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

    public static final int MAX_MESSAGE_LENGTH = 500;
    public static final int MAX_STACKTRACE_LENGTH = 20000;

    @NotNull
    @ApiModelProperty(value = "Message for user")
    @Column(name = "error_message", nullable = false, updatable = false)
    private String message;

    @ApiModelProperty(value = "Date created")
    @NotNull
    @Column(name = "date_created", nullable = false, updatable = false)
    private Date createdDate;

    @ApiModelProperty(value = "User in context")
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @CurrentPrincipal
    private User user;


    public AbstractError() {

    }

    public AbstractError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("message", this.getMessage())
                .toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}