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
package com.restdude.domain.base.model;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * Base interface for persistable entities
 */
public interface CalipsoPersistable<ID extends Serializable> extends Persistable<ID>{

    static final String PRE_AUTHORIZE_SEARCH = "isAuthenticated()";
    static final String PRE_AUTHORIZE_CREATE = "isAuthenticated()";
    static final String PRE_AUTHORIZE_UPDATE = "isAuthenticated()";
    static final String PRE_AUTHORIZE_PATCH = "isAuthenticated()";
    static final String PRE_AUTHORIZE_VIEW = "isAuthenticated()";
    static final String PRE_AUTHORIZE_DELETE = "denyAll";


    static final String PRE_AUTHORIZE_DELETE_BY_ID = "denyAll";
    static final String PRE_AUTHORIZE_DELETE_ALL = "denyAll";
    static final String PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
    static final String PRE_AUTHORIZE_FIND_BY_IDS = "denyAll";
    static final String PRE_AUTHORIZE_FIND_ALL = "denyAll";
    static final String PRE_AUTHORIZE_COUNT = "denyAll";


    ID getId();

    void setId(ID id);

    /**
     * Equivalent of a method annotated with @{@link javax.persistence.PrePersist} and/or
     *
     * @{@link javax.persistence.PreUpdate}, only applied before validation
     */
    void preSave();

}