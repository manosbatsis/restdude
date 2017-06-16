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
package com.restdude.domain.cases.init;

import com.restdude.domain.cases.service.SpaceService;
import com.restdude.domain.users.service.UserDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Named;

/**
 * Initializes user records
 */
@Component
@Named("spaceDataLoader")
public class SpaceDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private UserDataLoader userDataLoader;
    private SpaceService spaceContextService;

    @Autowired
    public void setUserDataLoader(UserDataLoader userDataLoader) {
        this.userDataLoader = userDataLoader;
    }

    @Autowired
    public void setSpaceService(SpaceService spaceContextService) {
        this.spaceContextService = spaceContextService;
    }


    /**
     * Handle an application event.

     * @param event the event to respond to
     */
    @Transactional(readOnly = false)
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }
}
