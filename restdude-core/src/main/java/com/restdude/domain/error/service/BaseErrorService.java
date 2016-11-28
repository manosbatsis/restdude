package com.restdude.domain.error.service;

import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.error.model.BaseError;


public interface BaseErrorService extends ModelService<BaseError, String> {
    public static final String BEAN_ID = "baseErrorService";
}
