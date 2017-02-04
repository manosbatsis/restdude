/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@RestController
//@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);


    @Autowired
    private ErrorAttributes errorAttributes;
    private List<ErrorViewResolver> errorViewResolvers;


    public ErrorController(){
    }

    public ErrorController(ErrorAttributes errorAttributes) {
        this(errorAttributes, (List) null);
    }

    public ErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
        this.errorViewResolvers = this.sortErrorViewResolvers(errorViewResolvers);
    }


    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT, RequestMethod.TRACE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map body = this.getErrorAttributes(request, true);
        LOGGER.debug("body: {}", body);
        LOGGER.debug("errorPath: {}", this.getErrorPath());
        LOGGER.debug("status: {}", this.getStatus(request));
        HttpStatus status = this.getStatus(request);
        return new ResponseEntity(body, status);
    }

    private List<ErrorViewResolver> sortErrorViewResolvers(List<ErrorViewResolver> resolvers) {
        ArrayList sorted = new ArrayList();
        if (resolvers != null) {
            sorted.addAll(resolvers);
            AnnotationAwareOrderComparator.sortIfNecessary(sorted);
        }

        return sorted;
    }

    public String getErrorPath() {
        return "/error";
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    protected boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        return parameter == null ? false : !"false".equals(parameter.toLowerCase());
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode.intValue());
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
