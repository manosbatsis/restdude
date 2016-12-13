package com.restdude.auth.spel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Inherited
public @interface PreAuthorizeDeleteById {
    String controller() default "denyAll";

    String service() default "permitAll";
}