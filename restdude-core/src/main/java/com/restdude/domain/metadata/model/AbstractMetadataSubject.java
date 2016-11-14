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
package com.restdude.domain.metadata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.metadata.binding.MetadataMapDeserializer;
import com.restdude.domain.metadata.binding.MetadatumToStringValueSerializer;
import org.jodah.typetools.TypeResolver;
import org.springframework.util.CollectionUtils;

import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base persistent class for metadata bearing classes. Implementations
 * can override relational specifics via javax.persistence.AssociationOverride
 * annotations
 */
@MappedSuperclass
public abstract class AbstractMetadataSubject<M extends Metadatum>
		extends AbstractSystemUuidPersistable implements MetadataSubject<M> {

	private static final long serialVersionUID = -1468517690700208260L;

	@OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
	@MapKey(name = "predicate")
	@JsonDeserialize(using = MetadataMapDeserializer.class)
	@JsonSerialize(contentUsing = MetadatumToStringValueSerializer.class)
	private Map<String, M> metadata;

	public AbstractMetadataSubject() {
		super();
	}

	@JsonIgnore
	@Override
	public abstract Class<M> getMetadataDomainClass();

	@Override
	public Map<String, M> getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(Map<String, M> metadata) {
		this.metadata = metadata;
	}

	@Override
	public M addMetadatum(M metadatum) {
		if (this.getMetadata() == null) {
			this.setMetadata(new HashMap<String, M>());
		}
		metadatum.setSubject(this);
		return this.getMetadata().put(metadatum.getPredicate(), metadatum);
	}

	@Override
	public void addMetadata(Collection<M> metadata) {
		if (!CollectionUtils.isEmpty(metadata)) {
			for (M metadatum : metadata) {
				this.addMetadatum(metadatum);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public M addMetadatum(String predicate, String object) {
		Class<?> metadatumClass = TypeResolver.resolveRawArgument(
				AbstractMetadataSubject.class, getClass());
		M metadatum = null;
		try {
			metadatum = (M) metadatumClass.getConstructor(String.class,
					String.class).newInstance(predicate, object);
		} catch (Exception e) {
			throw new RuntimeException("Failed adding metadatum", e);
		}
		return this.addMetadatum(metadatum);
	}

}