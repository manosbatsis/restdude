package com.restdude.domain.error.model;

import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractAssignedIdPersistable;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.HashUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@ModelResource(path = UserAgent.API_PATH, controllerSuperClass = AbstractReadOnlyModelController.class,
        apiName = "User Agents", apiDescription = "Collection of UA signatures")
@ApiModel(value = "UserAgent", description = "UA signatures")
@Entity
@Table(name = "user_agent")
public class UserAgent extends AbstractAssignedIdPersistable<String> {

    public static final String API_PATH = "userAgents";

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

    public static final int MAX_VALUE_LENGTH = 1600;


    @NotNull
    @ApiModelProperty(value = "UA string value")
    @Column(name = "ua_value", nullable = false, updatable = false, length = MAX_VALUE_LENGTH)
    private String value;


    protected UserAgent() {
        super();
    }

    public UserAgent(String id, String value) {
        super(id);
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("value", this.getValue())
                .toString();
    }


    @Override
    public void preSave() {
        if (StringUtils.isNotEmpty(this.value)) {
            if (this.getId() == null) {
                this.setId(HashUtils.buildHash(this.value));
            }
            if (this.value.length() > UserAgent.MAX_VALUE_LENGTH) {
                this.value = StringUtils.abbreviate(this.value, UserAgent.MAX_VALUE_LENGTH);
            }
        }

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}