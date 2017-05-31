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

import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.SpaceInvitations;
import com.restdude.domain.cases.model.dto.SpaceInvitationsResult;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.model.enums.MembershipRequestStatus;
import com.restdude.domain.users.model.User;
import com.restdude.test.AbstractControllerIT;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Base class for rest-assured based, Space-aware controller integration testing
 */
@SuppressWarnings("unused")
public abstract class SpaceIT extends AbstractControllerIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpaceIT.class);

	public static final String RESOURCE_TEST_AVATAR_IMG = "blank-profile.gif";
	public static final String RESOURCE_TEST_BANNER_IMG = "banner_gameplay_final_1920_1080.gif";

	private final StringKeyGenerator generator = KeyGenerators.string();

	protected Space createSpace(Loggedincontext ownerLoginContext) {
		return this.createSpace(ownerLoginContext, ContextVisibilityType.PUBLIC);
	}

	protected Space createSpace(Loggedincontext ownerLoginContext, ContextVisibilityType visibility) {
		String contextName = generator.generateKey();
		Space businessContext = given().spec(ownerLoginContext.requestSpec)
				.log().all()
				.body(new Space.Builder()
						//.owner(new User(ownerLoginContext.userId))
						.name(contextName)
						.title(this.getClass().getSimpleName() + " " + contextName)
						.description("Created by "+this.getClass().getName())
						.visibility(visibility)
						.build())
				.post(WEBCONTEXT_PATH + "/api/rest/" + Space.API_PATH_FRAGMENT)
				.then().log().all()
				.assertThat()
				// test assertions
				.statusCode(201)
				.body("id", notNullValue())
				// get model
				.extract().as(Space.class);
		return businessContext;
	}


	protected Space getOwnedSpace(Loggedincontext ownerLoginContext) {
		// obtain a random Space id
		String contextId = given().spec(ownerLoginContext.requestSpec)
				.param("owner", ownerLoginContext.userId)
				.get(WEBCONTEXT_PATH + "/api/rest/" + Space.API_PATH_FRAGMENT + "/my").then()
				.assertThat()
				.body("content[0].id", notNullValue())
				.statusCode(200)
				.extract().path("content[0].id");
		// use the public Space
		Space publicSpace = new Space();
		publicSpace.setId(contextId);
		return publicSpace;
	}

	protected Space getSpace(Loggedincontext ownerLoginContext, String contextId) {
		// obtain the Space matching the ID
		Assert.assertNotNull(contextId);
		Space context = given().spec(ownerLoginContext.requestSpec)
				.get(WEBCONTEXT_PATH + "/api/rest/" + Space.API_PATH_FRAGMENT + "/" + contextId).then()
				.assertThat()
				.statusCode(200)
				.extract().as(Space.class);
		return context;
	}


	protected MembershipRequest createMembershipInvitation(Loggedincontext ownerLoginContext, Space ownedSpace, User invited) {
		LOGGER.debug("createMembershipInvitation, ownedSpace: {}, invited: {}", ownedSpace, invited);
		return given()
				.spec(ownerLoginContext.requestSpec)
				 .log().all()
				.body(new MembershipRequest.Builder()
						.context(ownedSpace)
						.user(invited)
						.build())
				.post(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT)
				.then()
				 .log().all()
				.assertThat()
				.statusCode(201)
				.body("status", equalTo(MembershipRequestStatus.SENT_INVITE.name()))
				.extract().as(MembershipRequest.class);
	}

	protected SpaceInvitationsResult createSpaceMembershipBatchInvitation(Loggedincontext ownerLoginContext, Space ownedSpace, Iterable<String> cc) {
		SpaceInvitations.Builder b = new SpaceInvitations.Builder()
				.businessContextId(ownedSpace.getId());
		for (String ccItem : cc){
			b.cc(ccItem);
		}
		return given()
				.spec(ownerLoginContext.requestSpec)
				.log().all()
				.body(b.build())
				.post(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT + "/cc")
				.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().as(SpaceInvitationsResult.class);
	}


	protected MembershipRequest updateSpaceMembershipRequest(Loggedincontext loginContext, String requestId, MembershipRequestStatus newStatus) {
		return given()
				.spec(loginContext.requestSpec)
				 .log().all()
				.body(new MembershipRequest.Builder()
						.status(newStatus)
						.build())
				.put(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT + "/" + requestId)
				.then()
				 .log().all()
				.assertThat().statusCode(200).body("status", equalTo(newStatus.name()))
				.extract().as(MembershipRequest.class);
	}

	protected void ensureSpaceMembership(Loggedincontext ownerLoginContext, Loggedincontext memberLoginContext, Space ownedSpace, String notecEmail) {
		SpaceInvitationsResult invitationsResult = given()
				.spec(ownerLoginContext.requestSpec)
				.body(new SpaceInvitations.Builder()
						.businessContextId(ownedSpace.getId())
						.cc(notecEmail)
						.build())
				.post(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT + "/cc")
				.then()
				.assertThat().statusCode(200).extract().as(SpaceInvitationsResult.class);
		// accept invitation if marked as invited
		if(CollectionUtils.isNotEmpty(invitationsResult.getInvited()) && invitationsResult.getInvited().contains(notecEmail)){
			// get invitation id
			String invitationId = given()
					.spec(memberLoginContext.requestSpec)
					.log().all()
					.param("context", ownedSpace.getId())
					.param("user", memberLoginContext.userId)
					.get(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT)
					.then()
					.log().all()
					.statusCode(200)
					.body("content[0].status", equalTo(MembershipRequestStatus.SENT_INVITE.name()))
					.extract().path("content[0].id");
			// Accept invitationSpaceMembershipRequestServiceImpl
			MembershipRequest confirmedMembershipRequest = given()
					.spec(memberLoginContext.requestSpec)
					.body(new MembershipRequest.Builder()
							//.id(invitationId)
							.status(MembershipRequestStatus.CONFIRMED)
							.build())
					.put(WEBCONTEXT_PATH + "/api/rest/" + MembershipRequest.API_PATH_FRAGMENT + "/" + invitationId)
					.then()
					.assertThat()
					.statusCode(200)
					.body("status", equalTo(MembershipRequestStatus.CONFIRMED.name()))
					.extract().as(MembershipRequest.class);
		}
		// fail if not already a member otherwise
		else if(CollectionUtils.isEmpty(invitationsResult.getMembers()) || !invitationsResult.getMembers().contains(notecEmail)){
			throw new RuntimeException("User neither invited or already a member");
		}
	}

}