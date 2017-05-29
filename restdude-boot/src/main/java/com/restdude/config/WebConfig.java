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
package com.restdude.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.restdude.domain.error.resolver.RestExceptionHandler;
import com.restdude.hypermedia.util.HypermediaUtils;
import com.restdude.mdd.binding.CsvMessageConverter;
import com.restdude.mdd.binding.CustomEnumConverterFactory;
import com.restdude.mdd.binding.StringToEmbeddableManyToManyIdConverterFactory;
import com.restdude.web.filters.RestRequestNormalizerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter /*implements WebMvcRegistrations*/ {


    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);


    /**
     * Automatically collect and persist errors
     */
    @Bean
    public HandlerExceptionResolver restExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    // TODO: refactor to programmatic CORS control
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/auth/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                //.allowedHeaders("*")
                //.exposedHeaders("*")
                .allowedMethods("HEAD", "GET", "OPTIONS", "PUT", "PATCH", "POST", "DELETE")
                .maxAge(3600);
        registry.addMapping("/api/rest/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                //.allowedHeaders("*")
                //.exposedHeaders("*")
                .allowedMethods("HEAD", "GET", "OPTIONS", "PUT", "PATCH", "POST", "DELETE")
                .maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        RestRequestNormalizerFilter restRequestNormalizerFilter = new RestRequestNormalizerFilter();
        registrationBean.setFilter(restRequestNormalizerFilter);
        registrationBean.addUrlPatterns("/api/*", "/apiauth/*", "/ws/*", "/v2/api-docs");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToEnable(com.fasterxml.jackson.databind.MapperFeature.DEFAULT_VIEW_INCLUSION)
                .featuresToDisable(
                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        com.fasterxml.jackson.databind.DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
                        com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
                        com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return builder;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverterFactory(new CustomEnumConverterFactory());
        registry.addConverterFactory(new StringToEmbeddableManyToManyIdConverterFactory());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new CsvMessageConverter());
        converters.add(new StringHttpMessageConverter());
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        super.configureHandlerExceptionResolvers(exceptionResolvers);
        exceptionResolvers.add(restExceptionHandler());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false)
                .favorParameter(true)
                //.defaultContentType(new MediaType("application", "vnd.api+json"))
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);
    }

    /*
    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return null;
    }

    @Override
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return null;
    }

*/

}