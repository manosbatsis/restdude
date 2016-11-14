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
package com.restdude.domain.geography.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.model.AbstractAssignedidPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;

/**
 * Abstract base class for formal (usually political) geographical regions
 * @param <T> The parent category type
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@ModelResource(path = "countries")
@ApiModel(value = "Region", description = "A model representing a geographcal region.")
public abstract class AbstractFormalRegion<P extends AbstractFormalRegion> 
	extends AbstractAssignedidPersistable<String> {

	private static final long serialVersionUID = -1397566289646826160L;
	private static final String PATH_SEPARATOR = ": ";

	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "path", nullable = false)
	private String path;
	@Column(name = "path_level", nullable = false)
	private Short pathLevel;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="parent")
	private P parent;

	public AbstractFormalRegion() {
		super();
	}


	public AbstractFormalRegion(String id, String name, P parent) {
		super(id);
		this.name = name;
		this.parent = parent;
	}

	@JsonIgnore
	@Transient
	public String getPathSeparator(){
		return PATH_SEPARATOR;
	}
	
	@PreUpdate
    @PrePersist
    public void normalizePath() throws UnsupportedEncodingException{
		// set path
		if(this.getPath() == null){
			StringBuffer path = new StringBuffer();
			if(this.getParent() != null){
				path.append(this.getParent().getPath());
			}
			path.append(getPathSeparator());
			path.append(this.getName());
			this.setPath(path.toString());
		}
		// set path level
		Integer pathLevel = StringUtils.countMatches(this.getPath(), getPathSeparator());
		this.setPathLevel(pathLevel.shortValue());
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public P getParent() {
		return parent;
	}
	public void setParent(P parent) {
		this.parent = parent;
	}
	public Short getPathLevel() {
		return pathLevel;
	}
	public void setPathLevel(Short pathLevel) {
		this.pathLevel = pathLevel;
	}
	

}
