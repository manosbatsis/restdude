/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-auth-spel, https://manosbatsis.github.io/restdude/restdude-auth-spel
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
package com.restdude.auth.spel.binding;

import com.restdude.auth.userdetails.util.SecurityUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

public class ModelDrivenMethodSecurityExpressionHandler extends
        DefaultMethodSecurityExpressionHandler implements
        org.springframework.security.access.expression.method.MethodSecurityExpressionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelDrivenMethodSecurityExpressionHandler.class);

    public ModelDrivenMethodSecurityExpressionHandler() {
    }

    /**
     * Uses a {@link org.springframework.security.access.expression.method.MethodSecurityEvaluationContext} as the <tt>EvaluationContext</tt>
     * implementation.
     */
    public StandardEvaluationContext createEvaluationContextInternal(Authentication auth, MethodInvocation invocation) {
        LOGGER.debug("createEvaluationContextInternal, auth: {}, invocation: {}", auth, invocation);
        StandardEvaluationContext securityEvaluationContext = super.createEvaluationContextInternal(SecurityUtil.getAuthentication(), invocation);
        check(auth, invocation);
        return securityEvaluationContext;
    }

    protected boolean check(Authentication auth, MethodInvocation invocation) {
        return SpelUtil.checkModelsExpressionForMethod(invocation, this, auth);
    }


}

