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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "continent")
@AttributeOverrides({
    @AttributeOverride(name="id", column=@Column(unique = true, nullable = false, length = 2)),
})
public class Continent extends AbstractFormalRegion<Continent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Continent.class);

    @Id
    private String id;

    @Formula(" (id) ")
    private String savedId;


	public Continent() {
		super();
	}
	
	public Continent(String id, String name) {
		super(id, name, null);
	}

	@Override
	public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Continent) {
			final Continent other = (Continent) obj;
			return new EqualsBuilder().appendSuper(super.equals(other))
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(Continent.class)
				.toHashCode();
	}


    /**
     * Get the entity's primary key
     *
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the entity's primary key
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    private String getSavedId() {
        return savedId;
    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getSavedId();
    }

}