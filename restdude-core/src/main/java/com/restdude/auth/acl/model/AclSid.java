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
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;

@ShallowReference
@Entity
@Table(name = "acl_sid")
public class AclSid implements CalipsoPersistable<Long> {

	private static final long serialVersionUID = -6859990313570031965L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "principal")
	private Boolean principal;

	@Column(name = "sid")
	private String sid;

	public AclSid(String sid) {
		Assert.hasText(sid, "SID required");
		this.sid = sid;
	}

	public AclSid(Authentication authentication) {
		Assert.notNull(authentication, "Authentication required");
		Assert.notNull(authentication.getPrincipal(), "Principal required");

		if (authentication.getPrincipal() instanceof UserDetails) {
			this.sid = ((UserDetails) authentication.getPrincipal())
					.getUsername();
		} else {
			this.sid = authentication.getPrincipal().toString();
		}
	}

	@Override
	public boolean equals(Object object) {
		if ((object == null) || !(object instanceof PrincipalSid)) {
			return false;
		}

		// Delegate to getPrincipal() for the actual comparison
		return ((PrincipalSid) object).getPrincipal().equals(
				this.getPrincipal());
	}

	@Override
	public int hashCode() {
		return this.getPrincipal().hashCode();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@Override
	public boolean isNew() {
		return this.getId() == null;
	}
}
