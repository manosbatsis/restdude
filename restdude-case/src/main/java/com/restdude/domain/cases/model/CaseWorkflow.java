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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.audit.model.AbstractBasicAuditedModel;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Workflow metadata.
 */
@Entity
@Table(name = "case_workflow")
@ModelResource(pathFragment = CaseWorkflow.API_PATH_FRAGMENT,
        apiDescription = "Cases workflow management")
@ApiModel(description = CaseWorkflow.API_MODEL_DESCRIPTION)
public class CaseWorkflow extends AbstractBasicAuditedModel {

    public static final String API_PATH_FRAGMENT = "workflows";
    public static final String API_MODEL_DESCRIPTION = "A model representing a cases application workflow.";

    @NotNull
    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @NotNull
    @Column(nullable = false, length = 500)
    @Getter @Setter
    private String detail;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "maintainer_context", nullable = false, updatable = false)
    @ApiModelProperty(value = "The context that controls this workflow (VS just using it)", required = true)
    @Getter @Setter
    private Space maintainerContext;

    @OneToMany(mappedBy = "workflow", orphanRemoval = true)
    @JsonIgnore
    @Getter @Setter
    @ApiModelProperty(value = "The status options")
    private List<CaseStatus> statuses;

    public CaseWorkflow(){

    }

    public CaseWorkflow(String name, String detail, Space maintainerContext) {
        this.name = name;
        this.detail = detail;
        this.maintainerContext = maintainerContext;
    }

    public CaseWorkflow(String name, String detail, Space businessContext, List<CaseStatus> statuses) {
        this(name, detail, businessContext);
        this.statuses = statuses;
    }
}
