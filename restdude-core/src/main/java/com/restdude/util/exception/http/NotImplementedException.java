package com.restdude.util.exception.http;


import org.springframework.http.HttpStatus;

/**
 * Signals the appropriate handler for the request is not implemented
 */
public class NotImplementedException extends HttpException {

    protected static final HttpStatus STATUS = HttpStatus.NOT_IMPLEMENTED;

    /**
     * Creates a new NotImplementedException with HTTP 501 status code and message.
     */
    public NotImplementedException() {
        super(STATUS);
    }

    /**
     * Creates a new NotImplementedException with the specified message and HTTP status 501.
     *
     * @param message the exception detail message
     */
    public NotImplementedException(final String message) {
        super(message, STATUS);
    }

    /**
     * Creates a new NotImplementedException with the specified cause and HTTP status 501.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public NotImplementedException(final Throwable cause) {
        super(STATUS.getReasonPhrase(), STATUS, cause);
    }

    /**
     * Creates a new NotImplementedException with the specified message, cause and HTTP status 501.
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public NotImplementedException(final String message, final Throwable cause) {
        super(message, STATUS, cause);
    }

}
