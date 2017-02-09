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
package com.restdude.domain.users.service.impl;

import com.restdude.mdd.service.AbstractModelServiceImpl;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.repository.UserCredentialsRepository;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.users.service.UserCredentialsService;
import com.restdude.util.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;


@Named("userCredentialsService")
public class UserCredentialsServiceImpl extends AbstractModelServiceImpl<UserCredentials, String, UserCredentialsRepository> implements UserCredentialsService {

    private UserRepository roleRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public UserCredentials create(@P("resource") UserCredentials resource) {
        // require password for active
        if (resource.getActive() && resource.getPassword() == null) {
            throw new BadRequestException("Cannot create active user without a pre-set password");
        }
        return super.create(resource);
    }

}