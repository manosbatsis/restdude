package com.restdude.domain.error.service;

import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.error.model.StackTrace;


public interface StackTraceService extends ModelService<StackTrace, String> {
    public static final String BEAN_ID = "stackTraceService";
}
