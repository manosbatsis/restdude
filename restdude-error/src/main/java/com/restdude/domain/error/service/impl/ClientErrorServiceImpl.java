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
package com.restdude.domain.error.service.impl;

import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.error.model.ClientError;
import com.restdude.domain.error.repository.ClientErrorRepository;
import com.restdude.domain.error.service.ClientErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named(ClientErrorService.BEAN_ID)
public class ClientErrorServiceImpl extends AbstractErrorServiceImpl<ClientError, ClientErrorRepository>
        implements ClientErrorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientErrorServiceImpl.class);

    @Override
    public CaseWorkflow getWorkflow() {
        if(this.workflow == null){
            this.workflow = this.repository.getWorkflow();
        }
        return this.workflow;
    }

    public String getWorkflowName(){
        return ClientErrorRepository.ERRORS_WORKFLOW_NAME;
    }


}