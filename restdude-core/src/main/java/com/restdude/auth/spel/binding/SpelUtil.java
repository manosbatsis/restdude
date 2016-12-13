package com.restdude.auth.spel.binding;

import com.restdude.auth.spel.annotations.*;
import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.base.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;

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

    static {

        METHOD_SPEL_ANNOTATIONS.put("count", PreAuthorizeCount.class);
        METHOD_SPEL_ANNOTATIONS.put("create", PreAuthorizeCreate.class);
        //METHOD_SPEL_ANNOTATIONS.put("findOrCreate", PreAuthorizeFindCreate.class);


        METHOD_SPEL_ANNOTATIONS.put("delete", PreAuthorizeDelete.class);
        METHOD_SPEL_ANNOTATIONS.put("deleteAll", PreAuthorizeDeleteAll.class);
        METHOD_SPEL_ANNOTATIONS.put("deleteAllWithCascade", PreAuthorizeDeleteWithCascade.class);


        METHOD_SPEL_ANNOTATIONS.put("findAll", PreAuthorizeFindAll.class);
        METHOD_SPEL_ANNOTATIONS.put("findById", PreAuthorizeFindById.class);
        METHOD_SPEL_ANNOTATIONS.put("findByIds", PreAuthorizeFindByIds.class);

        METHOD_SPEL_ANNOTATIONS.put("findPaginated", PreAuthorizeFindPaginated.class);


        METHOD_SPEL_ANNOTATIONS.put("addMetadatum", PreAuthorizePatch.class);
        METHOD_SPEL_ANNOTATIONS.put("patch", PreAuthorizePatch.class);

        METHOD_SPEL_ANNOTATIONS.put("removeMetadatum", PreAuthorizeUpdate.class);
        METHOD_SPEL_ANNOTATIONS.put("update", PreAuthorizeUpdate.class);
        METHOD_SPEL_ANNOTATIONS.put("updateFiles", PreAuthorizeUpdate.class);


    }

    public static boolean isCandidate(Class targetClass, Method method) {
        return METHOD_SPEL_ANNOTATIONS.containsKey(method.getName())
                && (ModelService.class.isAssignableFrom(targetClass)
                || AbstractModelController.class.isAssignableFrom(targetClass));
    }

    public static Class getComponentModelInterfaceClass(Class targetClass) {
        Class interfaze = null;
        if (ModelService.class.isAssignableFrom(targetClass)) {
            interfaze = ModelService.class;
        } else if (AbstractModelController.class.isAssignableFrom(targetClass)) {
            interfaze = AbstractModelController.class;
        }
        return interfaze;
    }

    /**
     * Used by ModelService and AbstractModelController components.
     *
     * @return
     */
    public static String getExpressionString(Class targetClass, Method method) {

        String methodPreAuthorizeExpression = null;


        if (METHOD_SPEL_ANNOTATIONS.containsKey(method.getName())) {
            Class modelInterfaceClass = getComponentModelInterfaceClass(targetClass);


            if (modelInterfaceClass != null) {
                boolean isService = ModelService.class.isAssignableFrom(modelInterfaceClass);
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
        LOGGER.debug("g\netExpressionForComponent: \ntargetClass: {},\nmethod: {}, \nannotation: {}, \nexpression: {}, \ndomainClass: {}\n",
                targetClass,
                method,
                annotationClass,
                expression,
                domainClass);
        return expression;
    }


    public static final String getHasRole(String role) {
        return new StringBuffer(" hasRole('").append(role).append(") ").toString();
    }

    public static final String DENY_ALL = "permitAll";
    public static final String PERMIT_ALL = "permitAll";

    public static final String HAS_ROLE_ADMIN_OR_OPERATOR = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";


}
