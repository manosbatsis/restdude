package com.restdude.util.exception.http;


import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Signals a failure in authentication process
 */
public class AuthenticationException extends HttpException {

    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    /**
     * Creates a new AuthenticationException with HTTP 401 status code and message.
     */
    protected AuthenticationException() {
        super(STATUS);
    }

    /**
     * Creates a new AuthenticationException with the specified message and HTTP status 401.
     *
     * @param message the exception detail message
     */
    protected AuthenticationException(final String message) {
        super(message, STATUS);
    }

    /**
     * Creates a new AuthenticationException with the specified cause and HTTP status 401.
     *
     * @param cause the {@code Throwable} that caused this exception, or {@code null}
     *              if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    protected AuthenticationException(final Throwable cause) {
        super(STATUS.getReasonPhrase(), STATUS, cause);
    }

    /**
     * Creates a new AuthenticationException with the specified message, cause and HTTP status 401.
     *
     * @param message the exception detail message
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    protected AuthenticationException(final String message, final Throwable cause) {
        super(message, STATUS, cause);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("WWW-Authenticate", "X-Calipso-Token header or calipso-sso token cookie");
        return headers;
    }
}
