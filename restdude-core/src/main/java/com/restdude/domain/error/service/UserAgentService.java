package com.restdude.domain.error.service;

import com.restdude.domain.base.service.AbstractAssignedIdModelService;
import com.restdude.domain.error.model.UserAgent;


public interface UserAgentService extends AbstractAssignedIdModelService<UserAgent, String> {
    public static final String BEAN_ID = "userAgentService";
}
