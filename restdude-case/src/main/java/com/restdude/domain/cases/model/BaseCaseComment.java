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

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.cases.model.dto.CaseStatustInfo;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.javers.core.metamodel.annotation.DiffIgnore;

/**
 * Base case comment implementation
*/
@Slf4j
@Entity
@Table(name = "case_comment_base")
@ModelResource(pathFragment = BaseCaseComment.API_PATH_FRAGMENT,
        apiDescription = "Case comments management")
@ApiModel(description = BaseCaseComment.API_MODEL_DESCRIPTION)
public class BaseCaseComment<T extends BaseCase<T, C>, C extends BaseCaseComment<T, C>>
        extends RetreivableResource<BaseCase, BaseCaseComment> /*implements ICaseCommentModel<T, C>*/ {

    public static final String API_PATH_FRAGMENT = "caseComments";
    public static final String API_MODEL_DESCRIPTION = "A model representing a case comment.";

    // start parent case patch
    @ApiModelProperty(value = "The case priority")
    @Getter @Setter
    private String priority;

    @ApiModelProperty(value = "Current case status", allowableValues = "OPEM, CLOSED, [CUSTOM_VALUE]")
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private CaseStatus status;

    @ApiModelProperty(value = "Assigned to")
    @ManyToOne(fetch = FetchType.EAGER  )
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    @Getter @Setter
    private User assignee;
    // end parent case patch


    @Column(name = "entry_index")
    @Getter @Setter
    private Integer entryIndex;

    @DiffIgnore
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_case", referencedColumnName = "id", nullable = false)
    @Getter @Setter
    private BaseCase parentCase;

    public BaseCaseComment() {
        super();
    }

    public BaseCaseComment(String detail, BaseCase parentCase) {
        this(null, detail, parentCase);
    }

    public BaseCaseComment(String name, String detail, BaseCase parentCase) {
        super(name);
        this.setDetail(detail);
        this.setParent(parentCase);
        this.setParentCase(parentCase);
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void preSave() {
        if(StringUtils.isBlank(this.getName())){
            this.setName(UUID.randomUUID().toString());
            log.debug("preSave, tmp name: {}", this.getName());
        }
        // TODO: tmp parent pointer to app, should point to comment
        if(Objects.isNull(this.getParent())){
            this.setParent(this.getParentCase());
        }
        else if(Objects.isNull(this.getParentCase())){
            this.setParentCase((BaseCase) this.getParent());
        }

        super.preSave();
        // add empty title if needed
        if(Objects.isNull(this.getTitle())){
            this.setTitle(StringUtils.EMPTY);
        }
    }
}
