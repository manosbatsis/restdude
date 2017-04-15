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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.audit.model.AbstractBasicAuditedModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Workflow metadata.
 */
@Entity
@Table
public class CaseWorkflow extends AbstractBasicAuditedModel {

    @NotNull
    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @NotNull
    @Column(nullable = false, length = 500)
    @Getter @Setter
    private String description;

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

    public CaseWorkflow(String name, String description, Space maintainerContext) {
        this.name = name;
        this.description = description;
        this.maintainerContext = maintainerContext;
    }

    public CaseWorkflow(String name, String description, Space businessContext, List<CaseStatus> statuses) {
        this(name, description, businessContext);
        this.statuses = statuses;
    }
}
