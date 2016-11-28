package com.restdude.util.exception;

import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.util.exception.http.SystemException;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Renders a response with, and optionally persists,  a RESTful Error representation based on the {@link SystemError} POJO.
 * Persistence of SystemError objects is optional and can be configured using calipso.validationErrors.system.persist* configuration properties.
 * <p>
 * At a high-level, this implementation functions as follows:
 * <p>
 * <ol>
 * <li>Upon encountering an Exception, the configured {@link RestErrorResolver} is consulted to resolve the
 * exception into a {@link SystemError} instance.</li>
 * <li>The HTTP Response's Status Code will be set to the {@code RestError}'s
 * {@link SystemError#getHttpStatusCode()} value.</li>
 * <li>The {@code RestError} instance is presented to a configured {@link RestErrorConverter} to allow transforming
 * the {@code RestError} instance into an object potentially more suitable for rendering as the HTTP response body.</li>
 * <li>The
 * {@link #setMessageConverters(org.springframework.http.converter.HttpMessageConverter[]) HttpMessageConverters}
 * are consulted (in iteration order) with this object result for rendering.  The first
 * {@code HttpMessageConverter} instance that {@link HttpMessageConverter#canWrite(Class, org.springframework.http.MediaType) canWrite}
 * the object based on the request's supported {@code MediaType}s will be used to render this result object as
 * the HTTP response body.</li>
 * <li>If no {@code HttpMessageConverter}s {@code canWrite} the result object, nothing is done, and this handler
 * returns {@code null} to indicate other ExceptionResolvers potentially further in the resolution chain should
 * handle the exception instead.</li>
 * </ol>
 * <p>
 * <h3>Defaults</h3>
 * This implementation has the following property defaults:
 * <table>
 * <tr>
 * <th>Property</th>
 * <th>Instance</th>
 * <th>Notes</th>
 * </tr>
 * <tr>
 * <td>errorResolver</td>
 * <td>{@link DefaultRestErrorResolver DefaultRestErrorResolver}</td>
 * <td>Converts Exceptions to {@link SystemError} instances.  Should be suitable for most needs.</td>
 * </tr>
 * <tr>
 * <td>messageConverters</td>
 * <td>multiple instances</td>
 * <td>Default collection are those automatically enabled by Spring as
 * <a href="http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-config-enable">defined here</a> (specifically item #5)</td>
 * </tr>
 * </table>
 * <p>
 * <h2>JSON Rendering</h2>
 * This implementation comes pre-configured with Spring's typical default
 * {@link HttpMessageConverter} instances; JSON will be enabled automatically if Jackson is in the classpath.  If you
 * want to match the JSON representation shown in the article above (recommended) but do not want to use Jackson
 * (or the Spring's default Jackson config), you will need to
 * {@link #setMessageConverters(org.springframework.http.converter.HttpMessageConverter[]) configure} a different
 * JSON-capable {@link HttpMessageConverter}.
 *
 * @author Les Hazlewood
 * @see DefaultRestErrorResolver
 * @see HttpMessageConverter
 * @see org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
 */
public class RestExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);


    private SystemErrorService systemErrorService;

    private HttpMessageConverter<?>[] messageConverters = null;

    private List<HttpMessageConverter<?>> allMessageConverters = null;

    private RestErrorResolver errorResolver;

    private RestErrorConverter<?> errorConverter;

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    public RestExceptionHandler() {
        this.errorResolver = new DefaultRestErrorResolver();
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    public void setErrorResolver(RestErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }

    public RestErrorResolver getErrorResolver() {
        return this.errorResolver;
    }

    public RestErrorConverter<?> getErrorConverter() {
        return errorConverter;
    }

    public void setErrorConverter(RestErrorConverter<?> errorConverter) {
        this.errorConverter = errorConverter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ensureMessageConverters();
    }

    @SuppressWarnings("unchecked")
    private void ensureMessageConverters() {

        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();

        //user configured values take precedence:
        if (this.messageConverters != null && this.messageConverters.length > 0) {
            converters.addAll(CollectionUtils.arrayToList(this.messageConverters));
        }

        //defaults next:
        new HttpMessageConverterHelper().addDefaults(converters);

        this.allMessageConverters = converters;
    }

    //leverage Spring's existing default setup behavior:
    private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        public void addDefaults(List<HttpMessageConverter<?>> converters) {
            addDefaultHttpMessageConverters(converters);
        }
    }

    /**
     * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
     * represents a specific error page if appropriate.
     * <p/>
     * May be overridden in subclasses, in order to apply specific
     * exception checks. Note that this template method will be invoked <i>after</i> checking whether this resolved applies
     * ("mappedHandlers" etc), so an implementation may simply proceed with its actual exception handling.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or <code>null</code> if none chosen at the time of the exception (for example,
     *                 if multipart resolution failed)
     * @param ex       the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOGGER.error("#doResolveException: ", ex);
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        RestErrorResolver resolver = getErrorResolver();

        SystemError error = resolver.resolveError(webRequest, handler, ex);
        if (error == null) {
            return null;
        }
        // persist?
        if (true) {
            try {
                error = this.systemErrorService.create(error);
            } catch (SystemException e) {
                LOGGER.error("Failed persisting system error", e);
            }

        }
        ModelAndView mav = null;

        try {
            mav = getModelAndView(webRequest, handler, error);
        } catch (Exception invocationEx) {
            LOGGER.error("Failed resolving exception [" + ex + "]", invocationEx);
        }

        return mav;
    }

    protected ModelAndView getModelAndView(ServletWebRequest webRequest, Object handler, SystemError error) throws Exception {

        // apply response status
        applyStatusIfPossible(webRequest, error);
        // apply response headers
        applyHeadersIfPossible(webRequest, error);

        // set the error as default body to handle
        // cases where no converter is configured
        Object body = error;

        // apply converter if any
        RestErrorConverter converter = getErrorConverter();
        if (converter != null) {
            body = converter.convert(error);
        }

        // handle response
        return handleResponseBody(body, webRequest);
    }

    private void applyStatusIfPossible(ServletWebRequest webRequest, SystemError error) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(error.getHttpStatusCode().intValue());
        }
    }

    private void applyHeadersIfPossible(ServletWebRequest webRequest, SystemError error) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())
                && MapUtils.isNotEmpty(error.getResponseHeaders())) {

            Map<String, String> resHeaders = error.getResponseHeaders();

            HttpServletResponse response = webRequest.getResponse();
            for (String headerName : resHeaders.keySet()) {
                response.addHeader(headerName, resHeaders.get(headerName));
            }
        }
    }

    private ModelAndView handleResponseBody(Object body, ServletWebRequest webRequest) throws ServletException, IOException {

        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
        Class<?> bodyType = body.getClass();
        List<HttpMessageConverter<?>> converters = this.allMessageConverters;

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        //return empty model and view to let
                        //Spring know a view has already been rendered
                        return new ModelAndView();
                    }
                }
            }
        }

        LOGGER.warn("No suitable HttpMessageConverter for class: {} and MIME: {}", bodyType.getCanonicalName(), acceptedMediaTypes);
        return null;
    }
}