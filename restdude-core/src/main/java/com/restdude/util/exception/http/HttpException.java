package com.restdude.util.exception.http;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Signals that an HTTP aware error has occurred.
 */
public abstract class HttpException extends RuntimeException {

    protected String message;
    protected HttpStatus status;

    /**
     * Creates a new HttpException with a {@code null} message.
     */
    protected HttpException() {
        super();
    }

    /**
     * Creates a new HttpException with the specified message.
     *
     * @param message the exception message
     */
    protected HttpException(final String message) {
        super(message);
        this.message = message;
    }

    /**
     * Creates a new HttpException with the specified status.
     *
     * @param status the HTTP status
     */
    public HttpException(final HttpStatus status) {
        this(status.getReasonPhrase());
        this.status = status;
    }

    /**
     * Creates a new HttpException with the specified status and cause.
     *
     * @param status the HTTP status
     * @param cause  the {@code Throwable} that caused this exception, or {@code null}
     */
    public HttpException(final HttpStatus status, final Throwable cause) {
        this(status.getReasonPhrase(), status, cause);
    }

    /**
     * Creates a new HttpException with the specified message and status.
     *
     * @param message the exception message
     * @param status  the HTTP status
     */
    public HttpException(final String message, final HttpStatus status) {
        this(message);
        this.status = status;
    }

    /**
     * Creates a new HttpException with the specified message, status and cause.
     *
     * @param message the exception detail message
     * @param status  the HTTP status
     * @param cause   the {@code Throwable} that caused this exception, or {@code null}
     *                if the cause is unavailable, unknown, or not a {@code Throwable}
     */
    public HttpException(final String message, final HttpStatus status, final Throwable cause) {
        this(message, status);
        initCause(cause);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Map<String, String> getResponseHeaders() {
        return null;
    }
}
