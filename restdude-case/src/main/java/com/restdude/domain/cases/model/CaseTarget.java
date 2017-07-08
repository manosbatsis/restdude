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
package com.restdude.domain.cases.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restdude.domain.misc.model.Host;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractNoDeletePersistableModelController;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * {@value CLASS_DESCRIPTION}
 */
@Entity
@Table(name = "case_target")
@ModelResource(pathFragment = CaseTarget.API_PATH, controllerSuperClass = AbstractNoDeletePersistableModelController.class,
         apiDescription = CaseTarget.CLASS_DESCRIPTION)
@ApiModel(description = CaseTarget.CLASS_DESCRIPTION)
@Builder
public class CaseTarget extends AbstractSystemUuidPersistableModel {

    public static final String API_PATH = "caseTargets";
    public static final String CLASS_DESCRIPTION = "Entity model for case targets, typically web pages";

    @Getter @Setter
    @ApiModelProperty(value = "The absolute path of this target excluding the protocol, host and any query parameters")
    private String path;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "host", nullable = false, updatable = false)
    @Getter @Setter
    @ApiModelProperty(value = "The blox host", required = true)
    private Host host;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "context", nullable = false, updatable = false)
    @Getter @Setter
    @ApiModelProperty(value = "The blox host", required = true)
    private BaseContext context;

    public CaseTarget() {
        super();
    }

    public CaseTarget(String path, Host host, BaseContext context) {
        this.path = path;
        this.host = host;
        this.context = context;
    }



}