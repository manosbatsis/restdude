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
package com.restdude.domain.cases.model.enums;

import io.swagger.annotations.ApiModel;

import java.util.*;

@ApiModel(description = "Enumeration of relevant statuses. Provides utility methods for validating candidate status updates.")
public enum MembershipRequestStatus {

	/**
	 * Record is an invitation sent to the user
	 * sender: owner
	 */
	SENT_REQUEST,
	/**
	 * Record is a request to join sent by the user
	 * sender: user
	 */
	SENT_INVITE,
	/**
	 * A corresponding membership record already exists
	 * sender: any
	 */
	CONFIRMED,
	/**
	 * Record blocks any further requests
	 * sender: owner
	 */
	BLOCK_REQUEST,
	/**
	 * Record blocks any further invitations
	 * sender: user
	 */
	BLOCK_INVITE,
	/**
	 * Record is marked for deletion (detached-only status)
	 * sender: any
	 */
	DELETE;


	private static Map<MembershipRequestStatus, Set<MembershipRequestStatus>> allowedForOwner =
			new HashMap<MembershipRequestStatus, Set<MembershipRequestStatus>>();
	private static Map<MembershipRequestStatus, Set<MembershipRequestStatus>> allowedForUser =
			new HashMap<MembershipRequestStatus, Set<MembershipRequestStatus>>();

	static {

		// Setup allowed "next" choices per  role and status

		// NEW: when new, Website owner may invite or ban
		allowedForOwner.put(null, new HashSet<MembershipRequestStatus>(Arrays.asList(SENT_INVITE, BLOCK_REQUEST)));
		allowedForUser.put(null, new HashSet<MembershipRequestStatus>(Arrays.asList(SENT_REQUEST, BLOCK_INVITE)));

		// when a request to join is pending:
		allowedForOwner.put(SENT_REQUEST, new HashSet<MembershipRequestStatus>(Arrays.asList(CONFIRMED, BLOCK_REQUEST, DELETE)));
		allowedForUser.put(SENT_REQUEST, new HashSet<MembershipRequestStatus>(Arrays.asList(BLOCK_INVITE, DELETE)));

		// when an invitation is pending:
		allowedForOwner.put(SENT_INVITE, new HashSet<MembershipRequestStatus>(Arrays.asList(BLOCK_REQUEST, DELETE)));
		allowedForUser.put(SENT_INVITE, new HashSet<MembershipRequestStatus>(Arrays.asList(CONFIRMED, BLOCK_INVITE, DELETE)));

		// when a membership exists
		allowedForOwner.put(CONFIRMED, new HashSet<MembershipRequestStatus>(Arrays.asList(BLOCK_REQUEST, DELETE)));
		allowedForUser.put(CONFIRMED, new HashSet<MembershipRequestStatus>(Arrays.asList(BLOCK_INVITE, DELETE)));

		// when user is blocked
		allowedForOwner.put(BLOCK_REQUEST, new HashSet<MembershipRequestStatus>(Arrays.asList(SENT_INVITE, DELETE)));
		allowedForUser.put(BLOCK_REQUEST, new HashSet<MembershipRequestStatus>());

		// when owner is blocked
		allowedForOwner.put(BLOCK_INVITE, new HashSet<MembershipRequestStatus>());
		allowedForUser.put(BLOCK_INVITE, new HashSet<MembershipRequestStatus>(Arrays.asList(SENT_REQUEST, DELETE)));

	}

    /**
     * Check if the candidate status is allowed
     * @param current the currently persisted status, if any
     * @param next the candidate status, if any
     * @param forOwner whether the current user is the Website owner
     * @return whether the change is allowed
     */
	public static boolean isValidChange(MembershipRequestStatus current, MembershipRequestStatus next, boolean forOwner){
        boolean allowed = false;
        if(next != null){

            // get allowed next for owner or user
            Set<MembershipRequestStatus> allowedNext = forOwner ? allowedForOwner.get(current) : allowedForUser.get(current);
            // check if next is allowed
            allowed = allowedNext != null && allowedNext.contains(next);

        }

        return allowed;

	}

}
