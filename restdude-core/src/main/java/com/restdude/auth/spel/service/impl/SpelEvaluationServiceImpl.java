/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.spel.service.impl;


import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.auth.spel.service.SpelEvaluationService;
import com.restdude.auth.userdetails.service.UserDetailsService;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.SimpleMethodInvocation;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service("spelEvaluationService")
public class SpelEvaluationServiceImpl implements SpelEvaluationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpelEvaluationServiceImpl.class);

    private static final DefaultTarget DEFAULT_TARGET = new DefaultTarget();


    private static class DefaultTarget {
        public void defaultMethod() { /*NOP*/ }

        public Method getDefaultMethod() {
            try {
                return DefaultTarget.class.getMethod("defaultMethod");
            } catch (NoSuchMethodException e) {
                LOGGER.error("Faild to initialize", e);
                return null;
            }
        }
    }

    private UserDetailsService userDetailsService;
    private SpelExpressionParser parser = new SpelExpressionParser();
    private MethodSecurityExpressionHandler expressionHandler;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setExpressionHandler(MethodSecurityExpressionHandler expressionHandler) {
        this.expressionHandler = expressionHandler;


    }

    @Override
    public Boolean check(MethodInvocation invocation) {
        String expression = this.buildExpression(invocation);
        return check(expression);
    }

    private String buildExpression(MethodInvocation invocation) {
        Class targetClass = invocation.getThis().getClass();

        Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        if (method == null) method = invocation.getMethod();

        String expression = SpelUtil.getExpressionString(targetClass, method);
        return expression;
    }

    @Override
    public Boolean check(String securityExpression) {
        Boolean result = null;
        if (StringUtils.isNotBlank(securityExpression)) {

            MethodInvocation invocation = new SimpleMethodInvocation(DEFAULT_TARGET, DEFAULT_TARGET.getDefaultMethod());
            Authentication auth = this.userDetailsService.getAuthentication();
            EvaluationContext evaluationContext = expressionHandler.createEvaluationContext(auth, invocation);
            result = check(securityExpression, evaluationContext);

            LOGGER.debug("Check result: {}, principal: {}", result, auth.getPrincipal());

        }
        return result != null ? result : true;
    }

    @Override
    public Boolean check(MethodInvocation invocation, EvaluationContext securityEvaluationContext) {
        return check(buildExpression(invocation), securityEvaluationContext);
    }

    @Override
    public Boolean check(String securityExpression, EvaluationContext securityEvaluationContext) {
        Boolean result;
        result = ExpressionUtils.evaluateAsBoolean(parser.parseExpression(securityExpression), securityEvaluationContext);
        return result;
    }


}