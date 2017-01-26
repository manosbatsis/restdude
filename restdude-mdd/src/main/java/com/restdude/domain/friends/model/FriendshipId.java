/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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

import com.restdude.domain.base.model.EmbeddableManyToManyId;
import com.restdude.domain.users.model.User;
import io.swagger.annotations.ApiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * {@value #CLASS_DESCRIPTION}
 *
 * @see EmbeddableManyToManyId
 */

@ApiModel(value = "FriendshipId", description = FriendshipId.CLASS_DESCRIPTION)
@Embeddable
public class FriendshipId extends EmbeddableManyToManyId<User, String, User, String> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipId.class);
    public static final String CLASS_DESCRIPTION = "An {@link javax.persistence;Embeddable} JPA composite key. "
            + "The custom implementation provides support to all relevant de)serialization components "
            + "(JSON, request mappings, path/param variables etc.) " + "for both [ownerId" + EmbeddableManyToManyId.SPLIT_CHAR
            + "friendId]" + " and [friendId] string representations.";

    @Override
    public User buildLeft(Serializable left) {
        return new User(left.toString());
    }

    @Override
    public User buildRight(Serializable right) {
        return new User(right.toString());
    }
}