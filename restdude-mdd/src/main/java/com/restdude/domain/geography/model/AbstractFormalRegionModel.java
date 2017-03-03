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
package com.restdude.domain.geography.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.model.AbstractAssignedIdPersistableModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Abstract base class for formal (usually political) geographical regions
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractFormalRegionModel<P extends AbstractFormalRegionModel, PK extends Serializable> extends AbstractAssignedIdPersistableModel<PK> {

	private static final String PATH_SEPARATOR = ": ";


	@NotNull @Getter @Setter
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull @Getter @Setter
	@Column(name = "pathFragment", nullable = false)
	private String path;

	@NotNull @Getter @Setter
	@Column(name = "path_level", nullable = false)
	private Short pathLevel;

	@Getter @Setter
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="parent")
	private P parent;

	public AbstractFormalRegionModel() {
		super();
	}


    public AbstractFormalRegionModel(PK pk, String name, P parent) {
        this.setPk(pk);
		this.name = name;
		this.parent = parent;
	}

	@JsonIgnore
	@Transient
	public String getPathSeparator(){
		return PATH_SEPARATOR;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void preSave() {
        // set pathFragment
		if(this.getPath() == null){
			StringBuffer path = new StringBuffer();
			if(this.getParent() != null){
				path.append(this.getParent().getPath());
			}
			path.append(getPathSeparator());
			path.append(this.getName());
			this.setPath(path.toString());
		}
		// set pathFragment level
		Integer pathLevel = StringUtils.countMatches(this.getPath(), getPathSeparator());
		this.setPathLevel(pathLevel.shortValue());
		
	}




	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(AbstractFormalRegionModel.class)
				.append(this.getName())
				.append(this.getPath())
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof AbstractFormalRegionModel) {
			final AbstractFormalRegionModel other = (AbstractFormalRegionModel) obj;
			return new EqualsBuilder()
					.append(this.getName(), other.getName())
					.append(this.getPath(), other.getPath())
					.isEquals();
		} else {
			return false;
		}
	}
}
