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
import com.restdude.domain.CommentableModel;
import com.restdude.domain.audit.model.AbstractBasicAuditedModel;
import com.restdude.domain.cases.CaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Base topic impl
 */
@Slf4j
@MappedSuperclass
public abstract class AbstractCaseModel<A extends AbstractApplicationContext, C extends AbstractCaseCommentModel<?, C>> extends AbstractBasicAuditedModel implements CaseModel<C> {


    @NotNull
    @ApiModelProperty(value = "Short descriptive title")
    @Column(name = "title", nullable = true, updatable = true, length = CommentableModel.MAX_TITLE_LENGTH)
    @Getter
    @Setter
    private String title;

    @ApiModelProperty(value = "Main content", notes = "Max byte length: " + CommentableModel.MAX_DETAIL_LENGTH)
    @Column(name = "detail", nullable = true, updatable = true, length = CommentableModel.MAX_DETAIL_LENGTH)
    @Getter
    @Setter
    private String detail;

    @Column(name = "case_index")
    @Getter
    @Setter
    private Integer caseIndex;

    @ApiModelProperty(value = "Current case status", allowableValues = "OPEM, CLOSED, [CUSTOM_VALUE]")
    @ManyToOne
    @JoinColumn(referencedColumnName = "pk", nullable = false, updatable = false)
    @Getter
    @Setter
    private CaseStatus status;

    @ApiModelProperty(value = "The application this case belongs to")
    @ManyToOne
    @JoinColumn(referencedColumnName = "pk", nullable = false, updatable = false)
    @Getter @Setter
    private A application;

    @JsonIgnore
    @OneToMany(mappedBy="subject", fetch= FetchType.LAZY)
    @Getter @Setter
    private List<C> comments;

    public AbstractCaseModel() {
    }

    public AbstractCaseModel(String title) {
        this.title = title;
    }

    public AbstractCaseModel(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pk", this.getPk())
                .append("title", this.getTitle())
                .append("detail", this.getDetail())
                .toString();
    }

    @Override
    public void preSave() {

        log.debug("preSave, before: {}", this);
        super.preSave();
        if (this.getCreatedBy() != null && this.getCreatedBy().getPk() == null) {
            this.setCreatedBy(null);
        }
        if (StringUtils.isNotEmpty(this.getTitle()) && this.getTitle().length() > MAX_TITLE_LENGTH) {
            this.setTitle(StringUtils.abbreviate(this.getTitle(), MAX_TITLE_LENGTH));
        }
        if (StringUtils.isNotEmpty(this.getDetail()) && this.getDetail().length() > CommentableModel.MAX_DETAIL_LENGTH) {
            this.setDetail(StringUtils.abbreviate(this.getDetail(), CommentableModel.MAX_DETAIL_LENGTH));
        }

        log.debug("preSave, after: {}", this);
    }

}
