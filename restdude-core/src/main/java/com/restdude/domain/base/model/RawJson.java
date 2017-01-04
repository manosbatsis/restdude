package com.restdude.domain.base.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * DTO fpr raw json strings
 */
public class RawJson implements Serializable {

    private final String value;

    public RawJson(String value) {
        this.value = value;
    }

    @JsonValue
    @JsonRawValue
    public String value() {
        return value;
    }
}
