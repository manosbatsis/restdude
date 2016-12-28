package com.restdude.domain.base.binding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.restdude.domain.base.model.EmbeddableManyToManyId;

import java.io.IOException;

public class EmbeddableManyToManyIdSerializer extends JsonSerializer<EmbeddableManyToManyId> {

	@Override
	public void serialize(EmbeddableManyToManyId id, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		gen.writeString(id.toStringRepresentation());
	}

	@Override
	public Class handledType() {
		return EmbeddableManyToManyId.class;
	}

}