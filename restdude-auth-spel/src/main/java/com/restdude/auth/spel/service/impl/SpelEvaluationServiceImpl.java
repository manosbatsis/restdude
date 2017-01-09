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
package com.restdude.auth.spel.service.impl;


import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.auth.spel.service.SpelEvaluationService;
import com.restdude.auth.userdetails.service.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("spelEvaluationService")
public class SpelEvaluationServiceImpl implements SpelEvaluationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpelEvaluationServiceImpl.class);


    private UserDetailsService userDetailsService;
    private MethodSecurityExpressionHandler expressionHandler;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired(required = false)
    public void setExpressionHandler(MethodSecurityExpressionHandler expressionHandler) {
        this.expressionHandler = expressionHandler;
    }

    @Override
    public Boolean check(String expression) {

        return SpelUtil.checkExpression(expression, expressionHandler, this.userDetailsService.getAuthentication());
    }

    @Override
    public Boolean check(String expression, Authentication auth) {

        return SpelUtil.checkExpression(expression, expressionHandler, auth);
    }

    @Override
    public Boolean check(String expression, EvaluationContext evaluationContext) {
        Boolean result;
        result = SpelUtil.checkExpression(expression, evaluationContext);
        return result;
    }


}