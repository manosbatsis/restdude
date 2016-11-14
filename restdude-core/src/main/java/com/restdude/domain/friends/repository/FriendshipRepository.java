package com.restdude.domain.friends.repository;


import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.friends.model.FriendshipId;
import com.restdude.domain.friends.model.FriendshipStatus;
import com.restdude.domain.users.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
@SuppressWarnings("unused")
//@JaversSpringDataAuditable
public interface FriendshipRepository extends ModelRepository<Friendship,FriendshipId> {

    public static final String SELECT_FRIEND_AS_USERDTO = "select new com.restdude.domain.users.model.UserDTO(friendship.id.friend.id, "
            + "		friendship.id.friend.firstName, "
			+ "		friendship.id.friend.lastName, "
			+ "		friendship.id.friend.credentials.username, "
			+ "		friendship.id.friend.email, "
			+ "		friendship.id.friend.emailHash, "
			+ "		friendship.id.friend.avatarUrl, "
			+ "		friendship.id.friend.bannerUrl, "
			+ "		friendship.id.friend.stompSessionCount"
			+ ") ";


	public static final String IS_FRIEND = " (friendship.id.owner.id =  ?1 "
            + "and friendship.status = com.restdude.domain.friends.model.FriendshipStatus.CONFIRMED) ";


    static final String FROM__FRIENDS_BY_USERID = " from Friendship friendship where " + IS_FRIEND;
	// 
	static final String FROM__STOMPONLINE_FRIENDS_BY_USERID = " from Friendship friendship where " + IS_FRIEND + " and friendship.id.friend.stompSessionCount > 0 ";

	static final String QUERY_FRIEND_USERNAMES_BY_USERID = "select friendship.id.friend.credentials.username " + FROM__FRIENDS_BY_USERID;
	static final String QUERY_STOMPONLINE_FRIEND_USERNAMES_BY_USERID = "select friendship.id.friend.credentials.username " + FROM__STOMPONLINE_FRIENDS_BY_USERID;
	
	static final String QUERY_FRIENDS_BY_USERID = SELECT_FRIEND_AS_USERDTO + FROM__FRIENDS_BY_USERID;
	
	@Query(QUERY_STOMPONLINE_FRIEND_USERNAMES_BY_USERID)
	public List<String> findAllStompOnlineFriendUsernames(String userId);
    
//    @Query("select f from Friendship f where f.id.owner.id = ?#{principal.id} "
//    		+ "and ( status <> FriendshipStatus.CONFIRMED_INVERSE or  status <> FriendshipStatus.BLOCK_INVERSE) ")
//    List<Friendship> findSentByCurrentUser();
//
//    @Query("select f from Friendship f where f.id.friend.id = ?#{principal.id} "
//    		+ "and ( status <> FriendshipStatus.CONFIRMED_INVERSE or  status <> FriendshipStatus.BLOCK_INVERSE) ")
//    List<Friendship> findReceivedByCurrentUser();


//    @Query("select case when count(f) > 0 then true else false end from Friendship f where "
//    		+ "f.id.owner = ?1 and f.id.friend = ?2 "
//    		+ " and status = FriendshipStatus.CONFIRMED ")
//    Boolean existsConfirmed(User one, User other);
//    
//    @Query("select case when count(f) > 0 then true else false end from Friendship f where "
//    		+ " (f.user = ?1 and f.friend = ?2) ")
//    Boolean existsAny(User one, User other);


	@Query("select f.status from Friendship f where f.id = ?1 ")
	public FriendshipStatus getCurrentStatus(FriendshipId id);
	
	@Query(QUERY_FRIEND_USERNAMES_BY_USERID)
	public Iterable<String> findAllFriendUsernames(String userId);


	@Query(QUERY_FRIENDS_BY_USERID)
	public Iterable<UserDTO> findAllFriends(String userId);

	@Query(QUERY_FRIENDS_BY_USERID)
	public Page<UserDTO> findAllFriendsPaginated(String userId, Pageable pageRequest);
}
