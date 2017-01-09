/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-error, https://manosbatsis.github.io/restdude/restdude-error
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

    public static String SERVICE_PRE_AUTHORIZE_SEARCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_CREATE = "hasRole('ROLE_USER')";
    public static String SERVICE_PRE_AUTHORIZE_UPDATE = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_PATCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_VIEW = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_DELETE = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";

    public static String SERVICE_PRE_AUTHORIZE_DELETE_BY_ID = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_DELETE_ALL = "denyAll";
    public static String SERVICE_PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
    public static String SERVICE_PRE_AUTHORIZE_FIND_BY_IDS = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_FIND_ALL = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static String SERVICE_PRE_AUTHORIZE_COUNT = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";

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