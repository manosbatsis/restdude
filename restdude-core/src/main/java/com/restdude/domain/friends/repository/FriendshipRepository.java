package com.restdude.domain.friends.repository;


import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.friends.model.FriendshipId;
import com.restdude.domain.friends.model.FriendshipStatus;
import com.restdude.domain.users.model.UserDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
@SuppressWarnings("unused")
//@JaversSpringDataAuditable
public interface FriendshipRepository extends ModelRepository<Friendship,FriendshipId> {

    static final String SELECT_FRIEND_AS_USERDTO = "select new com.restdude.domain.users.model.UserDTO(friendship.id.right.id, "
            + "		friendship.id.right.firstName, "
            + "		friendship.id.right.lastName, "
            + "		friendship.id.right.username, "
            + "		friendship.id.right.contactDetails.primaryEmail.email, "
            + "		friendship.id.right.emailHash, "
            + "		friendship.id.right.avatarUrl, "
            + "		friendship.id.right.bannerUrl, "
            + "		friendship.id.right.stompSessionCount"
            + ") ";


    static final String IS_FRIEND = " (friendship.id.left.id =  ?1 "
            + "and friendship.status = com.restdude.domain.friends.model.FriendshipStatus.CONFIRMED) ";


    static final String FROM__FRIENDS_BY_USERID = " from Friendship friendship where " + IS_FRIEND;
	// 
    static final String FROM__STOMPONLINE_FRIENDS_BY_USERID = " from Friendship friendship where " + IS_FRIEND + " and friendship.id.right.stompSessionCount > 0 ";

    static final String QUERY_FRIEND_USERNAMES_BY_USERID = "select friendship.id.right.username " + FROM__FRIENDS_BY_USERID;
    static final String QUERY_STOMPONLINE_FRIEND_USERNAMES_BY_USERID = "select friendship.id.right.username " + FROM__STOMPONLINE_FRIENDS_BY_USERID;

    static final String QUERY_FRIENDS_BY_USERID = SELECT_FRIEND_AS_USERDTO + FROM__FRIENDS_BY_USERID;
	
	@Query(QUERY_STOMPONLINE_FRIEND_USERNAMES_BY_USERID)
	List<String> findAllStompOnlineFriendUsernames(String userId);

	@Query("select f.status from Friendship f where f.id = ?1 ")
	FriendshipStatus getCurrentStatus(FriendshipId id);
	
	@Query(QUERY_FRIEND_USERNAMES_BY_USERID)
	Iterable<String> findAllFriendUsernames(String userId);


	@Query(QUERY_FRIENDS_BY_USERID)
	Iterable<UserDTO> findAllFriends(String userId);

	//@Query(QUERY_FRIENDS_BY_USERID)
	//Page<UserDTO> findAllFriendsPaginated(String userId, Pageable pageRequest);
}
