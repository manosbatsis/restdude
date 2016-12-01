package com.restdude.domain.error.resolver;


import com.restdude.domain.error.model.SystemError;
import org.springframework.core.convert.converter.Converter;

/**
 * A {@code RestErrorConverter} is an intermediate 'bridge' component in the response rendering pipeline: it converts
 * a {@link SystemError} object into another object that is potentially better suited for HTTP response rendering by an
 * {@link org.springframework.http.converter.HttpMessageConverter HttpMessageConverter}.
 * <p/>
 * For example, a {@code RestErrorConverter} implementation might produce an intermediate Map of name/value pairs.
 * This resulting map might then be given to an {@code HttpMessageConverter} to write the response body:
 * <pre>
 *     Object result = mapRestErrorConverter.convert(aRestError);
 *     assert result instanceof Map;
 *     ...
 *     httpMessageConverter.write(result, ...);
 * </pre>
 * <p/>
 * This allows spring configurers to use or write simpler RestError conversion logic and let the more complex registered
 * {@code HttpMessageConverter}s operate on the converted result instead of needing to implement the more
 * complex {@code HttpMessageConverter} interface directly.
 *
 * @param <T> The type of object produced by the converter.
 * @author Les Hazlewood
 * @see org.springframework.http.converter.HttpMessageConverter
 * @see Converter
 */
public interface RestErrorConverter<T> extends Converter<SystemError, T> {

    /**
     * Converts the RestError instance into an object that will then be used by an
     * {@link org.springframework.http.converter.HttpMessageConverter HttpMessageConverter} to render the response body.
     *
     * @param re the {@code RestError} instance to convert to another object instance 'understood' by other registered
     *           {@code HttpMessageConverter} instances.
     * @return an object suited for HTTP response rendering by an {@code HttpMessageConverter}
     */
    T convert(SystemError re);
}