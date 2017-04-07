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
package com.restdude.domain.cms.model;

import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * {@value #API_MODEL_DESCRIPTION}}
 */
@MappedSuperclass
@ApiModel(description = AbstractSelectionRange.API_MODEL_DESCRIPTION)
public abstract class AbstractSelectionRange extends AbstractSystemUuidPersistableModel {

    public static final String API_MODEL_DESCRIPTION = "Defines a text or other content selection range, typically as the subject of another entity like a note or annotation";

    @Getter @Setter
    @ApiModelProperty(value = "(relative) XPath to start element")
    private String start;

    @Getter @Setter
    @ApiModelProperty(value = "(relative) XPath to end element")
    private String end;

    @Getter @Setter
    @ApiModelProperty(value = "Character offset within start element")
    private Integer startOffset;

    @Getter @Setter
    @ApiModelProperty(value = "Character offset within end element")
    private Integer endOffset;

    public AbstractSelectionRange() {
    }

    public AbstractSelectionRange(String start, String end, Integer startOffset, Integer endOffset) {
        this.start = start;
        this.end = end;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }
}
