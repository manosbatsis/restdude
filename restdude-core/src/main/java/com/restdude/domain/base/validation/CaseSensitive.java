package com.restdude.domain.base.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface CaseSensitive {

    /**
     * Whether to apply case-sensitive search or check uniqueness of string values, default is <code>false</code>.
     */
    boolean value() default false;
}