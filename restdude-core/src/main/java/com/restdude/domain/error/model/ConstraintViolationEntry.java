package com.restdude.domain.error.model;

import io.swagger.annotations.ApiModel;

import javax.validation.ConstraintViolation;
import java.io.Serializable;

/**
 * DTO class for {@link ConstraintViolation} instances
 */
@ApiModel(value = "ConstraintViolationEntry", description = "DTO class for serialization of bean validation violations. ")
public class ConstraintViolationEntry implements Serializable {

    private String message;
    private String propertyPath;

    public ConstraintViolationEntry() {

    }

    public ConstraintViolationEntry(String message, String propertyPath) {
        this.message = message;
        this.propertyPath = propertyPath;
    }

    public ConstraintViolationEntry(ConstraintViolation constraintViolation) {
        if (constraintViolation != null) {
            this.message = constraintViolation.getMessage();
            this.propertyPath = constraintViolation.getPropertyPath().toString();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

}
