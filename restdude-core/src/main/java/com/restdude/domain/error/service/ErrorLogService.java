package com.restdude.domain.error.service;

import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.error.model.ErrorLog;


public interface ErrorLogService extends ModelService<ErrorLog, String> {
    public static final String BEAN_ID = "errorLogService";
}
