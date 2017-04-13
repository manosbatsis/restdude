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
package com.restdude.domain.cases.repository;

import com.restdude.domain.cases.model.SpaceContext;
import com.restdude.domain.cases.model.dto.SpaceContextInfo;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.repository.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface ContextRepository<T extends SpaceContext> extends ModelRepository<T,String> {

    String SYSTEM_CONTEXT_NAME = "SYS_CTX";
    String SYSTEM_CONTEXT_TITLE = "System context";

    String BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS = " context.pk, context.name, context.description, context.avatarUrl, context.bannerUrl,  " +
            "  context.owner.pk, context.owner.firstName, context.owner.lastName, context.owner.username, context.owner.contactDetails.primaryEmail.email, " +
            "context.owner.emailHash, context.owner.avatarUrl, context.owner.bannerUrl, " +
            "context.owner.stompSessionCount, context.visibility ";
    String SELECT_BUSINESS_CONTEXT_INFO =
            "select new com.restdude.domain.cases.model.dto.SpaceContextInfo(" + BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS + ") from SpaceContext context ";
    String SELECT_COUNT_BUSINESS_CONTEXT_IDS = "select count(context.pk) from SpaceContext context ";
    String VISIBLE = "  left join context.memberships as membership "
            + "where context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC "
            + " or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + " or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.CLOSED "
            + " or membership.user.pk = ?#{principal.pk} ";
    String FIND_VISIBLE = SELECT_BUSINESS_CONTEXT_INFO + VISIBLE + "group by " + BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS;
    String COUNT_VISIBLE = SELECT_COUNT_BUSINESS_CONTEXT_IDS + VISIBLE + "group by context.pk";

    // Using GROUP BY because of a Hibernatre bug related toDISTINCT keyword
    @Query(value = FIND_VISIBLE)
    Page<SpaceContextInfo> findVisible(Pageable pageable);

    // Using GROUP BY because of a Hibernatre bug related toDISTINCT keyword
    @Query(value = SELECT_BUSINESS_CONTEXT_INFO +
            "   join context.memberships as contextMembership " +
            "   left join context.memberships as membership " +
            " where contextMembership.user = ?1 " +
            "   and ( contextMembership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC " +
            "     or contextMembership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN " +
            "     or contextMembership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.CLOSED " +
            "     or membership.user.pk = ?#{principal.pk} ) " +
            " group by " + BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS)
    Page<SpaceContextInfo> findVisibleMembershipSpaceContexts(User u, Pageable pageable);

    @Query(value = "select CASE WHEN COUNT(context) > 0 THEN true ELSE false END  "
            + "from SpaceContext context "
            + "  left join context.memberships as membership "
            + "where context.pk = ?1 "
            + "		and (context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC "
            + "         or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + "         or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.CLOSED "
            + "         or membership.user.pk = ?#{principal.pk} ) ")
    boolean isVisible(String id);


    @Query("select (persisted.owner.pk = :#{#user.pk}) from SpaceContext persisted where persisted.pk = :#{#context.pk} ")
    boolean isOwner(@Param("context") SpaceContext context, @Param("user") User user);

    @Query(value = "select CASE WHEN COUNT(membership) > 0 THEN true ELSE false END  "
            + "from Membership membership "
            //		+ "  left join membership.user.friendships as friendship "
            + "where membership.user.pk = ?#{principal.pk} and membership.context.pk = :#{#context.pk} "
            + "		and (membership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC "
            + "         or membership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + "         or membership.context.owner.pk = ?#{principal.pk} ) ")
    boolean canInviteUserToSpaceContext(@Param("context") SpaceContext context);

    @Query(value = "select CASE WHEN COUNT(membership) > 0 THEN true ELSE false END  "
            + "from Membership membership "
            + "where membership.user.pk = ?#{principal.pk} and membership.context.pk = :#{#context.pk} "
            + "		and (membership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + "         or membership.context.owner.pk = ?#{principal.pk} ) ")
    boolean canApproveRequestToJoinSpaceContext(@Param("context") SpaceContext context);

    @Query(value = SELECT_BUSINESS_CONTEXT_INFO + " where context.owner.pk = ?#{principal.pk} ")
    Page<SpaceContextInfo> findMy(Pageable pageable);

    @Query(value = "select context from SpaceContext context  where context.owner.username = 'system' and context.name = '" + SYSTEM_CONTEXT_NAME + "' ")
    SpaceContext getSystemContext();
}
