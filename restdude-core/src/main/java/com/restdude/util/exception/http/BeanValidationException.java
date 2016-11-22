package com.restdude.util.exception.http;


import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Signals a failure in authentication process
 */
public class BeanValidationException extends BadRequestException {

    private Set<ConstraintViolation> errors;


    /**
     * Creates a new instance with HTTP 400 status code and message.
     */
    public BeanValidationException(Set<ConstraintViolation> errors) {
        super();
        this.errors = errors;
    }

    /**
     * Creates a new instance with the specified message and HTTP status 400.
     *
     * @param message the exception detail message
     */
    public BeanValidationException(final String message, Set<ConstraintViolation> errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * Creates a new instance with the specified cause and HTTP status 400.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BeanValidationException(Set<ConstraintViolation> errors, final Throwable cause) {
        super(cause);
        this.errors = errors;
    }

    /**
     * Creates a new instance with the specified message, cause and HTTP status 400.
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BeanValidationException(final String message, Set<ConstraintViolation> errors, final Throwable cause) {
        super(message, cause);
        this.errors = errors;
    }

    public Set<ConstraintViolation> getErrors() {
        return errors;
    }

    public void setErrors(Set<ConstraintViolation> errors) {
        this.errors = errors;
    }
}
