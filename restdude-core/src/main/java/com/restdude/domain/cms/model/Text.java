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
package com.restdude.domain.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "content_text")
public class Text extends Resource {
	
	public static final String MIME_MARKDOWN = "text/x-markdown";

	/**
	 * The MIME type of the page source. May be used to dynamically generate response content. 
	 */
	@Column(name = "source_content_type", nullable = false)
	private String sourceContentType;
	
	/**
	 * The raw text content of the resource 
	 */
	@Column(name = "source", nullable = false)
	private String source;

	public Text() {
		super();
	}
	
	public Text(String name) {
		super(name);
	}

	public String getSourceContentType() {
		return sourceContentType;
	}

	public void setSourceContentType(String sourceContentType) {
		this.sourceContentType = sourceContentType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
}