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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.dto.CaseStatustInfo;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.util.UUID;

@Slf4j
@Entity
@Table(name = "case_base")
@ModelResource(pathFragment = BaseCase.API_PATH_FRAGMENT,
        apiDescription = "Cases management")
@ApiModel(description = BaseCase.API_MODEL_DESCRIPTION)
public  class BaseCase<C extends BaseCase<C, CC>, CC extends BaseCaseComment<C, CC>>
        extends RetreivableResource<SpaceCasesApp, BaseCase>
        /*implements ICaseModel<SpaceCasesApp, CC>*/ {

    public static final String API_PATH_FRAGMENT = "cases";
    public static final String API_MODEL_DESCRIPTION = "A model representing a case, such as an issue, ticket, note etc.";


    @Column(name = "entry_index")
    @Getter
    @Setter
    private Integer entryIndex;

    @ApiModelProperty(value = "Current case status", allowableValues = "OPEM, CLOSED, [CUSTOM_VALUE]")
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    private CaseStatus status;

    @ApiModelProperty(value = "Assigned to", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.EAGER  )
    @JoinColumn(name = "assigned_to", referencedColumnName = "id", updatable = false)
    @Getter @Setter
    private User assignee;

    @JsonIgnore
    public CaseStatus getStatus() {
        return status;
    }

    @JsonGetter("status")
    public CaseStatustInfo getStatusrDTO() {
        return CaseStatustInfo.from(this.status);
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }

    public BaseCase() {
        super();
    }


    public BaseCase(String title, String detail) {
        super();
        this.setTitle(title);
        this.setDetail(detail);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("name", this.getName())
                .append("entryIndex", this.getEntryIndex())
                .append("title", this.getTitle())
                .append("status", this.getStatus())
                .toString();
    }

    @Override
    public void preSave() {

        if(StringUtils.isBlank(this.getName())){
            this.setName(UUID.randomUUID().toString());
            log.debug("preSave, tmp name: {}", this.getName());
        }
        log.debug("preSave, before: {}", this);
        super.preSave();


        log.debug("preSave, after: {}", this);
    }

}
