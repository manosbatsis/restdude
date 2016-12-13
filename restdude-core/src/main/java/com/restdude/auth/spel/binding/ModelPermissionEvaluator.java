package com.restdude.auth.spel.binding;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

// TODO
public class ModelPermissionEvaluator implements PermissionEvaluator {


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {
            // TODO
        }
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        throw new RuntimeException("Id and Class permissions are not supperted by this application");
    }
}