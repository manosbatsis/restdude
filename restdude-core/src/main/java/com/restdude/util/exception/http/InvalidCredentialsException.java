package com.restdude.util.exception.http;

/**
 * Signals that authentication credentials required to respond to a authentication
 * challenge are invalid
 */
public class InvalidCredentialsException extends AuthenticationException {


    public static final String MESSAGE = "Invalid credentials";

    /**
     * Creates a new InvalidCredentialsException with default message and HTTP status 401.
     */
    public InvalidCredentialsException() {
        super(MESSAGE);
    }

    /**
     * Creates a new InvalidCredentialsException with the specified message and HTTP status 401
     *
     * @param message the exception detail message
     */
    public InvalidCredentialsException(final String message) {
        super(message);
    }

    /**
     * Creates a new InvalidCredentialsException with the specified cause and HTTP status 401.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public InvalidCredentialsException(Throwable cause) {
        super(MESSAGE, cause);
    }

    /**
     * Creates a new InvalidCredentialsException with the specified detail message, cause and HTTP status 401
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public InvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
