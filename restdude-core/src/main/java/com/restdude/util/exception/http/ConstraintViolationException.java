package com.restdude.util.exception.http;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Interface for Exceptions that carry {@link ConstraintViolation} errors
 */
public interface ConstraintViolationException {
    public Set<ConstraintViolation> getConstraintViolations();
}
