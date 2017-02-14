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
package com.restdude.domain.friends.controller;

import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.friends.model.FriendshipIdentifier;
import com.restdude.domain.friends.model.FriendshipStatus;
import com.restdude.domain.friends.service.FriendshipService;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.domain.users.service.UserService;
import com.restdude.mdd.controller.PageableUtil;
import com.restdude.mdd.specifications.SpecificationsBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Friends", description = "Friend searches")
@RequestMapping(value = "/api/rest/friends", produces = { "application/json", "application/xml" })
public class FriendsController implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendsController.class);

    private SpecificationsBuilder<Friendship, FriendshipIdentifier> specificationsBuilder;
    @Autowired
    protected HttpServletRequest request;

    @Inject
    @Qualifier(FriendshipService.BEAN_ID)
    FriendshipService friendshipService;

    @Inject
    @Qualifier("userService")
    UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.specificationsBuilder = new SpecificationsBuilder<>(Friendship.class, this.friendshipService.getConversionService());
    }

    @RequestMapping(value = {"my" }, method = RequestMethod.GET)
    @ApiOperation(value = "Find all friends (paginated)", notes = "Find all friends of the current user. Returns paginated results")
    public Page<UserDTO> findMyFriendsPaginated(
            @ApiParam(name = "status", required = false, allowableValues = "SENT, PENDING, CONFIRMED, BLOCK", allowMultiple = true, defaultValue = "CONFIRMED")
            @RequestParam(value = "status", required = false, defaultValue = "CONFIRMED") String[] status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "pk") String sort) {
        // validate status
        status = getValidatedStatus(status, new FriendshipStatus[]{FriendshipStatus.SENT, FriendshipStatus.PENDING, FriendshipStatus.CONFIRMED, FriendshipStatus.BLOCK});
        return this.findFriendsPaginated(this.friendshipService.getPrincipal().getPk(), status, page, size, sort);
    }

    @RequestMapping(value = {"{friendId}"}, method = RequestMethod.GET)
    @ApiOperation(value = "Find all friends of a friend (paginated)", notes = "Find all friends of a friend. Returns paginated results")
    public Page<UserDTO> findAFriendsFriendsPaginated(
            @ApiParam(name = "friendId", required = true, value = "string") @PathVariable String friendId,
            @ApiParam(name = "status", required = false, allowableValues = "SENT, PENDING, CONFIRMED, BLOCK", allowMultiple = true, defaultValue = "CONFIRMED")
            @RequestParam(value = "status", required = false, defaultValue = "CONFIRMED") String[] status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "pk") String sort) {

        // validate targget friend
//		FriendshipIdentifier friendshipId = new FriendshipIdentifier(this.friendshipService.getPrincipal().getIdentifier(), friendId);
//		Friendship friendship = this.friendshipService.findById(friendshipId);
//		if(friendship == null 
//				|| !(FriendshipStatus.CONFIRMED.equals(friendship.getStatus())
//						|| FriendshipStatus.PENDING.equals(friendship.getStatus()))){
//			throw new IllegalArgumentException("Unauthorized");
//		}

        // validate status
        status = getValidatedStatus(status, new FriendshipStatus[]{FriendshipStatus.CONFIRMED});

        return this.findFriendsPaginated(friendId, status, page, size, sort);
    }


    protected Page<UserDTO> findFriendsPaginated(
            String targetId,
            String[] status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "pk") String sort) {

        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("pk.left.pk", new String[]{targetId});
        parameters.put("status", status);

        Pageable pageable = PageableUtil.buildPageable(page, size, sort);

        Specification<Friendship> spec = this.specificationsBuilder.<Friendship>build(parameters);

        Page<Friendship> friendshipPage = this.friendshipService.findPaginated(spec, pageable);
        LOGGER.debug("Found {} friendships for status {}", friendshipPage.getTotalElements(), status);
        // TODO: move DTO selection to query
        List<UserDTO> frieds = new ArrayList<UserDTO>(friendshipPage.getNumberOfElements());
        for(Friendship friendship : friendshipPage){
            frieds.add(UserDTO.fromUser(friendship.getPk().getRight()));
        }

        PageImpl<UserDTO> friends = new PageImpl<UserDTO>(frieds, pageable, friendshipPage.getTotalElements());
        return friends;
    }


    /**
     * @return
     */
    protected String[] getValidatedStatus(String[] status, FriendshipStatus[] allowed) {
        // validate status
        if(status == null || status.length == 0){
            status = new String[]{FriendshipStatus.CONFIRMED.toString()};
        }
        else{
            for(String stat : status){
                if(stat.equals(FriendshipStatus.BLOCK_INVERSE) || FriendshipStatus.valueOf(stat) == null){
                    throw new IllegalArgumentException("Invalid status pathFragment: "+ stat);
                }
            }
        }
        return status;
    }


}
