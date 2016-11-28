package com.restdude.domain.error.service;

import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.error.model.UserAgent;


public interface UserAgentService extends ModelService<UserAgent, String> {
    public static final String BEAN_ID = "userAgentService";
}
