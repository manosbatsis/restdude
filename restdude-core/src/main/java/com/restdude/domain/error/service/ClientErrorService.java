package com.restdude.domain.error.service;

import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.error.model.ClientError;


public interface ClientErrorService extends ModelService<ClientError, String> {
    public static final String BEAN_ID = "clientErrorService";
}
