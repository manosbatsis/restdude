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
package com.restdude.init;

import java.util.Arrays;

import com.restdude.domain.Roles;
import com.restdude.domain.error.service.ErrorLogService;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.domain.geography.service.CountryService;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.repository.UserRegistrationCodeBatchRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.users.service.RoleService;
import com.restdude.domain.users.service.UserService;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.email.service.EmailService;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Creates initial roles users, countrries etc.
 */
public abstract class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    protected UserService userService;
    protected EmailService emailService;
    protected CountryService countryService;
    protected RoleService roleService;
    protected UserRepository userRepository;
    protected ModelRepository<UserCredentials, String> credentialsRepository;

    protected UserRegistrationCodeRepository userRegistrationCodeRepository;
    protected UserRegistrationCodeBatchRepository userRegistrationCodeBatchRepository;

    protected SystemErrorService systemErrorService;
    protected ErrorLogService stackTraceService;

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    @Autowired
    public void setStackTraceService(ErrorLogService stackTraceService) {
        this.stackTraceService = stackTraceService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCredentialsRepository(ModelRepository<UserCredentials, String> credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Autowired
    public void setUserRegistrationCodeRepository(UserRegistrationCodeRepository userRegistrationCodeRepository) {
        this.userRegistrationCodeRepository = userRegistrationCodeRepository;
    }

    @Autowired
    public void setUserRegistrationCodeBatchRepository(UserRegistrationCodeBatchRepository userRegistrationCodeBatchRepository) {
        this.userRegistrationCodeBatchRepository = userRegistrationCodeBatchRepository;
    }

    protected abstract String getTestEmailDomain();

    public void run() {

        Configuration config = ConfigurationFactory.getConfiguration();
        boolean initData = config.getBoolean(ConfigurationFactory.INIT_DATA, true);
        String testEmailDomain = getTestEmailDomain();
        LOGGER.debug("run, testEmailDomain: {}", testEmailDomain);

        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken(this.getClass().getName(), this.getClass().getName(),
                        Arrays.asList(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority(Roles.ROLE_USER), new SimpleGrantedAuthority(Roles.ROLE_ADMIN)})));

        if (this.countryService.count() == 0) {
            this.countryService.initContinentsAndCountries();
        } else {

            LOGGER.debug("run, skipping data init");
        }
    }

}
