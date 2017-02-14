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
package com.restdude.domain.friends.repository;


import com.restdude.domain.friends.model.FriendshipIdentifier;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.friends.model.FriendshipStatus;
import com.restdude.domain.users.model.UserDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
@SuppressWarnings("unused")
//@JaversSpringDataAuditable
public interface FriendshipRepository extends ModelRepository<Friendship, FriendshipIdentifier> {

    static final String SELECT_FRIEND_AS_USERDTO = "select new com.restdude.domain.users.model.UserDTO(friendship.pk.right.pk, "
            + "		friendship.pk.right.firstName, "
            + "		friendship.pk.right.lastName, "
            + "		friendship.pk.right.username, "
            + "		friendship.pk.right.contactDetails.primaryEmail.email, "
            + "		friendship.pk.right.emailHash, "
            + "		friendship.pk.right.avatarUrl, "
            + "		friendship.pk.right.bannerUrl, "
            + "		friendship.pk.right.stompSessionCount"
            + ") ";


    static final String IS_FRIEND = " (friendship.pk.left.pk =  ?1 "
            + "and friendship.status = com.restdude.domain.friends.model.FriendshipStatus.CONFIRMED) ";


    static final String FROM__FRIENDS_BY_USERID = " from Friendship friendship where " + IS_FRIEND;
    //
    static final String FROM__STOMPONLINE_FRIENDS_BY_USERID = " from Friendship friendship where " + IS_FRIEND + " and friendship.pk.right.stompSessionCount > 0 ";

    static final String QUERY_FRIEND_USERNAMES_BY_USERID = "select friendship.pk.right.username " + FROM__FRIENDS_BY_USERID;
    static final String QUERY_STOMPONLINE_FRIEND_USERNAMES_BY_USERID = "select friendship.pk.right.username " + FROM__STOMPONLINE_FRIENDS_BY_USERID;

    static final String QUERY_FRIENDS_BY_USERID = SELECT_FRIEND_AS_USERDTO + FROM__FRIENDS_BY_USERID;

    @Query(QUERY_STOMPONLINE_FRIEND_USERNAMES_BY_USERID)
    List<String> findAllStompOnlineFriendUsernames(String userId);

    @Query("select f.status from Friendship f where f.pk = ?1 ")
    FriendshipStatus getCurrentStatus(FriendshipIdentifier id);

    @Query(QUERY_FRIEND_USERNAMES_BY_USERID)
    Iterable<String> findAllFriendUsernames(String userId);


    @Query(QUERY_FRIENDS_BY_USERID)
    Iterable<UserDTO> findAllFriends(String userId);

    //@Query(QUERY_FRIENDS_BY_USERID)
    //Page<UserDTO> findAllFriendsPaginated(String userId, Pageable pageRequest);
}
