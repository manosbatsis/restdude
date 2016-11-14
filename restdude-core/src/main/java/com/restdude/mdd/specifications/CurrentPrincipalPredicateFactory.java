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
package com.restdude.mdd.specifications;

import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * A predicate that automatically enforces the current principal (i.e. loggedin user) id
 * as the parent of a many2one. The predicate is used  by {@link GenericSpecifications} for entity attributes using the
 * {@link com.restdude.mdd.annotation.CurrentPrincipalField}.
 * @see com.restdude.mdd.annotation.CurrentPrincipalField
 * @see com.restdude.mdd.specifications.GenericSpecifications
 */
public class CurrentPrincipalPredicateFactory<F extends Serializable> implements IPredicateFactory<F> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentPrincipalPredicateFactory.class);

	public CurrentPrincipalPredicateFactory() {
	}


	/**
     * @see com.restdude.mdd.specifications.IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, String[])
     */
	@Override
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<F> fieldType,
			String[] propertyValues) {
		if (!AbstractPersistable.class.isAssignableFrom(fieldType)) {
			LOGGER.warn("Non-Entity type for property '" + propertyName + "': " + fieldType.getName());
		}
		// ignore given values, enforce current principal
		ICalipsoUserDetails userDetails = SecurityUtil.getPrincipal();
		Path<F> parentId = root.<AbstractPersistable> get(propertyName).<F> get("id");

		LOGGER.info("Creating predicate for current principal: " + userDetails);
		Predicate predicate = cb.equal(parentId, userDetails.getId());
		
		return predicate;
	}
}