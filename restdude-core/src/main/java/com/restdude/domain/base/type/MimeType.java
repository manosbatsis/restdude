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
package com.restdude.domain.base.type;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mime_type")
// TODO: add support for sub type suffix and params
public class MimeType implements Serializable {
	

	public static final MimeType HTML = new MimeType("text", "html");
	public static final MimeType XML = new MimeType("text", "xml");
	public static final MimeType BINARY_DATA = new MimeType("application", "octet-stream");
	public static final MimeType JPEG = new MimeType("image", "jpeg");
	public static final MimeType TEXT = new MimeType("text", "plain");
	public static final MimeType JSON = new MimeType("application", "json");

	@Id
	private String primaryType;

	@Id
	private String subType;

	public MimeType(final String mimeType) {
		int separator = mimeType.indexOf("/");
		if (separator < 1) {
			throwInvalidFormat(mimeType);
		}
		String[] types = mimeType.split("/");
		if (types.length != 2) {
			throwInvalidFormat(mimeType);
		}
		this.primaryType = types[0];
		this.subType = types[1];
	}

	public MimeType(final String primaryType, final String subType) {
		this(primaryType + '/' + subType);
	}


	private void throwInvalidFormat(final String mimeType) {
		throw new IllegalArgumentException(
				"Invalid MIME type format for value '" + mimeType
						+ "', expected format: 'type/subtype'");
	}

	public String getPrimaryType() {
		return primaryType;
	}

	public void setPrimaryType(final String primaryType) {
		this.primaryType = primaryType.toLowerCase();
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(final String subType) {
		this.subType = subType.toLowerCase();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.getPrimaryType());
		hcb.append(this.getSubType());
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MimeType)) {
			return false;
		}
		MimeType that = (MimeType) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.getPrimaryType(), that.getPrimaryType());
		eb.append(this.getSubType(), that.getSubType());
		return eb.isEquals();
	}

	public boolean isText() {
		final String primary = getPrimaryType();
		final String sub = getSubType();

		return "text".equalsIgnoreCase(primary) || isTextSubType(primary, sub);
	}

	private boolean isTextSubType(final String primaryType, final String sub) {
		boolean is = false;
		if ("application".equalsIgnoreCase(primaryType)) {
			if ("xml".equalsIgnoreCase(sub) || "html".equalsIgnoreCase(sub)
					|| "xhtml".equalsIgnoreCase(sub)
					|| "javascript".equalsIgnoreCase(sub)
					|| "x-javascript".equalsIgnoreCase(sub)
					|| "velocity".equalsIgnoreCase(sub)
					|| "freemarker".equalsIgnoreCase(sub)
					|| "rdf+xml".equalsIgnoreCase(sub)
					|| "json".equalsIgnoreCase(sub)) {
				is = true;
			}
		}
		return is;
	}

	public boolean isImage() {
		return "image".equalsIgnoreCase(getPrimaryType());
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer()
			.append(this.getPrimaryType())
			.append('/').append(this.getSubType());
//		if (StringUtils.isNotBlank(this.getSuffix())) {
//			s.append('+').append(this.getSuffix());
//		}
//		if (StringUtils.isNotBlank(this.getParameters())) {
//			s.append(';').append(this.getParameters());
//		}
		return s.toString();
	}

}
