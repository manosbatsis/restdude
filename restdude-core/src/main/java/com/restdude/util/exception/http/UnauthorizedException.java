package com.restdude.util.exception.http;

/**
 * Signals an unauthorized attempt
 */
public class UnauthorizedException extends AuthenticationException {


    public static final String MESSAGE = "Invalid credentials";

    /**
     * Creates a new instance with default message and HTTP status 401.
     */
    public UnauthorizedException() {
        super(MESSAGE);
    }

    /**
     * Creates a new instance with the specified message and HTTP status 401
     *
     * @param message the exception detail message
     */
    public UnauthorizedException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance with the specified cause and HTTP status 401.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public UnauthorizedException(Throwable cause) {
        super(MESSAGE, cause);
    }

    /**
     * Creates a new instance with the specified detail message, cause and HTTP status 401
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
