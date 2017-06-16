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
import com.restdude.domain.PersistableModel;
import com.restdude.domain.cases.ICaseCommentModel;
import com.restdude.mdd.model.AbstractPersistableNamedModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Base case comment implementation

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "case_comment_base")
@Inheritance(strategy = InheritanceType.JOINED)
*/
@Slf4j
@Data
@MappedSuperclass
public abstract class AbstractCaseComment<T extends AbstractCase, C extends AbstractCaseComment>
        extends AbstractPersistableNamedModel implements ICaseCommentModel<T, C> {


    @NotNull
    @ApiModelProperty(value = "The comment text", required = true, notes = "Max byte length: " + MAX_DETAIL_LENGTH)
    @Column(name = "text_content", nullable = false, updatable = false, length = MAX_DETAIL_LENGTH)
    private String content;

    @ApiModelProperty(value = "The case this comment is attached to")
    @ManyToOne
    @JoinColumn(name = "topic_case", updatable = false, nullable = false)
    private T subject;

    @ApiModelProperty(value = "The comment replied to")
    @ManyToOne
    @JoinColumn(name = "parent_comment", updatable = false, nullable = true)
    private T parent;

    @Column(name = "entry_index")
    @Getter
    @Setter
    private Integer entryIndex;

    @JsonIgnore
    @OneToMany(mappedBy="parent", fetch= FetchType.LAZY)
    private List<C> comments;

    /**
     * {@inheritDoc}
     */
    @Override
    public void preSave() {
        super.preSave();
        if(StringUtils.isBlank(this.getName())){
            this.setName(UUID.randomUUID().toString());
            log.debug("preSave, tmp name: {}", this.getName());
        }
    }
}
