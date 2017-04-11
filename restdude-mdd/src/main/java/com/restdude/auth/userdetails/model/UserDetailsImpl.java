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
package com.restdude.auth.userdetails.model;

import com.restdude.auth.model.BasicUserDetailsImpl;
import com.restdude.auth.model.LoginRequest;
import com.restdude.domain.UserDetails;
import com.restdude.domain.users.model.Role;
import com.restdude.domain.users.model.User;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;


//@ApiModel
@XmlRootElement(name = "loggedInUserDetails")
public class UserDetailsImpl extends BasicUserDetailsImpl {
	
	private static final long serialVersionUID = 5206010308112791343L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDetailsImpl.class);


	public static UserDetails anonymous() {
		BasicUserDetailsImpl details = new BasicUserDetailsImpl();
		details.setUsername("anonymousUser");
		details.setAuthorities( Role.ROLES_ANONYMOUD);
		return details;
	}

	public static UserDetails fromUser(User user) {
		UserDetailsImpl details = null;
		if (user != null) {
			details = new UserDetailsImpl();
			BeanUtils.copyProperties(user, details);
			if (user.getPk() != null) {
				details.setPk(user.getPk().toString());
			}
			if (user.getCredentials() != null) {
				BeanUtils.copyProperties(user.getCredentials(), details, "pk");
			}

			// init global roles
			if (!CollectionUtils.isEmpty(user.getRoles())) {
				List<GrantedAuthority> authorities = user.getRoles().stream()
						.map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
						.collect(Collectors.toList());
				details.setAuthorities(authorities);
				List<String> roles = authorities.stream()
						.map(role -> role.getAuthority())
						.collect(Collectors.toList());
				details.setRoles(roles);
			}

			
		}
		return details;
	}

	/**
	 * Default constructor
	 */
	public UserDetailsImpl() {
		super();
	}

	public UserDetailsImpl(LoginRequest loginSubmission) {
		super(loginSubmission);
	}



}
