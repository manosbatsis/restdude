/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.spel.binding;

import com.restdude.auth.spel.annotations.*;
import com.restdude.mdd.controller.AbstractPersistableModelController;
import com.restdude.mdd.service.PersistableModelService;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.SimpleMethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by manos on 12/12/2016.
 */
public class SpelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpelUtil.class);
    private static final ConcurrentHashMap<String, Optional<String>> SPEL_EXPRESSION_CACHE = new ConcurrentHashMap<String, Optional<String>>();
    private static final HashMap<String, Class> METHOD_SPEL_ANNOTATIONS = new HashMap<String, Class>();
    private static final SpelExpressionParser parser = new SpelExpressionParser();

    static {

        METHOD_SPEL_ANNOTATIONS.put("count", PreAuthorizeCount.class);
        METHOD_SPEL_ANNOTATIONS.put("plainJsonPost", PreAuthorizeCreate.class);
        //METHOD_SPEL_ANNOTATIONS.put("findOrCreate", PreAuthorizeFindCreate.class);


        METHOD_SPEL_ANNOTATIONS.put("delete", PreAuthorizeDelete.class);
        METHOD_SPEL_ANNOTATIONS.put("deleteAll", PreAuthorizeDeleteAll.class);
        METHOD_SPEL_ANNOTATIONS.put("deleteAllWithCascade", PreAuthorizeDeleteWithCascade.class);


        METHOD_SPEL_ANNOTATIONS.put("plainJsonGetAll", PreAuthorizeFindAll.class);
        METHOD_SPEL_ANNOTATIONS.put("plainJsonGetById", PreAuthorizeFindById.class);
        METHOD_SPEL_ANNOTATIONS.put("plainJsonGetByIds", PreAuthorizeFindByIds.class);

        METHOD_SPEL_ANNOTATIONS.put("plainJsonGetPage", PreAuthorizeFindPaginated.class);


        METHOD_SPEL_ANNOTATIONS.put("addMetadatum", PreAuthorizePatch.class);
        METHOD_SPEL_ANNOTATIONS.put("plainJsonPatch", PreAuthorizePatch.class);

        METHOD_SPEL_ANNOTATIONS.put("removeMetadatum", PreAuthorizeUpdate.class);
        METHOD_SPEL_ANNOTATIONS.put("plainJsonPut", PreAuthorizeUpdate.class);
        METHOD_SPEL_ANNOTATIONS.put("updateFiles", PreAuthorizeUpdate.class);


    }

    public static boolean isCandidate(Class targetClass, Method method) {
        return METHOD_SPEL_ANNOTATIONS.containsKey(method.getName())
                && (PersistableModelService.class.isAssignableFrom(targetClass)
                || AbstractPersistableModelController.class.isAssignableFrom(targetClass));
    }

    public static Class getComponentModelInterfaceClass(Class targetClass) {
        Class interfaze = null;
        if (PersistableModelService.class.isAssignableFrom(targetClass)) {
            interfaze = PersistableModelService.class;
        } else if (AbstractPersistableModelController.class.isAssignableFrom(targetClass)) {
            interfaze = AbstractPersistableModelController.class;
        }
        return interfaze;
    }

    /**
     * Used by ModelService and AbstractPersistableModelController components.
     *
     * @return
     */
    public static String getExpressionString(Class targetClass, Method method) {

        String methodPreAuthorizeExpression = null;


        if (METHOD_SPEL_ANNOTATIONS.containsKey(method.getName())) {
            Class modelInterfaceClass = getComponentModelInterfaceClass(targetClass);


            if (modelInterfaceClass != null) {
                boolean isService = PersistableModelService.class.isAssignableFrom(modelInterfaceClass);
                String methodKey = new StringBuffer(targetClass.getCanonicalName()).append('#').append(method.getName()).toString();


                if (!SPEL_EXPRESSION_CACHE.containsKey(methodKey)
                        && METHOD_SPEL_ANNOTATIONS.containsKey(method.getName())) {
                    // read annotation for component
                    methodPreAuthorizeExpression = getExpressionForComponent(targetClass, method, modelInterfaceClass);
                    SPEL_EXPRESSION_CACHE.put(methodKey, Optional.ofNullable(methodPreAuthorizeExpression));
                    LOGGER.debug("Added method key: {}, expression: {}, for model: {}", methodKey, methodPreAuthorizeExpression, methodKey);

                } else {
                    methodPreAuthorizeExpression = SPEL_EXPRESSION_CACHE.get(methodKey).orElse(null);
                }

            }
        }

        return methodPreAuthorizeExpression;
    }

    private static String getExpressionForComponent(Class targetClass, Method method, Class targetClassInterface) {

        Class<?> domainClass = GenericTypeResolver.resolveTypeArguments(targetClass, targetClassInterface)[0];
        Class annotationClass = METHOD_SPEL_ANNOTATIONS.get(method.getName());
        Optional<Annotation> preAuthAnnotation = Optional.ofNullable(AnnotationUtils.findAnnotation(domainClass, annotationClass));

        String expression = null;
        if (preAuthAnnotation.isPresent()) {
            boolean isService = !targetClass.getName().endsWith("Controller");
            expression = AnnotationUtils.getValue(preAuthAnnotation.get(), isService ? "service" : "controller").toString();
        }
        LOGGER.debug("getExpressionForComponent: \ntargetClass: {},\nmethod: {}, \nannotation: {}, \nexpression: {}, \ndomainClass: {}\n",
                targetClass,
                method,
                annotationClass,
                expression,
                domainClass);
        return expression;
    }


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

    public static Boolean checkModelsExpressionForMethod(MethodInvocation invocation, MethodSecurityExpressionHandler expressionHandler, Authentication auth) {
        String expression = SpelUtil.buildModelBasedExpressionForMethod(invocation);
        return checkExpression(expression, expressionHandler, auth);
    }

    public static Boolean checkExpression(String expression, MethodSecurityExpressionHandler expressionHandler, Authentication auth) {
        Boolean result = null;
        if (StringUtils.isNotBlank(expression)) {

            MethodInvocation invocation = new SimpleMethodInvocation(DEFAULT_TARGET, DEFAULT_TARGET.getDefaultMethod());
            EvaluationContext evaluationContext = expressionHandler.createEvaluationContext(auth, invocation);
            result = checkExpression(expression, evaluationContext);

            LOGGER.debug("Check result: {}, principal: {}", result, auth.getPrincipal());

        }
        return result != null ? result : true;
    }

    public static Boolean checkExpression(String securityExpression, EvaluationContext securityEvaluationContext) {
        Boolean result;
        result = ExpressionUtils.evaluateAsBoolean(parser.parseExpression(securityExpression), securityEvaluationContext);
        return result;
    }

    public static String buildModelBasedExpressionForMethod(MethodInvocation invocation) {
        Class targetClass = invocation.getThis().getClass();

        Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        if (method == null) method = invocation.getMethod();

        String expression = SpelUtil.getExpressionString(targetClass, method);
        return expression;
    }

    public static final String getHasRole(String role) {
        return new StringBuffer(" hasRole('").append(role).append(") ").toString();
    }

    public static final String DENY_ALL = "permitAll";
    public static final String PERMIT_ALL = "permitAll";

    public static final String HAS_ROLE_ADMIN_OR_OPERATOR = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String HAS_ROLE_USER = "hasRole('ROLE_USER')";


}
