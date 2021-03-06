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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.domain.UserDetails;
import com.restdude.domain.cases.model.BaseContext;
import com.restdude.domain.cases.model.Membership;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.cases.repository.ContextNpRepositoryBean;
import com.restdude.domain.cases.service.ContextService;
import com.restdude.domain.cases.service.MembershipRequestService;
import com.restdude.domain.cases.service.MembershipService;
import com.restdude.domain.friends.repository.FriendshipRepository;
import com.restdude.domain.friends.service.FriendshipService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.util.exception.http.ForbiddenException;
import com.restdude.util.exception.http.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
public abstract class AbstractContextServiceImpl<T extends BaseContext, R extends ContextNpRepositoryBean<T>>
        extends AbstractPersistableModelServiceImpl<T, String, R> implements ContextService<T> {

    protected MembershipService membershipService;
    protected FriendshipRepository friendshipRepository;

    protected UserService userService;
    protected FriendshipService friendshipService;
    protected MembershipRequestService membershipRequestService;

    protected Space syetemSpace;

    @Override
    public Space getSystemContext(){
        if(this.syetemSpace == null){
            this.syetemSpace = this.repository.getSystemContext();
        }
        return this.syetemSpace;
    }
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
    @Autowired
    public void setMembershipService(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @Autowired
    public void setFriendshipRepository(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findByName(String name) {
        return this.repository.findByName(name);
    }

    /**
     * Create a context and the owner's membership
     */
    @Transactional(readOnly = false)
    public T create(T resource) {
        if(resource.getOwner() == null || resource.getOwner().getId() == null){
            UserDetails ud = this.getPrincipal();
            if(StringUtils.isNotBlank(ud.getId())){
                resource.setOwner(new User(ud.getId()));
            }
        }
        if(Objects.nonNull(resource.getParent())){
            resource.setParent(this.repository.findOne(resource.getParent().getId()));
        }
        log.debug("create resource: {}", resource);
        // create the context
        resource = super.create(resource);

        // create the owner membership
        this.membershipService.create(new Membership.Builder()
                .context(resource)
                .user(resource.getOwner())
                .build());

        return resource;
    }

    @Override
    public T findById(String id) {
        if(!this.repository.isVisible(id)){
            throw new UnauthorizedException("Unauthorized (non-owner) or not found");
        }
        return super.findById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public T update(@P("resource") T resource) {
        ensureAuthorized(resource);
        return super.update(resource);
    }

    private void ensureAuthorized(@P("resource") T resource) {
        String principalId = this.getPrincipal().getId();
        // ensure request was made by owner
        if (principalId == null || !this.repository.isOwner(resource, new User(principalId))) {
            throw new UnauthorizedException("Unauthorized (non-owner) or not found");
        }
    }

    @Override
    @Transactional(readOnly = false)
    public T patch(@P("resource") T resource) {
        ensureAuthorized(resource);
        return super.patch(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(SpelUtil.HAS_ROLE_USER)
    public Page<BaseContextInfo> findVisible(Pageable pageable) {
        return this.repository.findVisible(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(SpelUtil.HAS_ROLE_USER)
    public Page<BaseContextInfo> findMy(Pageable pageable) {
        return this.repository.findMy(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(SpelUtil.HAS_ROLE_USER)
    public void delete(String id) {
        T resource = ensureAuthorizedAndGetPersisted(id);
        super.delete(resource);
    }


    @Transactional(readOnly = false)
    @PreAuthorize(SpelUtil.HAS_ROLE_USER)
    public void delete(T resource) {
        ensureAuthorized(resource);
        super.delete(resource);
    }


    @Override
    public void deleteAll() {
        throw new ForbiddenException("Unauthorized");
    }

    private T ensureAuthorizedAndGetPersisted(String id) {
        T resource = this.repository.getOne(id);
        String principalId = this.getPrincipal().getId();
        // ensure request was made by owner
        if (principalId == null || !resource.getOwner().getId().equals(principalId)) {
            throw new ForbiddenException("Unauthorized (non-owner) or not found");
        }
        return resource;
    }

    @Override
    public Set<String> getActivityNotificationRecepients(User activityUser, T resource) {
        Set<String> recepientUsernames = new HashSet<>();
        // add online C2 members
        recepientUsernames.addAll(this.membershipService.findOnlineMemberUsernames(resource));

        // if context is public, add friends, let the set filter out dupes
        if(ContextVisibilityType.PUBLIC.equals(resource.getVisibility())){
            recepientUsernames.addAll(this.friendshipRepository.findAllStompOnlineFriendUsernames(activityUser.getId()));
        }
        return recepientUsernames;
    }
}