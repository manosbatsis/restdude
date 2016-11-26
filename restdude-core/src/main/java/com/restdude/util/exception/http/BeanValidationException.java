package com.restdude.util.exception.http;


import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Signals a validation failure
 */
public class BeanValidationException extends BadRequestException implements ConstraintViolationException {

    private Set<ConstraintViolation> constraintViolations;


    /**
     * Creates a new instance with HTTP 400 status code and message.
     * @param constraintViolations bean validation errors, if any
     */
    public BeanValidationException(Set<ConstraintViolation> constraintViolations) {
        super();
        this.constraintViolations = constraintViolations;
    }

    /**
     * Creates a new instance with the specified message and HTTP status 400.
     *
     * @param message the exception detail message
     * @param constraintViolations bean validation errors, if any
     */
    public BeanValidationException(final String message, Set<ConstraintViolation> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }

    /**
     * Creates a new instance with the specified cause and HTTP status 400.
     *
     * @param constraintViolations bean validation errors, if any
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BeanValidationException(Set<ConstraintViolation> constraintViolations, final Throwable cause) {
        super(cause);
        this.constraintViolations = constraintViolations;
    }

    /**
     * Creates a new instance with the specified message, cause and HTTP status 400.
     *
     * @param message the exception detail message
     * @param constraintViolations bean validation errors, if any
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BeanValidationException(final String message, Set<ConstraintViolation> constraintViolations, final Throwable cause) {
        super(message, cause);
        this.constraintViolations = constraintViolations;
    }

    @Override
    public Set<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    public void setConstraintViolations(Set<ConstraintViolation> constraintViolations) {
        this.constraintViolations = constraintViolations;
    }
}
