/**
 *
 * Restdude
 * -------------------------------------------------------------------
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
package com.restdude.mdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.validation.Unique;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A base class for pathFragment-like resource entities: files, folders, categories etc.
 */
@MappedSuperclass
public abstract class AbstractPersistableHierarchicalModel<P extends AbstractPersistableHierarchicalModel, T extends AbstractPersistableHierarchicalModel> extends AbstractPersistableNamedModel {

	private static final long serialVersionUID = 1L;

	private static final String PATH_SEPARATOR = "/";

	/**
	 * The HTTP URL of the resource, excluding the protocol, domain and port. Starts with a slash. 
	 */
    @NotNull
    @Column(name = "resource_path", length = 1500, nullable = false, unique = true)
	@Getter @Setter
	private String path;
	
	/**
	 * The number of URL segments in the resource path
	 */
    @NotNull
    @Column(name = "path_level", nullable = false)
	@Getter @Setter
	private Short pathLevel;

	@DiffIgnore
	@JsonIgnore
	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.LAZY)
    @JoinColumn(name = "same_as", referencedColumnName = "id", nullable = true)
	@Getter @Setter
    private T sameAs;

	@DiffIgnore
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", referencedColumnName = "id", nullable = true)
    private P parent;


	public AbstractPersistableHierarchicalModel() {
		super();
	}
	public AbstractPersistableHierarchicalModel(String name) {
		this.setName(name);
	}
	public AbstractPersistableHierarchicalModel(String name, P parent) {
		this(name);
		this.setParent(parent);
	}
	
	@JsonIgnore
	@Transient
	protected String getPathSeparator(){
		return PATH_SEPARATOR;
	}

	public P getParent() {
		return parent;
	}

	public void setParent(P parent) {
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preSave() {
		super.preSave();
		// update path if needed
		if(this.getPath() == null
				|| (Objects.nonNull(this.getPath()) && Objects.nonNull(this.getName()) && !this.getPath().endsWith(this.getName()))){
			StringBuffer path = new StringBuffer();
			if(this.getParent() != null){
				path.append(this.getParent().getPath());
			}
			path.append(getPathSeparator());
			path.append(this.getName());
			this.setPath(path.toString());

			// set pathFragment level
			Integer pathLevel = StringUtils.countMatches(this.getPath(), getPathSeparator());
			this.setPathLevel(pathLevel.shortValue());;
		}
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!this.getClass().isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		AbstractPersistableHierarchicalModel other = (AbstractPersistableHierarchicalModel) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.appendSuper(super.equals(obj));
		builder.append(this.getPath(), other.getPath());
		return builder.isEquals();
	}
	


}