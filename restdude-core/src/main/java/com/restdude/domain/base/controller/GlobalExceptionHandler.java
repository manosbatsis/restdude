package com.restdude.domain.base.controller;


import com.restdude.domain.base.model.ErrorInfo;
import com.restdude.util.exception.BadRequestException;
import com.restdude.util.exception.http.HttpException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleBadRequestException(HttpServletRequest request, Exception e) {
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
        HttpStatus status = ex.getStatus();

        // build error info
        ErrorInfo.Builder builder = new ErrorInfo.Builder()
                .message(ex.getMessage())
                .code(status.value())
                .status(status.getReasonPhrase());

        // update response
        response.setStatus(status.value());
        Map<String, String> headers = ex.getResponseHeaders();
        if (MapUtils.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                response.addHeader(key, headers.get(key));
            }
        }

        return builder.build();
    }
}