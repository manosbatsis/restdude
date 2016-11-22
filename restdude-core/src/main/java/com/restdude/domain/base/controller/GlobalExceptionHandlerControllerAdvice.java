package com.restdude.domain.base.controller;


import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.util.exception.http.BeanValidationException;
import com.restdude.util.exception.http.HttpException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Global RESTful-friendly exception handling that writes a {@Link SystemError} object to the response.
 * May automatically persist SystemError objects according to calipso.validationErrors.system.persist* configuration properties.
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

    private SystemErrorService systemErrorService;

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    @ExceptionHandler({NestedServletException.class, ServletException.class})
    @ResponseBody
    public SystemError handleNestedServletException(HttpServletRequest request, HttpServletResponse response, ServletException ex) {
        Throwable nestedException = ex;
        while (NestedServletException.class.isAssignableFrom(nestedException.getClass()) || nestedException.getClass().equals(ServletException.class)) {
            nestedException = nestedException.getCause();
        }

        if (BeanValidationException.class.isAssignableFrom(nestedException.getClass())) {
            return this.handleBeanValidationException(request, response, (BeanValidationException) nestedException);
        } else if (HttpException.class.isAssignableFrom(nestedException.getClass())) {
            return this.handleHttpException(request, response, (HttpException) nestedException);
        } else {
            return this.handleStandardException(request, response, (HttpException) nestedException);
        }

    }

    @ExceptionHandler(BeanValidationException.class)
    @ResponseBody
    public SystemError handleBeanValidationException(HttpServletRequest request, HttpServletResponse response, BeanValidationException ex) {

        // get response status
        HttpStatus status = ex.getStatus();

        // create error
        SystemError error = this.createSystemError(request, response, status.value(), ex);

        error.setValidationErrors(ex.getErrors());

        return error;
    }

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public SystemError handleHttpException(HttpServletRequest request, HttpServletResponse response, HttpException ex) {

        // get response status
        HttpStatus status = ex.getStatus();

        // create error
        SystemError error = this.createSystemError(request, response, status.value(), ex);

        return error;
    }


    @ExceptionHandler({NoSuchRequestHandlingMethodException.class, HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class, MissingPathVariableException.class, MissingServletRequestParameterException.class,
            ServletRequestBindingException.class, ConversionNotSupportedException.class, TypeMismatchException.class,
            HttpMessageNotReadableException.class, HttpMessageNotWritableException.class, MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class, BindException.class, NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class, Exception.class})
    @ResponseBody
    public SystemError handleStandardException(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        // get response status
        HttpStatus status = getStandardExceptionHttpStatus(ex);

        // create error
        SystemError error = this.createSystemError(request, response, status.value(), ex);

        return error;
    }

    private SystemError createSystemError(HttpServletRequest request, HttpServletResponse response, Integer status, Exception ex) {

        // create error instance
        SystemError error = new SystemError(request, status, ex.getMessage(), ex);

        // update response status
        response.setStatus(status);

        // update response headers
        if (HttpException.class.isAssignableFrom(ex.getClass())) {
            Map<String, String> headers = ((HttpException) ex).getResponseHeaders();
            if (MapUtils.isNotEmpty(headers)) {
                for (String key : headers.keySet()) {
                    response.addHeader(key, headers.get(key));
                }
            }
        }

        // persist?
        if (true) {
            try {
                error = this.systemErrorService.create(error);
            } catch (HttpException e) {
                LOGGER.error("Failed persisting system error", e);
            }

        }

        return error;
    }

    private HttpStatus getStandardExceptionHttpStatus(Exception ex) {
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