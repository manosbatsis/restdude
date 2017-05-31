/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.error.model;

import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractReadOnlyPersistableModelController;
import com.restdude.mdd.model.AbstractAssignedIdPersistableModel;
import com.restdude.util.HashUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.hateoas.core.Relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@ModelResource(pathFragment = UserAgent.API_PATH, controllerSuperClass = AbstractReadOnlyPersistableModelController.class,
        apiName = "User Agents", apiDescription = "Collection of UA signatures")
@ApiModel(value = "UserAgent", description = "UA signatures")
@Relation(value = "userAgent", collectionRelation = UserAgent.API_PATH)
@Entity
@Table(name = "user_agent")
public class UserAgent extends AbstractAssignedIdPersistableModel<String> {

    public static final String API_PATH = "userAgents";

    public static final int MAX_VALUE_LENGTH = 1600;


    @NotNull
    @ApiModelProperty(value = "UA signature string pathFragment")
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
                .append("pathFragment", this.getValue())
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