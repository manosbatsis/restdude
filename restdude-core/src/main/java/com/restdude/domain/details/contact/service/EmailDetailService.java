package com.restdude.domain.details.contact.service;


import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.details.contact.model.EmailDetail;
import org.springframework.security.access.method.P;

public interface EmailDetailService extends ModelService<EmailDetail, String> {

    public static final String BEAN_ID = "emailDetailService";

    EmailDetail forceVerify(@P("resource") EmailDetail resource);
}
