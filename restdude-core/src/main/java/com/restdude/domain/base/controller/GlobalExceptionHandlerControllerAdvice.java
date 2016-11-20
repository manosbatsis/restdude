package com.restdude.domain.base.controller;


import com.restdude.domain.error.model.ErrorInfo;
import com.restdude.util.exception.http.BadRequestException;
import com.restdude.util.exception.http.HttpException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Global RESTful-friendly exception handling that writes aa {@Link ErrorInfo} object to the response
 */
@EnableWebMvc
@ControllerAdvice
public class GlobalExceptionHandlerControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerControllerAdvice.class);
    private static Map<Class, Integer> exceptionStatuses = new HashMap<Class, Integer>();

    static {
        exceptionStatuses.put(NoSuchRequestHandlingMethodException.class, HttpServletResponse.SC_NOT_FOUND);
        exceptionStatuses.put(HttpRequestMethodNotSupportedException.class, HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        exceptionStatuses.put(HttpMediaTypeNotSupportedException.class, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        exceptionStatuses.put(HttpMediaTypeNotAcceptableException.class, HttpServletResponse.SC_NOT_ACCEPTABLE);
        exceptionStatuses.put(MissingPathVariableException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(MissingServletRequestParameterException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(ServletRequestBindingException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(ConversionNotSupportedException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(TypeMismatchException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(HttpMessageNotReadableException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(HttpMessageNotWritableException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(MethodArgumentNotValidException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(MissingServletRequestPartException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(BindException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(NoHandlerFoundException.class, HttpServletResponse.SC_NOT_FOUND);
        exceptionStatuses.put(AsyncRequestTimeoutException.class, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        exceptionStatuses.put(Exception.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleBadRequestException(HttpServletRequest request, Exception e) {
        LOGGER.warn("handleBadRequestException", e);
        BadRequestException ex = (BadRequestException) e;
        ErrorInfo.Builder builder = new ErrorInfo.Builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(ex.getErrors())
                .throwable(ex);
        return builder.build();
    }

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ErrorInfo handleHttpException(HttpServletRequest request, HttpServletResponse response, HttpException ex) {
        LOGGER.warn("handleHttpException", ex);
        HttpStatus status = ex.getStatus();
        ErrorInfo.Builder builder = buildErrorInfo(response, ex, status);

        Map<String, String> headers = ex.getResponseHeaders();
        if (MapUtils.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                response.addHeader(key, headers.get(key));
            }
        }

        return builder.build();
    }


    @ExceptionHandler({NoSuchRequestHandlingMethodException.class, HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class, MissingPathVariableException.class, MissingServletRequestParameterException.class,
            ServletRequestBindingException.class, ConversionNotSupportedException.class, TypeMismatchException.class,
            HttpMessageNotReadableException.class, HttpMessageNotWritableException.class, MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class, BindException.class, NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class, Exception.class})
    @ResponseBody
    public ErrorInfo handleStandardException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        LOGGER.warn("handleStandardException", ex);
        HttpStatus status = getHttpStatus(ex);

        ErrorInfo.Builder builder = buildErrorInfo(response, ex, status);

        return builder.build();
    }


    private ErrorInfo.Builder buildErrorInfo(HttpServletResponse response, Exception ex, HttpStatus status) {
        // build error info
        ErrorInfo.Builder builder = new ErrorInfo.Builder()
                .message(ex.getMessage())
                .code(status.value())
                .status(status.getReasonPhrase());

        // update response
        response.setStatus(status.value());
        return builder;
    }

    private HttpStatus getHttpStatus(Exception ex) {
        Class exceptionClass = ex.getClass();
        Integer statusCode = null;
        while (statusCode == null) {
            statusCode = exceptionStatuses.get(exceptionClass);
            if (statusCode == null) {
                exceptionClass = exceptionClass.getSuperclass();
            }
        }
        return HttpStatus.valueOf(statusCode.intValue());
    }
}