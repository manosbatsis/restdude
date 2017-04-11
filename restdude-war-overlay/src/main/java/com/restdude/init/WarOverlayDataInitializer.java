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

import com.restdude.domain.users.model.User;
import com.restdude.util.ConfigurationFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Named;


/**
 * {@inheritDoc}
 */
@Component
@Named(WarOverlayDataInitializer.BEAN_NAME)
public class WarOverlayDataInitializer extends DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarOverlayDataInitializer.class);
    public static final String BEAN_NAME = "calipsoDataInitializer";


    @Override
    protected String getTestEmailDomain() {
        Configuration config = ConfigurationFactory.getConfiguration();
        return config.getString("restdude.testEmailDomain");
    }

    @PostConstruct
    @Transactional(readOnly = false)
    public void run() {

        super.run();
        ;

        Configuration config = ConfigurationFactory.getConfiguration();
        boolean initData = config.getBoolean(ConfigurationFactory.INIT_DATA, true);



        // send test email?
        if (config.getBoolean(ConfigurationFactory.TEST_EMAIL_ENABLE, false)) {
            String testEmailUsername = config.getString(ConfigurationFactory.TEST_EMAIL_USER, "system");
            if (StringUtils.isNotBlank(testEmailUsername)) {
                User u = this.userService.findOneByUserNameOrEmail(testEmailUsername);
                this.emailService.sendTest(u);
            }
        }


    }



}
