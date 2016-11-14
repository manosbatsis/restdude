package com.restdude.domain.friends.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

public enum FriendshipStatus {
	SENT, DELETE, PENDING, CONFIRMED, BLOCK, BLOCK_INVERSE;
	

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipStatus.class);

	private static Map<FriendshipStatus, Set<FriendshipStatus>> allowedNext = new HashMap<FriendshipStatus, Set<FriendshipStatus>>();
	private static Map<FriendshipStatus, FriendshipStatus> applicableInverse = new HashMap<FriendshipStatus, FriendshipStatus>();
	
		
	static {
		
		// allowed "next" choices per status
		allowedNext.put(null, new HashSet<FriendshipStatus>(Arrays.asList(SENT, BLOCK)));
		allowedNext.put(SENT, new HashSet<FriendshipStatus>(Arrays.asList(DELETE)));
		allowedNext.put(PENDING, new HashSet<FriendshipStatus>(Arrays.asList(CONFIRMED, DELETE, BLOCK)));
		allowedNext.put(CONFIRMED, new HashSet<FriendshipStatus>(Arrays.asList(DELETE, BLOCK)));
		allowedNext.put(BLOCK, new HashSet<FriendshipStatus>(Arrays.asList(DELETE)));
		
		// applied "inverse" value per selected status 
		applicableInverse.put(SENT, PENDING);
		applicableInverse.put(CONFIRMED, CONFIRMED);
		applicableInverse.put(DELETE, DELETE);
		applicableInverse.put(BLOCK, BLOCK_INVERSE);

	}

	public static Set<FriendshipStatus> getAllowedNext(@Nullable FriendshipStatus current){
		return allowedNext.get(current);
	}
	
	
	public static boolean isAllowedNext(@Nullable FriendshipStatus current, @Nullable FriendshipStatus next){
		boolean allowed =  allowedNext.get(current).contains(next);
		LOGGER.debug("isAllowedNext, current: {} ,next: {}, allowed: {}" + allowed, current, next);
		return allowed;
	}
	

	public static FriendshipStatus getApplicableInverse(@Nullable FriendshipStatus current){
		return applicableInverse.get(current);
	}
	
}