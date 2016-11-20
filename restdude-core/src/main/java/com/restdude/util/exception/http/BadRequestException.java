package com.restdude.util.exception.http;


import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Signals a failure in authentication process
 */
public class BadRequestException extends HttpException {

    protected static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;


    private List<String> errors;

    /**
     * Creates a new instance with HTTP 400 status code and message.
     */
    protected BadRequestException() {
        super(STATUS);
    }

    /**
     * Creates a new instance with HTTP 400 status code and message.
     */
    public BadRequestException(List<String> errors) {
        super(STATUS);
        this.errors = errors;
    }

    /**
     * Creates a new instance with the specified message and HTTP status 400.
     *
     * @param message the exception detail message
     */
    public BadRequestException(final String message) {
        super(message, STATUS);
    }

    /**
     * Creates a new instance with the specified message and HTTP status 400.
     *
     * @param message the exception detail message
     */
    public BadRequestException(final String message, List<String> errors) {
        super(message, STATUS);
        this.errors = errors;
    }

    /**
     * Creates a new instance with the specified cause and HTTP status 400.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BadRequestException(final Throwable cause) {
        super(STATUS.getReasonPhrase(), STATUS, cause);
    }

    /**
     * Creates a new instance with the specified cause and HTTP status 400.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BadRequestException(List<String> errors, final Throwable cause) {
        super(STATUS.getReasonPhrase(), STATUS, cause);
        this.errors = errors;
    }

    /**
     * Creates a new instance with the specified message, cause and HTTP status 400.
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BadRequestException(final String message, final Throwable cause) {
        super(message, STATUS, cause);
    }

    /**
     * Creates a new instance with the specified message, cause and HTTP status 400.
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public BadRequestException(final String message, List<String> errors, final Throwable cause) {
        super(message, STATUS, cause);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
