package com.restdude.auth.spel.service;


import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;

/**
 * Created by manos on 13/12/2016.
 */
public interface SpelEvaluationService {


    Boolean check(MethodInvocation mi);

    Boolean check(String securityExpression);

    Boolean check(MethodInvocation mi, EvaluationContext securityEvaluationContext);

    Boolean check(String securityExpression, EvaluationContext securityEvaluationContext);
}
