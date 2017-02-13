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
package com.restdude.domain.friends.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.auth.spel.annotations.PreAuthorizeDelete;
import com.restdude.mdd.model.AbstractPersistableResource;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * {@value #CLASS_DESCRIPTION}
 */
@Entity
@Table(name = "friendships")
@ModelResource(pathFragment = Friendship.API_PATH, apiName = "Friendships", apiDescription = "Operations about friendships")
@ApiModel(value = "Friendship", description = Friendship.CLASS_DESCRIPTION)
@PreAuthorizeDelete(controller = " hasRole('ROLE_USER') ", service = " hasRole('ROLE_USER') ")
public class Friendship extends AbstractPersistableResource<FriendshipId> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Friendship.class);
    private static final long serialVersionUID = 1L;
    public static final String CLASS_DESCRIPTION = "A model representing a directional connection between two users. ";

    public static final String API_PATH = "friendships";

    @NotNull
    @ApiModelProperty(required = true)
    @EmbeddedId
    private FriendshipId pk;

    @ApiModelProperty(required = true, allowableValues = "NEW, CONFIRMED, BLOCK, DELETE")
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private FriendshipStatus status = FriendshipStatus.SENT;

    @JsonIgnore
    @Formula(" (current_status) ")
    private String previousStatus;

    public Friendship() {
    }

    public Friendship(FriendshipId id) {
        this.pk = id;
    }

    public Friendship(FriendshipStatus status) {
        this.status = status;
    }

    public Friendship(FriendshipId id, FriendshipStatus status) {
        this(id);
        this.status = status;
    }

    public Friendship(User sender, User recipient) {

        this.pk = new FriendshipId();
        this.pk.setLeft(sender);
        this.pk.setRight(recipient);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pk", this.getPk())
                .append("status", this.getStatus())
                .append("new", this.isNew())
                .toString();
    }

    @JsonIgnore
    public FriendshipId getInverseId() {
        FriendshipId inverse = null;
        FriendshipId thisId = this.getPk();
        if (thisId != null) {
            inverse = new FriendshipId();
            inverse.setLeft(thisId.getRight());
            inverse.setRight(thisId.getLeft());
        }
        return inverse;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isNew() {
        return this.getPreviousStatus() != null;
    }

    /**
     * {@inheritDoc}
     */
    public FriendshipId getPk() {
        return pk;
    }

    /**
     * {@inheritDoc}
     */
    public void setPk(FriendshipId id) {
        this.pk = id;
    }

    /**
     * @return the status
     */
    public FriendshipStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    /**
     * @return the previousStatus
     */
    public String getPreviousStatus() {
        return previousStatus;
    }

    /**
     * @param previousStatus the previousStatus to set
     */
    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }


    /**
     * {@inheritDoc}
     */
    public void preSave() {

    }


}