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
@AnyMetaDef(name = "SpaceUserActivityObjectMetaDef", metaType = "string", idType = "string",
		metaValues = {
				@MetaValue(value = "1", targetEntity = Space.class),
				@MetaValue(value = "2", targetEntity = SpaceApp.class),
				@MetaValue(value = "3", targetEntity = AbstractCase.class),
				@MetaValue(value = "4", targetEntity = AbstractCaseComment.class),
				@MetaValue(value = "5", targetEntity = Membership.class),
				@MetaValue(value = "6", targetEntity = MembershipRequest.class)
		}
)
package com.restdude.domain.cases.model;

import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;