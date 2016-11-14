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
package com.restdude.domain.rating.model;

import com.restdude.domain.rating.repository.RateableRepository;

import java.util.List;

/**
 * Simple interface for rating POJOs that have no typed target. 
 * If you are using this interface in your persistend classes you might want to check out 
 * the provided AbstractAuditableRateable entity and RateableRepository.
 * 
 * @see gr.abiss.calipso.model.entities.AbstractAuditableRateable
 * @see RateableRepository
 */
public interface Rateable<R extends AbstractRating>{

	/**
	 * Get the average rating for this entity
	 * @return the average rating
	 */
	public Float getRating();

	/**
	 * Set the average rating for this entity
	 */
	public void setRating(Float rating);

	/**
	 * Get the number of ratings for this entity
	 */
	public Integer getRatingsSize();

	/**
	 * Set the number of ratings for this entity
	 */
	public void setRatingsSize(Integer ratingsSize);

	/**
	 * Get the ratings for this entity that have been created by the current user
	 */
	public List<R> getCurrentUserRatings();

	/**
	 * Get the ratings for this entity that have been created by the current user
	 */
	public void setCurrentUserRatings(List<R> currentUserRatings);

}