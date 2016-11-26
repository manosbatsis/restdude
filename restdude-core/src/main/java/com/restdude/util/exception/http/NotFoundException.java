package com.restdude.util.exception.http;


import org.springframework.http.HttpStatus;

/**
 * Signals the requested resource was not found
 */
public class NotFoundException extends SystemException {

    protected static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    /**
     * Creates a new NotFoundException with HTTP 404 status code and message.
     */
    public NotFoundException() {
        super(STATUS);
    }

    /**
     * Creates a new NotFoundException with the specified message and HTTP status 404.
     *
     * @param message the exception detail message
     */
    public NotFoundException(final String message) {
        super(message, STATUS);
    }

    /**
     * Creates a new NotFoundException with the specified cause and HTTP status 404.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public NotFoundException(final Throwable cause) {
        super(STATUS.getReasonPhrase(), STATUS, cause);
    }

    /**
     * Creates a new NotFoundException with the specified message, cause and HTTP status 404.
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public NotFoundException(final String message, final Throwable cause) {
        super(message, STATUS, cause);
    }

}
