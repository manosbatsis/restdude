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
package com.restdude.domain.topic.model;

import com.restdude.domain.users.model.User;
import com.restdude.mdd.model.TopicModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;


/**
 * Base topic impl
 */
@Slf4j
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractTopicModel<C extends AbstractTopicCommentModel<?, C>> extends AbstractGeneratedContentModel implements TopicModel<String, C, User>{


    @NotNull
    @ApiModelProperty(value = "Short descriptive title")
    @Column(name = "title", nullable = true, updatable = true, length = MAX_MESSAGE_LENGTH)
    @Getter
    @Setter
    private String title;

    @ApiModelProperty(value = "Main content", notes = "Max byte length: " + DEFAULT_MAX_DESCRIPTION_LENGTH)
    @Column(name = "detail", nullable = true, updatable = true, length = DEFAULT_MAX_DESCRIPTION_LENGTH)
    @Getter
    @Setter
    private String detail;

    public AbstractTopicModel() {
    }

    public AbstractTopicModel(String title) {
        this.title = title;
    }

    public AbstractTopicModel(String title, String detail) {
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
        if (StringUtils.isNotEmpty(this.getTitle()) && this.getTitle().length() > MAX_MESSAGE_LENGTH) {
            this.setTitle(StringUtils.abbreviate(this.getTitle(), MAX_MESSAGE_LENGTH));
        }
        if (StringUtils.isNotEmpty(this.getDetail()) && this.getDetail().length() > DEFAULT_MAX_DESCRIPTION_LENGTH) {
            this.setDetail(StringUtils.abbreviate(this.getDetail(), DEFAULT_MAX_DESCRIPTION_LENGTH));
        }

        log.debug("preSave, after: {}", this);
    }

}
