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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import com.restdude.mdd.model.GeneratedContentModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Base impl class for generated content
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractGeneratedContentModel extends AbstractSystemUuidPersistableModel implements GeneratedContentModel<String, User> {

    @CreatedDate
    @DiffIgnore
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "Date created", readOnly = true)
    @Column(name = "date_created", nullable = false, updatable = false)
    @Getter @Setter
    private LocalDateTime createdDate;

    @LastModifiedDate
    @DiffIgnore
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "Date last modified", readOnly = true)
    @Column(name = "date_last_modified", nullable = false)
    @Getter @Setter
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @DiffIgnore
    @JsonIgnore
    @ApiModelProperty(value = "Created by", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "pk", updatable = false)
    @Getter @Setter
    private User createdBy;

    @LastModifiedBy
    @DiffIgnore
    @JsonIgnore
    @ApiModelProperty(value = "udated by", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by", referencedColumnName = "pk", updatable = false)
    @Getter @Setter
    private User lastModifiedBy;

    @JsonProperty("createdBy")
    @ApiModelProperty(value = "The initial content author", readOnly = true)
    public UserDTO getCreatedByInfo() {
        return UserDTO.fromUser(this.getCreatedBy());
    }

    @JsonProperty("lastModifiedBy")
    @ApiModelProperty(value = "The latest update author", readOnly = true)
    public UserDTO getLastModifiedByInfo() {
        return UserDTO.fromUser(this.getLastModifiedBy());
    }

}
