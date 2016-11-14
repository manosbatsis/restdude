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
package com.restdude.auth.acl.model;

import com.restdude.domain.base.model.CalipsoPersistable;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;

/**
 * Simple JPA implementation of Spring's AclObjectIdentity.
 */

@ShallowReference
@Entity
@Table(name = "acl_object_identity")
public class AclObjectIdentity implements CalipsoPersistable<Long> {

	private static final long serialVersionUID = -412927951704768649L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
	@JoinColumn(name = "object_id_class", referencedColumnName = "id", nullable = false, updatable = false)
	private AclClass aclClass;

	@Column(name = "object_id_identity")
	private String identity;

	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_object", referencedColumnName = "id")
	private AclObjectIdentity parent;

	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_sid", referencedColumnName = "id", nullable = false, updatable = false)
	private AclSid owner;

	@Column(name = "entries_inheriting")
	private Boolean entriesInheriting;


	public AclObjectIdentity(String identity, AclClass aclClass,
			AclObjectIdentity parent, AclSid owner, Boolean entriesInheriting) {
		super();
		this.aclClass = aclClass;
		this.identity = identity;
		this.parent = parent;
		this.owner = owner;
		this.entriesInheriting = entriesInheriting;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AclClass getAclClass() {
		return aclClass;
	}

	public void setAclClass(AclClass aclClass) {
		this.aclClass = aclClass;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public AclObjectIdentity getParent() {
		return parent;
	}

	public void setParent(AclObjectIdentity parent) {
		this.parent = parent;
	}

	public AclSid getOwner() {
		return owner;
	}

	public void setOwner(AclSid owner) {
		this.owner = owner;
	}

	public Boolean getEntriesInheriting() {
		return entriesInheriting;
	}

	public void setEntriesInheriting(Boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
	}

	/**
	 * Important so caching operates properly.
	 * <p>
	 * Considers an object of the same class equal if it has the same
	 * <code>classname</code> and <code>id</code> properties.
	 * <p>
	 * Numeric identities (Integer and Long values) are considered equal if they
	 * are numerically equal. Other serializable types are evaluated using a
	 * simple equality.
	 * 
	 * @param arg0
	 *            object to compare
	 * 
	 * @return <code>true</code> if the presented object matches this object
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null || !(arg0 instanceof AclObjectIdentity)) {
			return false;
		}

		AclObjectIdentity other = (AclObjectIdentity) arg0;
		if (!identity.equals(other.identity)) {
			return false;
		}

		return aclClass.equals(other.aclClass);
	}

	/**
	 * Important so caching operates properly.
	 * 
	 * @return the hash
	 */
	@Override
	public int hashCode() {
		int code = 31;
		code ^= this.aclClass.hashCode();
		code ^= this.identity.hashCode();

		return code;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()).append("[");
		sb.append("Type: ").append(this.aclClass);
		sb.append("; Identifier: ").append(this.identity).append("]");

		return sb.toString();
	}

	@Override
	public boolean isNew() {
		return this.getId() == null;
	}
}
