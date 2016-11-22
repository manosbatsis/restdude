package com.restdude.domain.base.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {UniqueValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
public @interface Unique {
    String message() default "{org.hibernate.validator.constraints.Unique.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}