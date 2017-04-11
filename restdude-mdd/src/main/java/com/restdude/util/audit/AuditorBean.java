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
package com.restdude.util.audit;

import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.UserDetails;
import com.restdude.domain.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.AuditorAware;

public class AuditorBean implements AuditorAware<User> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuditorBean.class);
    public static final String BEAN_NAME = "auditorBean";

    private UserDetailsService userDetailsService;


    @Autowired(required = true)
    @Qualifier("userDetailsService") // somehow required for CDI to work on 64bit JDK?
    public void setLocalUserService(
            UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public User getCurrentAuditor() {
        User currentAuditor = null;

        UserDetails userDetails = SecurityUtil.getPrincipal();
        if (userDetails != null && userDetails.getPk() != null) {
            currentAuditor = new User(userDetails.getPk());
            currentAuditor.setUsername(userDetails.getUsername());
            currentAuditor.setFirstName(userDetails.getFirstName());
            currentAuditor.setLastName(userDetails.getLastName());
            currentAuditor.setName(userDetails.getName());
            currentAuditor.setAvatarUrl(userDetails.getAvatarUrl());
        }
        LOGGER.debug("getCurrentAuditor: {}", currentAuditor);
        return currentAuditor;
    }


}