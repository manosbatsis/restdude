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
import com.restdude.domain.cases.ICaseCommentModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Base case comment implementation
 */
@MappedSuperclass
public abstract class AbstractCaseCommentModel<T extends AbstractCaseModel, C extends AbstractCaseCommentModel>
        extends AbstractBasicAuditedModel implements ICaseCommentModel<T, C> {


    @NotNull
    @ApiModelProperty(value = "The comment text", required = true, notes = "Max byte length: " + MAX_DETAIL_LENGTH)
    @Column(name = "text_content", nullable = false, updatable = false, length = MAX_DETAIL_LENGTH)
    @Getter
    @Setter
    private String content;

    @ApiModelProperty(value = "The case this comment is attached to")
    @ManyToOne
    @JoinColumn(name = "topic_case", updatable = false, nullable = false)
    @Getter @Setter
    private T subject;

    @ApiModelProperty(value = "The comment replied to")
    @ManyToOne
    @JoinColumn(name = "parent_comment", updatable = false, nullable = true)
    @Getter @Setter
    private T parent;

    @JsonIgnore
    @OneToMany(mappedBy="parent", fetch= FetchType.LAZY)
    @Getter @Setter
    private List<C> comments;

}
