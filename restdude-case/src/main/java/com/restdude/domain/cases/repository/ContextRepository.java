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

import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.repository.ModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface ContextRepository<T extends Space> extends ModelRepository<T,String> {

    String SYSTEM_CONTEXT_NAME = "SYS_CTX";
    String SYSTEM_CONTEXT_TITLE = "System context";

    String BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS = " context.pk, context.name, context.title, context.description, context.avatarUrl, context.bannerUrl,  " +
            "  context.owner.pk, context.owner.firstName, context.owner.lastName, context.owner.username, context.owner.contactDetails.primaryEmail.email, " +
            "context.owner.emailHash, context.owner.avatarUrl, context.owner.bannerUrl, " +
            "context.visibility ";
    String SELECT_BUSINESS_CONTEXT_INFO =
            "select new com.restdude.domain.cases.model.dto.BaseContextInfo(" + BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS + ") from Space context ";
    String SELECT_COUNT_BUSINESS_CONTEXT_IDS = "select count(context.pk) from Space context ";
    String VISIBLE = "  left join context.memberships as membership "
            + "where context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC "
            + " or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + " or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.CLOSED "
            + " or membership.user.pk = ?#{principal.pk} ";
    String FIND_VISIBLE = SELECT_BUSINESS_CONTEXT_INFO + VISIBLE + "group by " + BUSINESSCONTEXTINFO_CONSTRUCTOR_PARAMS;
    String COUNT_VISIBLE = SELECT_COUNT_BUSINESS_CONTEXT_IDS + VISIBLE + "group by context.pk";

    // Using GROUP BY because of a Hibernatre bug related toDISTINCT keyword
    @Query(value = FIND_VISIBLE)
    Page<BaseContextInfo> findVisible(Pageable pageable);

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
    Page<BaseContextInfo> findVisibleMembershipSpaces(User u, Pageable pageable);

    @Query(value = "select CASE WHEN COUNT(context) > 0 THEN true ELSE false END  "
            + "from Space context "
            + "  left join context.memberships as membership "
            + "where context.pk = ?1 "
            + "		and (context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC "
            + "         or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + "         or context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.CLOSED "
            + "         or membership.user.pk = ?#{principal.pk} ) ")
    boolean isVisible(String id);


    @Query("select (persisted.owner.pk = :#{#user.pk}) from Space persisted where persisted.pk = :#{#context.pk} ")
    boolean isOwner(@Param("context") Space context, @Param("user") User user);

    @Query(value = "select CASE WHEN COUNT(membership) > 0 THEN true ELSE false END  "
            + "from Membership membership "
            //		+ "  left join membership.user.friendships as friendship "
            + "where membership.user.pk = ?#{principal.pk} and membership.context.pk = :#{#context.pk} "
            + "		and (membership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.PUBLIC "
            + "         or membership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + "         or membership.context.owner.pk = ?#{principal.pk} ) ")
    boolean canInviteUserToSpace(@Param("context") Space context);

    @Query(value = "select CASE WHEN COUNT(membership) > 0 THEN true ELSE false END  "
            + "from Membership membership "
            + "where membership.user.pk = ?#{principal.pk} and membership.context.pk = :#{#context.pk} "
            + "		and (membership.context.visibility = com.restdude.domain.cases.model.enums.ContextVisibilityType.OPEN "
            + "         or membership.context.owner.pk = ?#{principal.pk} ) ")
    boolean canApproveRequestToJoinSpace(@Param("context") Space context);

    @Query(value = SELECT_BUSINESS_CONTEXT_INFO + " where context.owner.pk = ?#{principal.pk} ")
    Page<BaseContextInfo> findMy(Pageable pageable);

    @Query(value = "select context from Space context  where context.owner.username = 'system' and context.name = '" + SYSTEM_CONTEXT_NAME + "' ")
    Space getSystemContext();
}
