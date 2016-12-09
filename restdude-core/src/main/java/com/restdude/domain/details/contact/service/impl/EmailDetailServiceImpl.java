package com.restdude.domain.details.contact.service.impl;

import com.restdude.domain.confirmationtoken.model.ConfirmationToken;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.details.contact.repository.EmailDetailRepository;
import com.restdude.domain.details.contact.service.EmailDetailService;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named(EmailDetailService.BEAN_ID)
public class EmailDetailServiceImpl extends AbstractContactDetailServiceImpl<EmailDetail, String, EmailDetailRepository> implements EmailDetailService {


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public EmailDetail forceVerify(@P("resource") EmailDetail resource) {
        if (!resource.getVerified()) {
            ConfirmationToken token = this.confirmationTokenReposirory.findByTargetId(resource.getId());
            if (token != null) {
                this.confirmationTokenReposirory.delete(token);
            }
            resource.setVerified(true);
            resource = this.repository.save(resource);
        }
        return resource;
    }
}
