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
package com.restdude.domain.cases.service.impl;

import com.restdude.domain.cases.model.MembershipRequest;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.model.enums.MembershipRequestStatus;
import com.restdude.domain.cases.repository.SpaceRepository;
import com.restdude.domain.cases.service.MembershipRequestService;
import com.restdude.domain.cases.service.SpaceService;
import com.restdude.domain.details.contact.model.ContactDetails;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.friends.service.FriendshipService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Named("spaceService")
public class SpaceServiceImpl
		extends AbstractContextServiceImpl<Space, SpaceRepository>
		implements SpaceService {

	private Space syetemSpace;
	private UserService userService;
	private FriendshipService friendshipService;
	protected MembershipRequestService membershipRequestService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setFriendshipService(FriendshipService friendshipService) {
		this.friendshipService = friendshipService;
	}

	@Autowired
	public void setMembershipRequestService(MembershipRequestService membershipRequestService) {
		this.membershipRequestService = membershipRequestService;
	}

	public Space getUserSpace(User user){
		return this.repository.getUserSpace(user);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initDataOverride(User systemUser){

		// initialize globals?
		this.syetemSpace = this.repository.getSystemContext();
		if(this.syetemSpace == null){


			// create global Space
			this.syetemSpace = this.create(
					new Space.Builder().owner(systemUser)
							.name(SpaceRepository.SYSTEM_CONTEXT_NAME)
							.title(SpaceRepository.SYSTEM_CONTEXT_TITLE)
							.description("The global system context")
							.visibility(ContextVisibilityType.SECRET)
							.build());

			Space geekologue = this.create(new Space.Builder()
					.owner(systemUser)
					.name("GKLG")
					.title("Geekologue, EOOD")
					.description("Root business space for Geekologue, EOOD")
					.visibility(ContextVisibilityType.CLOSED)
					.build());
		}

		Map<String, User> testUsers = createTestSpaceUsers();

		createTestFriendships(testUsers);
		String[][] membershipsData = { { "No-Tec", "Squeeek" }, { "No-Tec", "Chapperwocky" },
				{ "No-Tec", "DKang0002" }, { "Squeeek", "No-Tec" }, { "Squeeek", "Chapperwocky" },
				{ "Squeeek", "DKang0002" }, { "Chapperwocky", "No-Tec" }, { "Chapperwocky", "Squeeek" },
				{ "Chapperwocky", "DKang0002" }, { "DKang0002", "Squeeek" }, { "DKang0002", "No-Tec" },
				{ "DKang0002", "Chapperwocky" }, { "DKang0002", "ITakeRequests" }, { "Ecnassianer", "Squeeek" },
				{ "Ecnassianer", "No-Tec" }, { "Ecnassianer", "Chapperwocky" }, { "Ecnassianer", "ITakeRequests" },
				{ "Ecnassianer", "TotallyTroy" }, { "Ecnassianer", "HiBame" }, { "Ecnassianer", "OrangeFa1ry" } };

		for (int i = 0; i < membershipsData.length; i++) {

			String[] inv = membershipsData[i];
			User owner = testUsers.get(inv[0]);
			User member = testUsers.get(inv[1]);
			Space space = this.getUserSpace(owner);

			// skip if membership request already exists
			if (!this.membershipRequestService.exists(space, member)) {
				log.debug("User: {}, {}, owner: {}, member: {}", inv[0], inv[01],
						(owner != null ? owner.getUsername() : null),
						(member != null ? member.getUsername() : null));
				// invite user to space

				MembershipRequest invitation = new MembershipRequest.Builder().user(member)
						.status(MembershipRequestStatus.CONFIRMED).context(space).build();
				this.membershipRequestService.createConfirmed(invitation);
			}

		}
	}

	protected void createTestFriendships(Map<String, User> testUsers) {
		// new friendships
		MultiValuedMap<String, String> friends = new HashSetValuedHashMap<String, String>();
		friends.put("No-Tec", "Squeeek");
		friends.put("No-Tec", "Chapperwocky");
		friends.put("No-Tec", "DKang0002");
		friends.put("Squeeek", "No-Tec");
		friends.put("Squeeek", "Chapperwocky");
		friends.put("Squeeek", "DKang0002");
		friends.put("Chapperwocky", "No-Tec");
		friends.put("Chapperwocky", "Squeeek");
		friends.put("Chapperwocky", "DKang0002");
		friends.put("DKang0002", "Squeeek");
		friends.put("DKang0002", "No-Tec");
		friends.put("DKang0002", "Chapperwocky");
		friends.put("DKang0002", "ITakeRequests");
		friends.put("Ecnassianer", "Squeeek");
		friends.put("Ecnassianer", "No-Tec");
		friends.put("Ecnassianer", "Chapperwocky");
		friends.put("Ecnassianer", "ITakeRequests");
		friends.put("Ecnassianer", "TotallyTroy");
		friends.put("Ecnassianer", "HiBame");
		friends.put("Ecnassianer", "OrangeFa1ry");

		// add more friends
		User admin = this.userService.findActiveByUsername("admin");
		log.debug("createTestFriendships admin: {}", admin);

		for (String username : friends.keySet()) {
			User one = testUsers.get(username);

			createFriendship(one, admin);

			Collection<String> inverseFriends = friends.get(username);
			log.debug("createTestFriendships username: {}, user: {}, friends: {}", username, one.getUsername(),
					inverseFriends);
			for (String sOther : inverseFriends) {

				User other = testUsers.get(sOther);

				createFriendship(one, other);
			}
		}
	}

	private void createFriendship(User one, User other) {
		Friendship f = new Friendship(one, other);
		if (!friendshipService.exists(f)) {
            log.debug("createTestFriendships friendship: {}", f);
            f = this.friendshipService.createTest(f);
        }
	}

	protected Map<String, User> createTestSpaceUsers() {
		Map<String, User> testUsers = new HashMap<String, User>();
		Map<String, String> userAvatars = new HashMap<String, String>();
		userAvatars.put("No-Tec", "/img/avatars/user/evil-trump.jpeg");
		userAvatars.put("Squeeek", "/img/avatars/user/squirrel-girl.jpg");
		userAvatars.put("Chapperwocky", "/img/avatars/user/slim-pickens.jpg");
		userAvatars.put("DKang0002", "/img/avatars/user/pow.jpg");
		userAvatars.put("Ecnassianer", "/img/avatars/user/squirrel-revolution.jpg");
		userAvatars.put("ITakeRequests", "/img/avatars/user/wolverine.jpg");
		userAvatars.put("TotallyTroy", "/img/avatars/user/zap.jpg");
		userAvatars.put("HiBame", "/img/avatars/user/dr-doom.jpg");
		userAvatars.put("OrangeFa1ry", "/img/avatars/user/homer-wall.jpg");
		userAvatars.put("GreenFa1ry", null);

		for (String username : userAvatars.keySet()) {
			User u = new User.Builder().username(username)
					.credentials(new UserCredentials.Builder().active(true).password(username).build())
					.contactDetails(new ContactDetails.Builder()
							.primaryEmail(new EmailDetail(username + "@evasyst.com")).build())
					.firstName(username.substring(0, 3)).lastName(username.substring(3)).build();
			testUsers.put(username, userService.createAsConfirmed(u));
		}


		return testUsers;
	}
	@Override
	public Space getSystemContext(){
		if(this.syetemSpace == null){
			this.syetemSpace = this.repository.getSystemContext();
		}
		return this.syetemSpace;
	}

}