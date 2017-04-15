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
package com.restdude.test.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.SpaceInvitationsResult;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.model.enums.MembershipRequestStatus;
import com.restdude.domain.cases.model.enums.SpaceActivity;
import com.restdude.domain.users.model.User;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompSession;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;

@Test(singleThreaded = true, description = "Test Space membership requests")
@SuppressWarnings("unused")
public class MembershipRequestIT extends SpaceIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembershipRequestIT.class);

	public void testPublic() throws Exception {

		// --------------------------------
		// Setup
		// --------------------------------
		LOGGER.info("Login users...");
		Loggedincontext ownerLoginContext = this.getLoggedinContext("admin", "admin");

		StompSession ownerSession = getStompSession(WEBSOCKET_URI, ownerLoginContext, null, null, null);
		BlockingQueue<JsonNode> ownerActivitiesQueue = getActivityQueue(ownerSession);

		LOGGER.info("Owner: Create a Space...");
		Space ownedSpace = this.createSpace(ownerLoginContext, ContextVisibilityType.PUBLIC);


		Loggedincontext ittest0LoginContext = this.getLoggedinContext("usercontrollerit", "usercontrollerit");
		MembershipRequest confirmedMembershipRequest = given()
				.spec(ittest0LoginContext.requestSpec)
				.body(new MembershipRequest.Builder()
						.context(ownedSpace)
						.build())
				.post(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT)
				.then()
				.assertThat()
				.statusCode(201)
				.body("status", equalTo(MembershipRequestStatus.CONFIRMED.name()))
				.extract().as(MembershipRequest.class);
	}

	@Test(priority = 10, description = "Test Space invitation list created by owner")
	public void testClosed() throws Exception {

		// --------------------------------
		// Setup
		// --------------------------------
		LOGGER.info("Login users...");
		Loggedincontext ownerLoginContext = this.getLoggedinContext("admin", "admin");
		Loggedincontext ittest0LoginContext = this.getLoggedinContext("adminFriend", "adminFriend");
		Loggedincontext ittest1LoginContext = this.getLoggedinContext("usercontrollerit", "usercontrollerit");

		LOGGER.info("Open stomp sessions...");
		StompSession ownerSession = getStompSession(WEBSOCKET_URI, ownerLoginContext, null, null, null);
		StompSession ittest0Session = getStompSession(WEBSOCKET_URI, ittest0LoginContext, null, null, null);
		StompSession ittest1Session = getStompSession(WEBSOCKET_URI, ittest1LoginContext, null, null, null);

		LOGGER.info("Subscribe users to generic activity notifications...");
		BlockingQueue<JsonNode> ownerActivitiesQueue = getActivityQueue(ownerSession);
		BlockingQueue<JsonNode> ittest0ActivitiesQueue = getActivityQueue(ittest0Session);
		BlockingQueue<JsonNode> ittest1ActivitiesQueue = getActivityQueue(ittest1Session);

		LOGGER.info("Owner: Create a Space...");
		Space ownedSpace = this.createSpace(ownerLoginContext, ContextVisibilityType.CLOSED);

		// --------------------------------
		// Owner: Create Invitations
		// --------------------------------
		User invitedUser = this.getUserByUsernameOrEmail(ownerLoginContext.requestSpec, "adminFriend");
		LOGGER.info("Owner: Create invitation for  user: {}", invitedUser);
		MembershipRequest singleRequest = createMembershipInvitation(ownerLoginContext, ownedSpace, invitedUser);

		LOGGER.info("Owner: Create batch invitations for recipient list...");
		SpaceInvitationsResult batchRequest = createSpaceMembershipBatchInvitation(ownerLoginContext, ownedSpace,
				Arrays.asList(new String[]{"operator", "ittest2", "ittest3@restdude.com", "ittest4@restdude.com", "ittest5@restdude.com"}));


		// --------------------------------
		// Users: Handle notifications
		// --------------------------------

		// verify invitation notifications were received
		JsonNode itest0Pending = ittest0ActivitiesQueue.poll(5, SECONDS);
		LOGGER.debug("itest0: received activity notification: {}", JacksonUtils.prettyPrint(itest0Pending));
		Assert.assertEquals(itest0Pending.get("object").get("status").asText(), MembershipRequestStatus.SENT_INVITE.name());

		JsonNode itest1Pending = ittest1ActivitiesQueue.poll(5, SECONDS);
		Assert.assertEquals(itest0Pending.get("object").get("status").asText(), MembershipRequestStatus.SENT_INVITE.name());

		// user0: accept
		updateSpaceMembershipRequest(ittest0LoginContext, itest0Pending.get("object").get("id").asText(), MembershipRequestStatus.CONFIRMED);

		// verify new membership notifications were received
		JsonNode ownerNewMemberMsw = ownerActivitiesQueue.poll(5, SECONDS);
		LOGGER.debug("owner: received new member notification: {}", JacksonUtils.prettyPrint(ownerNewMemberMsw));
		Assert.assertEquals(ownerNewMemberMsw.get("predicate").asText(), SpaceActivity.BECAME_MEMBER_OF.name());


        // user0: remove self and block further  invites
        updateSpaceMembershipRequest(ittest0LoginContext, itest0Pending.get("object").get("id").asText(), MembershipRequestStatus.BLOCK_INVITE);


	}



}