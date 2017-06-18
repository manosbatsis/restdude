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

import javax.inject.Named;

import com.restdude.domain.cases.service.SpaceService;
import com.restdude.domain.error.service.ClientErrorService;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.domain.users.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Initial data
 */
@Slf4j
@Component
public class ApplicationReadyListenerDataLoader {

    private UserService userService;
    private SpaceService spaceContextService;
    private SystemErrorService systemErrorService;
    private ClientErrorService clientErrorService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSpaceContextService(SpaceService spaceContextService) {
        this.spaceContextService = spaceContextService;
    }

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    @Autowired
    public void setClientErrorService(ClientErrorService clientErrorService) {
        this.clientErrorService = clientErrorService;
    }

    /**
     * Handle an application event.

     * @param event the event to respond to
     */
    @Transactional(readOnly = false)
    @EventListener
    @Order(10)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.warn("onApplicationEvent, event: {}", event);

        userService.initData();
        spaceContextService.initData();

        systemErrorService.initData();
        clientErrorService.initData();
    }
}
