package com.restdude.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static String setBaseUrl(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        String baseUrl = (String) request.getAttribute(Constants.BASE_URL_KEY);
        if (StringUtils.isBlank(baseUrl)) {
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String ctx = request.getContextPath();
            baseUrl = url.substring(0, url.length() - uri.length() + ctx.length());
            String scheme = request.getHeader("X-Forwarded-Proto");
            if (StringUtils.isNotBlank(scheme) && scheme.equalsIgnoreCase("HTTPS") && baseUrl.startsWith("http:")) {
                baseUrl = baseUrl.replaceFirst("http:", "https:");
            }
            request.setAttribute(Constants.BASE_URL_KEY, baseUrl);
            LOGGER.debug("Added request attribute '{}': {}", Constants.BASE_URL_KEY, baseUrl);
            request.setAttribute(Constants.DOMAIN_KEY, request.getServerName());
        }
        return baseUrl;
    }

}
