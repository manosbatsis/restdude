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
import com.restdude.domain.users.model.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Status transition configuration
 */
@Entity
@Table(name = "case_status_option")
public class CaseStatusOption extends AbstractBasicAuditedModel {

    @ApiModelProperty(value = "The current status")
    @ManyToOne
    @JoinColumn(name = "current_status", updatable = false, nullable = false)
    @Getter @Setter
    private CaseStatus currentStatus;

    @ApiModelProperty(value = "The candidate next status option")
    @ManyToOne
    @JoinColumn(name = "candidate_status", updatable = false, nullable = false)
    @Getter @Setter
    private CaseStatus candidateStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "candidate_status_options_roles", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {
            @JoinColumn(name = "candidate_status_option_id")})
    private List<Role> roles;

    public CaseStatusOption() {
    }

    public CaseStatusOption(CaseStatus currentStatus, CaseStatus candidateStatus, List<Role> roles) {
        this.currentStatus = currentStatus;
        this.candidateStatus = candidateStatus;
        this.roles = roles;
    }
}
