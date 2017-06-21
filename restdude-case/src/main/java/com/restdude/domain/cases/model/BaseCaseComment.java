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
import javax.persistence.Table;

import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

    @Column(name = "entry_index")
    @Getter @Setter
    private Integer entryIndex;

    public BaseCaseComment() {
        super();
    }

    public BaseCaseComment(String detail, BaseCase parent) {
        super();
        this.setDetail(detail);
        this.setParent(parent);
    }

    public BaseCaseComment(String name, String detail, BaseCase parent) {
        super(name);
        this.setDetail(detail);
        this.setParent(parent);
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
        super.preSave();
        // add empty title if needed
        if(Objects.isNull(this.getTitle())){
            this.setTitle(StringUtils.EMPTY);
        }
    }
}
