package com.restdude.util.exception;


import com.restdude.domain.error.model.SystemError;
import com.restdude.util.exception.http.SystemException;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Default {@code RestErrorResolver} implementation that converts discovered Exceptions to
 * {@link SystemError} instances.
 */
public class DefaultRestErrorResolver implements RestErrorResolver, MessageSourceAware, InitializingBean {

    private static Map<Class, Integer> exceptionStatuses = new HashMap<Class, Integer>();

    static {
        exceptionStatuses.put(NoSuchRequestHandlingMethodException.class, HttpServletResponse.SC_NOT_FOUND);
        //exceptionStatuses.put(AuthenticationException.class, HttpServletResponse.SC_UNAUTHORIZED);
        //exceptionStatuses.put(UsernameNotFoundException.class, HttpServletResponse.SC_UNAUTHORIZED);
        exceptionStatuses.put(ObjectNotFoundException.class, HttpServletResponse.SC_NOT_FOUND);
        exceptionStatuses.put(EntityNotFoundException.class, HttpServletResponse.SC_NOT_FOUND);
        exceptionStatuses.put(EntityExistsException.class, HttpServletResponse.SC_CONFLICT);
        exceptionStatuses.put(HttpRequestMethodNotSupportedException.class, HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        exceptionStatuses.put(HttpMediaTypeNotSupportedException.class, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        exceptionStatuses.put(HttpMediaTypeNotAcceptableException.class, HttpServletResponse.SC_NOT_ACCEPTABLE);
        exceptionStatuses.put(MissingPathVariableException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(DataIntegrityViolationException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(MissingServletRequestParameterException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(ServletRequestBindingException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(ValidationException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(ConversionNotSupportedException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(TypeMismatchException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(HttpMessageNotReadableException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(HttpMessageNotWritableException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(MethodArgumentNotValidException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(MissingServletRequestPartException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(BindException.class, HttpServletResponse.SC_BAD_REQUEST);
        exceptionStatuses.put(NoHandlerFoundException.class, HttpServletResponse.SC_NOT_FOUND);
        exceptionStatuses.put(AsyncRequestTimeoutException.class, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        exceptionStatuses.put(RuntimeException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionStatuses.put(Exception.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRestErrorResolver.class);

    protected MessageSource messageSource;


    public DefaultRestErrorResolver() {
    }


    @Override
    public SystemError resolveError(ServletWebRequest request, Object handler, Exception ex) {

        // get response status
        HttpStatus status = null;

        // if  SystemException
        if (SystemException.class.isAssignableFrom(ex.getClass())) {
            status = ((SystemException) ex).getStatus();
        } else {
            status = this.getStandardExceptionHttpStatus(ex);
        }

        // build and return error
        return this.createSystemError(request, status.value(), ex);
    }

    protected HttpStatus getStandardExceptionHttpStatus(Exception ex) {
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

    protected SystemError createSystemError(ServletWebRequest request, Integer status, Exception ex) {

        // create error instance
        return new SystemError(request.getRequest(), status, ex.getMessage(), ex);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}