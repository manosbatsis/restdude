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

import com.restdude.domain.audit.model.AbstractBasicAuditedModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.restdude.domain.CommentableModel.MAX_DETAIL_LENGTH;

/**
 * BaseCase status option
 */
@Entity
@Table(name = "case_status")
public class CaseStatus extends AbstractBasicAuditedModel {

    public static final String UNASSIGNED = "Unassigned";
    public static final String OPEN = "Open";
    public static final String CLOSED = "Closed";

    @NotNull
    @ApiModelProperty(value = "Short description, i.e. one or two words")
    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @ApiModelProperty(value = "Human-readable description")
    @Column(nullable = false, updatable = true, length = MAX_DETAIL_LENGTH)
    @Getter
    @Setter
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @ApiModelProperty(value = "The workflow this status belongs to", required = true)
    @Getter @Setter
    private CaseWorkflow workflow;

    public CaseStatus() {
    }

    public CaseStatus(String name, String description, CaseWorkflow workflow) {
        this.name = name;
        this.description = description;
        this.workflow = workflow;
    }
}
