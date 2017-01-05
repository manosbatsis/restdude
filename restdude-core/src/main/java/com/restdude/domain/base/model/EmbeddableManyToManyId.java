/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.restdude.domain.base.binding.EmbeddableManyToManyIdDeserializer;
import com.restdude.domain.base.binding.EmbeddableManyToManyIdSerializer;
import com.restdude.domain.base.binding.StringToEmbeddableManyToManyIdConverterFactory;
import com.restdude.domain.friends.model.FriendshipId;
import com.restdude.mdd.util.EntityUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A base class for {@link Embeddable}s used as composite IDs in entities based on a many-to-many table.
 * Conveniently mapped from/to JSON via {@link EmbeddableManyToManyIdSerializer},  {@link EmbeddableManyToManyIdDeserializer} and  {@link StringToEmbeddableManyToManyIdConverterFactory}
 *
 * @param <L>   The type of the left MenyToOne relationship entity
 * @param <LID> The type of the left MenyToOne relationship entity ID
 * @param <R>   The type of the right MenyToOne relationship entity
 * @param <RID> The type of the right MenyToOne relationship entity ID
 * @see EmbeddableManyToManyIdDeserializer
 * @see EmbeddableManyToManyIdSerializer
 * @see StringToEmbeddableManyToManyIdConverterFactory
 */
@MappedSuperclass
@JsonSerialize(using = EmbeddableManyToManyIdSerializer.class)
@JsonDeserialize(using = EmbeddableManyToManyIdDeserializer.class)
public abstract class EmbeddableManyToManyId<L extends CalipsoPersistable<LID>, LID extends Serializable, R extends CalipsoPersistable<RID>, RID extends Serializable> implements Serializable, IEmbeddableManyToManyId<L, LID, R, RID> {


    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddableManyToManyId.class);

    public static final String SPLIT_CHAR = "_";


    @NotNull
    @ApiModelProperty(required = true, example = "{id: '[id]'}")
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private L left;

    @NotNull
    @ApiModelProperty(required = true, example = "{id: '[id]'}")
    @JoinColumn(name = "friend_id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private R right;

    public EmbeddableManyToManyId() {
    }

    public EmbeddableManyToManyId(@NotNull String value) {
        init(value);
    }

    public EmbeddableManyToManyId(LID left, @NotNull RID right) {
        init(left, right);
    }


    public EmbeddableManyToManyId(L left, @NotNull R right) {
        init(left, right);
    }

    public abstract L buildLeft(Serializable left);

    public abstract R buildRight(Serializable right);


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(EntityUtil.idOrNull(this.getLeft())).append(EntityUtil.idOrNull(this.getRight())).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (FriendshipId.class.isAssignableFrom(obj.getClass())) {
            final FriendshipId other = (FriendshipId) obj;
            return new EqualsBuilder().append(EntityUtil.idOrNull(this.getLeft()), EntityUtil.idOrNull(other.getLeft()))
                    .append(EntityUtil.idOrNull(this.getRight()), EntityUtil.idOrNull(other.getRight())).isEquals();
        } else {
            return false;
        }
    }

    @Override
    public void init(@NotNull String value) {
        String[] parts = value.split(SPLIT_CHAR);
        if (parts.length == 2) {
            this.left = this.buildLeft(parts[0]);
            this.right = this.buildRight(parts[1]);
        } else if (parts.length == 1) {
            this.right = this.buildRight(parts[0]);
        }
    }

    @Override
    public void init(LID left, @NotNull RID right) {
        if (left != null) {
            this.left = buildLeft(left);
        }
        this.right = buildRight(right);
    }

    @Override
    public void init(L left, @NotNull R right) {
        if (left != null) {
            this.left = left;
        }
        this.right = right;
    }

    @Override
    public String toStringRepresentation() {

        String sender = EntityUtil.idOrNEmpty(this.getLeft());
        String recipient = EntityUtil.idOrNEmpty(this.getRight());

        StringBuffer s = new StringBuffer(sender);
        if (StringUtils.isNoneBlank(sender, recipient)) {
            s.append(SPLIT_CHAR);
        }
        s.append(recipient);

        String id = s.toString();

        return StringUtils.isNotBlank(id) ? id : null;

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("left", EntityUtil.idOrNull(this.getLeft()))
                .append("right", EntityUtil.idOrNull(this.getRight()))
                .toString();
    }


    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public void setLeft(L left) {
        this.left = left;
    }

    @Override
    public R getRight() {
        return right;
    }

    @Override
    public void setRight(R right) {
        this.right = right;
    }

}