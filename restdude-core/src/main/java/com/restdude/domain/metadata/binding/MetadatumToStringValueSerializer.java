/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.metadata.binding;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.restdude.domain.metadata.model.Metadatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MetadatumToStringValueSerializer extends JsonSerializer<Metadatum> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetadatumToStringValueSerializer.class);

	@Override
	public void serialize(Metadatum value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
		// LOGGER.info("serialize value: "+value);
		if (null == value || null == value.getObject()) {
			// write the word 'null' if there's no value available
			jgen.writeNull();
		} else {
			jgen.writeRawValue(new StringBuffer("\"").append(value.getObject())
					.append("\"").toString());
		}
	}
}
