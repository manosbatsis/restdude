package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.annotation.CurrentPrincipal;
import com.restdude.mdd.annotation.CurrentPrincipalField;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.HttpUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;


@ModelResource(path = BaseError.API_PATH, controllerSuperClass = AbstractReadOnlyModelController.class,
        apiName = "Errors", apiDescription = "Generic error information (readonly)")
@ApiModel(value = "BaseError", description = "Generic error superclass")
@Entity
@Table(name = "error_abstract")
@Inheritance(strategy = InheritanceType.JOINED)
@CurrentPrincipalField(ignoreforRoles = {"ROLE_ADMIN", "ROLE_SITE_OPERATOR"})
public class BaseError extends AbstractSystemUuidPersistable implements PersistableError<String> {
    public static final String API_PATH = "allErrors";

    public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_CREATE = "denyAll";
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


    @NotNull
    @ApiModelProperty(value = "Message for user")
    @Column(name = "error_message", nullable = false, updatable = false)
    private String message;

    @NotNull
    @ApiModelProperty(value = "Date created")
    @Column(name = "date_created", nullable = false, updatable = false)
    private Date createdDate;

    @ApiModelProperty(value = "The address the request originated from")
    @Column(name = "remote_address", updatable = false, length = 500)
    private String remoteAddress;

    @ApiModelProperty(value = "User in context")
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @CurrentPrincipal
    private User user;

    @ApiModelProperty(value = "The UA string if provided with a request")
    @ManyToOne
    @JoinColumn(name = "user_agent_id", updatable = false)
    private UserAgent userAgent;

    @ManyToOne
    @JoinColumn(name = "error_log_id", updatable = false)
    private ErrorLog errorLog;

    public BaseError() {

    }

    protected BaseError(HttpServletRequest request, String message) {
        this.message = message;
        // note reguest details
        this.addRequestInfo(request);


    }

    public void addRequestInfo(HttpServletRequest request) {
        // get request info
        if (request != null) {

            if (this.remoteAddress == null) {
                this.remoteAddress = HttpUtil.getRemoteAddress(request);
            }
            if (this.userAgent == null) {
                this.userAgent = HttpUtil.getUserAgent(request);
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("message", this.getMessage())
                .append("userAgent", this.getUserAgent())
                .toString();
    }

    @Override
    public void preSave() {
        super.preSave();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(new Date());
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }


    @JsonProperty("user")
    public UserDTO getUserInfo() {
        return UserDTO.fromUser(this.getUser());
    }


    @JsonIgnore
    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }

    @JsonIgnore
    @Override
    public ErrorLog getErrorLog() {
        return errorLog;
    }

    @JsonProperty
    @Override
    public void setErrorLog(ErrorLog errorLog) {
        this.errorLog = errorLog;
    }
}