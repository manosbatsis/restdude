package com.restdude.domain.friends.binding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.restdude.domain.friends.model.FriendshipId;

import java.io.IOException;

public class FriendshipIdSerializer extends JsonSerializer<FriendshipId> {

	@Override
	public void serialize(FriendshipId id, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		gen.writeString(id.toStringRepresentation());
	}

}