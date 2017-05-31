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

import com.restdude.domain.cases.model.CaseStatus;
import com.restdude.domain.cases.model.CaseWorkflow;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.repository.AbstractCaseModelRepository;
import com.restdude.domain.cases.service.CaseStatusService;
import com.restdude.domain.cases.service.CaseWorkflowService;
import com.restdude.domain.cases.service.SpaceService;
import com.restdude.domain.cases.service.impl.AbstractCaseServiceImpl;
import com.restdude.domain.error.model.BaseError;
import com.restdude.domain.error.model.ErrorComment;
import com.restdude.domain.error.model.ErrorLog;
import com.restdude.domain.error.model.ErrorsApplication;
import com.restdude.domain.error.repository.ErrorCommentRepository;
import com.restdude.domain.error.service.ErrorLogService;
import com.restdude.domain.error.service.UserAgentService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.service.UserService;
import com.restdude.mdd.service.PersistableModelService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractErrorServiceImpl<T extends BaseError,  R extends AbstractCaseModelRepository<T>>
        extends AbstractCaseServiceImpl<T, ErrorComment,  R>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractErrorServiceImpl.class);
    public static final char INDEX_CHAR = '-';


    protected  CaseWorkflow workflow;
    private ErrorsApplication errorApplication;

    private UserAgentService userAgentService;
    private ErrorLogService errorLogService;

    private UserService userService;
    private CaseStatusService caseStatusService;
    private SpaceService spaceContextService;
    private PersistableModelService<ErrorsApplication, String> errorsApplicationService;
    protected ErrorCommentRepository errorCommentRepository;
    private CaseWorkflowService caseWorkflowService;

    @Autowired
    public void setErrorCommentRepository(ErrorCommentRepository errorCommentRepository) {
        this.errorCommentRepository = errorCommentRepository;
    }

    @Autowired
    public void setCaseWorkflowService(CaseWorkflowService caseWorkflowService) {
        this.caseWorkflowService = caseWorkflowService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCaseStatusService(CaseStatusService caseStatusService) {
        this.caseStatusService = caseStatusService;
    }

    @Autowired
    public void setSpaceService(SpaceService spaceContextService) {
        this.spaceContextService = spaceContextService;
    }

    @Autowired
    public void setErrorsApplicationService(PersistableModelService<ErrorsApplication, String> errorsApplicationService) {
        this.errorsApplicationService = errorsApplicationService;
    }


    @Autowired
    public void setErrorLogService(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    @Autowired
    public void setUUserAgentService(UserAgentService userAgentService) {
        this.userAgentService = userAgentService;
    }

    protected ErrorLogService getErrorLogService() {
        return errorLogService;
    }

    public ErrorsApplication getErrorApplication() {
        return errorApplication;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDataOverride(User systemUser){
        LOGGER.debug("initData");
        // initialize globals?
        this.workflow = this.getWorkflow();
        if(this.workflow == null){

            // create global BusinessContext
            Space syetemBusinessContext = this.spaceContextService.getSystemContext();

            // TODO: move to system error service
            // create errors application and workflow
            this.workflow = this.caseWorkflowService.create(
                    new CaseWorkflow(this.getWorkflowName(), "Workflow configuration for " + this.getDomainClass().getSimpleName() + "entries", syetemBusinessContext));

            // add error statuses
            List<CaseStatus> errorsWorkflowStatuses = new LinkedList<>();
            errorsWorkflowStatuses.add(caseStatusService.create(new CaseStatus(CaseStatus.UNASSIGNED, "Status for cases that have not yet been assigned", this.workflow)));
            errorsWorkflowStatuses.add(caseStatusService.create(new CaseStatus(CaseStatus.OPEN, "Status for pending cases", this.workflow)));
            errorsWorkflowStatuses.add(caseStatusService.create(new CaseStatus(CaseStatus.CLOSED, "Status for closed cases", this.workflow)));
            this.workflow.setStatuses(errorsWorkflowStatuses);

            this.errorApplication = this.errorsApplicationService.create(
                    new ErrorsApplication.Builder()
                            .space(syetemBusinessContext)
                            .owner(systemUser)
                            .name(this.getWorkflowName())
                            .title(this.getWorkflowTitle())
                            .description(this.getWorkflowDescription())
                            .workflow(this.workflow).build());
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public T create(T resource) {
        LOGGER.debug("create PersistableError: {}, userDetails: {}", resource, this.getPrincipal());

        // set app if missing
        if(resource.getApplication() == null){
            resource.setApplication(this.errorApplication);
        }
        // set status if missing
        if(resource.getStatus() == null){
            resource.setStatus(this.getStatusForNew(resource));
        }
        // merge the UserAgent based on it's hash/id
        if (resource.getUserAgent() != null) {
            resource.setUserAgent(this.userAgentService.findOrCreate(resource.getUserAgent()));
        }
        // merge the ErrorLog based on it's hash (i.e. ID)
        if (resource.getErrorLog() != null) {
            this.setDetailFromLogIfMissing(resource);
            ErrorLog log = this.errorLogService.findOrCreate(resource.getErrorLog());

            resource.setErrorLog(log);
            if (log.getFirstOccurred() == null) {
                log.setFirstOccurred(resource.getCreatedDate());
            }
            log.setLastOccurred(resource.getCreatedDate());
        }

        // save error
        resource = super.create(resource);

        return resource;
    }

    protected void setDetailFromLogIfMissing(T resource) {
        // if the description detail is empty, use the error log instead
        ErrorLog errorLog = resource.getErrorLog();
        if(StringUtils.isBlank(resource.getDetail())
                && errorLog != null
                && StringUtils.isNotBlank(errorLog.getRootCauseMessage())){
            String detail = new StringBuffer().append("Error logged automatically, root case: \"`").append(errorLog.getRootCauseMessage())
                    .append("`... \", trace: \n```\n").append(org.apache.commons.lang3.StringUtils.abbreviate(errorLog.getStacktrace(), BaseError.MAX_DETAIL_LENGTH-250))
                    .append("\n```\n see attached error log for more information").toString();
            LOGGER.debug("create, detail: {}", detail);
            resource.setDetail(detail);
        }
    }

}