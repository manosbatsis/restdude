package com.restdude.domain.friends.model;

import com.restdude.domain.base.model.EmbeddableManyToManyId;
import com.restdude.domain.users.model.User;
import io.swagger.annotations.ApiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Composite primary key for {@Friendship}
 *
 * @see EmbeddableManyToManyId
 */

@ApiModel(value = "FriendshipId", description = "An {@link javax.persistence;Embeddable} JPA composite key. "
        + "The custom implementation provides support to all relevant de)serialization components "
		+ "(JSON, request mappings, path/param variables etc.) " + "for both [ownerId" + EmbeddableManyToManyId.SPLIT_CHAR
		+ "friendId]" + " and [friendId] string representations.")
@Embeddable
public class FriendshipId extends EmbeddableManyToManyId<User, String, User, String> implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipId.class);

	@Override
	public User buildLeft(Serializable left) {
		return new User(left.toString());
	}

	@Override
	public User buildRight(Serializable right) {
		return new User(right.toString());
	}
}