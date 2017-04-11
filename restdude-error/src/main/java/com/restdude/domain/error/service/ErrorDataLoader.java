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
package com.restdude.domain.error.service;

import com.restdude.domain.cases.service.MembershipContextDataLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Named;

/**
 * Initial data
 */
@Slf4j
@Component
@Named("errorDataLoader")
public class ErrorDataLoader {

    private MembershipContextDataLoader membershipContextDataLoader;
    private BaseErrorService baseErrorService;
    private SystemErrorService systemErrorService;
    private ClientErrorService clientErrorService;

    @Autowired
    public void setMembershipContextDataLoader(MembershipContextDataLoader membershipContextDataLoader) {
        this.membershipContextDataLoader = membershipContextDataLoader;
    }

    @Autowired
    public void setBaseErrorService(BaseErrorService baseErrorService) {
        this.baseErrorService = baseErrorService;
    }

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    @Autowired
    public void setClientErrorService(ClientErrorService clientErrorService) {
        this.clientErrorService = clientErrorService;
    }

    @PostConstruct
    @Transactional(readOnly = false)
    public void run() {
        log.debug("run");
        baseErrorService.initData();
        systemErrorService.initData();
        clientErrorService.initData();
    }
}
