package com.restdude.domain.error.model;

import com.restdude.domain.base.model.AbstractAssignedidPersistable;
import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "error_trace")
@Inheritance(strategy = InheritanceType.JOINED)
@ApiModel(value = "StackTrace", description = "Used to persist stacktraces of application or client errors. The hash of the stacktrace is used as the ID to avoid redundancy")
public class StackTrace extends AbstractAssignedidPersistable<String> {

    private static final long serialVersionUID = 3558291745762331656L;

    public static final String PRE_AUTHORIZE_SEARCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_CREATE = "hasRole('ROLE_USER')";
    public static final String PRE_AUTHORIZE_UPDATE = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_PATCH = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_VIEW = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_DELETE = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";

    public static final String PRE_AUTHORIZE_DELETE_BY_ID = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_DELETE_ALL = "denyAll";
    public static final String PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
    public static final String PRE_AUTHORIZE_FIND_BY_IDS = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_FIND_ALL = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";
    public static final String PRE_AUTHORIZE_COUNT = "hasAnyRole('ROLE_ADMIN', 'ROLE_SITE_OPERATOR')";

    public StackTrace() {
    }


}
