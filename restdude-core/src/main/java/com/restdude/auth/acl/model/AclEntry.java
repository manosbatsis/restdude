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

@ShallowReference
@Entity
@Table(name = "acl_entry")
public class AclEntry implements CalipsoPersistable<Long> {

	private static final long serialVersionUID = -6426926101684163445L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// @ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
	// @JoinColumn(name = "acl_object_identity", referencedColumnName = "id",
	// nullable = false, updatable = false)
	// private AclObject objectIdentity;

	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
	@JoinColumn(name = "acl_object_identity", referencedColumnName = "id", nullable = false, updatable = false)
	private AclObjectIdentity objectIdentity;

	@Column(name = "ace_order")
	private Integer order;

	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
	@JoinColumn(name = "sid", referencedColumnName = "id", nullable = false, updatable = false)
	private AclSid sid;

	@Column(name = "mask")
	private Integer mask;

	@Column(name = "granting")
	private Boolean granting;

	@Column(name = "audit_success")
	private Boolean auditSuccess;

	@Column(name = "audit_failure")
	private Boolean auditFailure;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AclObjectIdentity getObjectIdentity() {
		return objectIdentity;
	}

	public void setObjectIdentity(AclObjectIdentity objectIdentity) {
		this.objectIdentity = objectIdentity;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public AclSid getSid() {
		return sid;
	}

	public void setSid(AclSid sid) {
		this.sid = sid;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Boolean getGranting() {
		return granting;
	}

	public void setGranting(Boolean granting) {
		this.granting = granting;
	}

	public Boolean getAuditSuccess() {
		return auditSuccess;
	}

	public void setAuditSuccess(Boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	public Boolean getAuditFailure() {
		return auditFailure;
	}

	public void setAuditFailure(Boolean auditFailure) {
		this.auditFailure = auditFailure;
	}

	@Override
	public boolean isNew() {
		return this.getId() == null;
	}

}
