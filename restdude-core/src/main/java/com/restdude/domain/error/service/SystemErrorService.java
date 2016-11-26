package com.restdude.domain.error.service;

import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.error.model.SystemError;


public interface SystemErrorService extends ModelService<SystemError, String> {
    public static final String BEAN_ID = "systemErrorService";
}
