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
import com.restdude.domain.base.validation.Unique;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Abstract entity class with basic auditing, unique constraints validation and authorization settings.
 * @param <PK> The id Serializable
 */
@XmlRootElement
@MappedSuperclass
@Unique
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
public abstract class AbstractPersistable<PK extends Serializable> implements CalipsoPersistable<PK> {

    private static final long serialVersionUID = -6009587976502456848L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistable.class);

    public AbstractPersistable() {
        super();
    }

    public AbstractPersistable(PK pk) {
        this.setPk(pk);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(PK_FIELD_NAME, this.getPk()).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract PK getPk();

    /**
     * {@inheritDoc}
     */
    public abstract void setPk(PK pk);

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        if (!AbstractPersistable.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        AbstractPersistable other = (AbstractPersistable) obj;
        return new EqualsBuilder()
                .append(this.getPk(), other.getPk())
                .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getPk())
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    public void preSave() {

    }
}