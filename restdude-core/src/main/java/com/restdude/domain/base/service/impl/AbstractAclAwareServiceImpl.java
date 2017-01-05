/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.base.service.impl;

import com.restdude.auth.acl.model.AclClass;
import com.restdude.auth.acl.model.AclObject;
import com.restdude.auth.acl.model.AclObjectIdentity;
import com.restdude.auth.acl.model.AclSid;
import com.restdude.auth.acl.repository.AclClassRepository;
import com.restdude.auth.acl.repository.AclObjectIdentityRepository;
import com.restdude.auth.acl.repository.AclSidRepository;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * JPA Entity CRUD and search service that uses a Spring Data repository implementation.
 * You should extend it and inject your Repository bean by overriding setRepository.
 *
 * @param <T> Your resource class to manage, usually an entity class
 * @param <ID> Resource id type, usually Long or String
 * @param <R> The repository class
 */
public abstract class AbstractAclAwareServiceImpl<T extends CalipsoPersistable<ID>, ID extends Serializable, R extends ModelRepository<T, ID>>
		extends CrudServiceImpl<T, ID, R> implements CrudService<T, ID> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAclAwareServiceImpl.class);

	private AclObjectIdentityRepository aclObjectIdentityRepository;
	private AclClassRepository aclClassRepository;
	private AclSidRepository aclSidRepository;

	@Autowired
	public void setAclObjectIdentityRepository(
			AclObjectIdentityRepository aclObjectIdentityRepository) {
		this.aclObjectIdentityRepository = aclObjectIdentityRepository;
	}

	@Autowired
	public void setAclClassRepository(AclClassRepository aclClassRepository) {
		this.aclClassRepository = aclClassRepository;
	}

	@Autowired
	public void setAclSidRepository(AclSidRepository aclSidRepository) {
		this.aclSidRepository = aclSidRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false)
    public void postCreate(T resource) {
        createAclObjectIdentity(resource, this.getDomainClass());
    }

	@SuppressWarnings("unchecked")
	protected void createAclObjectIdentity(T saved, Class domainClass) {
		if (AclObject.class.isAssignableFrom(domainClass)) {
			AclObject<ID, ID> aclObject = (AclObject<ID, ID>) saved;
			AclClass aclClass = this.aclClassRepository
					.findByClassName(domainClass.getName());

			Serializable sid = aclObject.getOwner();
			AclSid aclSid = null;
			// use current principal as owner?
			if (sid == null) {
				UserDetails userDetails = SecurityUtil.getPrincipal();
				if (userDetails != null) {
					sid = userDetails.getUsername();
				}
			}

			// add owner if any
			if (sid != null) {
				aclSid = this.aclSidRepository.findBySid(sid.toString());
			}

			// TODO: parent
			this.aclObjectIdentityRepository.save(new AclObjectIdentity(
					aclObject.getIdentity().toString(), aclClass, null, aclSid,
					aclObject.getEntriesInheriting()));
		}
	}




}