package com.restdude.domain.error.service;

import com.restdude.domain.base.service.AbstractAssignedIdModelService;
import com.restdude.domain.error.model.ErrorLog;


public interface ErrorLogService extends AbstractAssignedIdModelService<ErrorLog, String> {
    public static final String BEAN_ID = "errorLogService";
}
