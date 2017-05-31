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
package com.restdude.domain.users.repository;

import com.restdude.mdd.repository.ModelRepository;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

//#import org.javers.spring.data.JaversSpringDataAuditable;

@JaversSpringDataAuditable
public interface UserRepository extends ModelRepository<User, String> {

    public static final String SELECT_USERDTO = "select new com.restdude.domain.users.model.UserDTO(u.id, "
            + "		u.firstName, "
			+ "		u.lastName, "
			+ "		u.username, "
			+ "		u.contactDetails.primaryEmail.email, "
			+ "		u.emailHash,"
			+ "		u.avatarUrl,"
			+ "		u.bannerUrl,"
			+ "		u.stompSessionCount"
			+ ") ";

    @Query("select u from User u where u.id = UPPER(?1) and u.credentials.active = true")
    public User findActiveById(String id);

    @Query("select u.username from User u where u.id = ?1 ")
    public String findUsernameById(String id);

	@Query("select u from User u left join u.contactDetails.emails as email where u.credentials.active = true and UPPER(email.email) = UPPER(?1) and email.verified = true ")
	public User findActiveByEmail(String email);

	@Query("select u from User u where UPPER(u.username) = UPPER(?1) and u.credentials.active = true")
	public User findActiveByUsername(String username);

	@Query("select u from User u where UPPER(u.username) = UPPER(?1) ")
	public User findByUsername(String username);

	@Query("select u from User u left join u.contactDetails.emails as email where UPPER(email.email) = UPPER(?1) ")
	public User findByEmail(String email);

    @Query("select u from User u left join u.contactDetails.emails as email " +
            "where u.credentials.active = true " +
            "    and (" +
            "       (u.id = ?1)" +
            "       or (UPPER(u.username) = UPPER(?1)) " +
            "       or (UPPER(email.email) = UPPER(?1) and email.verified = true)" +
            "    )")
    public User findActiveByIdOrUsernameOrEmail(String idOrUsernameOrEmail);

	@Query(SELECT_USERDTO
            + "from User u where u.id = ?1")
    public UserDTO findCompactUserById(String id);


    @Modifying
    @Query("UPDATE UserCredentials AS c SET c.lastLogin = CURRENT_TIMESTAMP() WHERE c.user.id = ?1")
    public void updateLastLogin(String userId);

    @Modifying

	@Query("UPDATE User AS u SET u.credentials.resetPasswordTokenCreated = NULL, u.credentials.resetPasswordToken = NULL "
			+ "WHERE u.credentials.resetPasswordTokenCreated IS NOT NULL and u.credentials.resetPasswordTokenCreated  < ?1")
	public void expireResetPasswordTokens(Date yesterday);
	
}
