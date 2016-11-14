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

import java.io.Serializable;

/**
 * An interface for entities that expose information to create and map to an
 * instance of an ACL ObjectIdentity
 *
 * @see AclObjectIdentity
 * 
 */
public interface AclObject<IDTYPE extends Serializable, OWNERIDTYPE extends Serializable> {

	IDTYPE getIdentity();

	IDTYPE getParentIdentity();

	OWNERIDTYPE getOwner();

	Boolean getEntriesInheriting();

}