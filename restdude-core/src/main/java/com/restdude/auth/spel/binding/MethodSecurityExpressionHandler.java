package com.restdude.auth.spel.binding;


import com.restdude.auth.spel.service.SpelEvaluationService;
import com.restdude.auth.userdetails.util.SecurityUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

public class MethodSecurityExpressionHandler extends
        DefaultMethodSecurityExpressionHandler implements
        org.springframework.security.access.expression.method.MethodSecurityExpressionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodSecurityExpressionHandler.class);

    private SpelEvaluationService spelEvaluationService;

    public MethodSecurityExpressionHandler() {
    }


    @Autowired
    public void setMmthodSecurityEvaluationService(SpelEvaluationService mmthodSecurityEvaluationService) {
        this.spelEvaluationService = mmthodSecurityEvaluationService;
    }

    /**
     * Uses a {@link org.springframework.security.access.expression.method.MethodSecurityEvaluationContext} as the <tt>EvaluationContext</tt>
     * implementation.
     */
    public StandardEvaluationContext createEvaluationContextInternal(Authentication auth, MethodInvocation invocation) {
        LOGGER.debug("createEvaluationContextInternal, auth: {}, invocation: {}", auth, invocation);
        StandardEvaluationContext securityEvaluationContext = super.createEvaluationContextInternal(SecurityUtil.getAuthentication(), invocation);
        Boolean spelResult = this.spelEvaluationService.check(invocation);
        return securityEvaluationContext;
    }


}

