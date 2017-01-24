/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
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
package com.restdude.domain.base.model;

import com.restdude.auth.spel.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Abstract entity class with basic auditing.
 * @param <ID> The primary key type, Serializable
 */
@XmlRootElement
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@PreAuthorizeCount
@PreAuthorizeCreate
@PreAuthorizeDelete
@PreAuthorizeDeleteAll
@PreAuthorizeDeleteById
@PreAuthorizeDeleteWithCascade
@PreAuthorizeFindAll
@PreAuthorizeFindById
@PreAuthorizeFindByIds
@PreAuthorizeFindOrCreate
@PreAuthorizeFindPaginated
@PreAuthorizePatch
@PreAuthorizeUpdate
public abstract class AbstractPersistable<ID extends Serializable> {

	private static final long serialVersionUID = -6009587976502456848L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistable.class);

    public static interface ItemView {}
    public static interface CollectionView {}

	public AbstractPersistable() {
		super();
	}

}