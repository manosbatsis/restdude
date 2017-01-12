/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-boot, https://manosbatsis.github.io/restdude/restdude-boot
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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

import com.restdude.domain.base.binding.CsvMessageConverter;
import com.restdude.domain.base.binding.CustomEnumConverterFactory;
import com.restdude.domain.base.binding.StringToEmbeddableManyToManyIdConverterFactory;
import com.restdude.domain.error.resolver.RestExceptionHandler;
import com.restdude.mdd.processor.ModelDrivenBeanGeneratingRegistryPostProcessor;
import com.restdude.web.filters.RestRequestNormalizerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public HandlerExceptionResolver restExceptionHandler() {
        RestExceptionHandler handler = new RestExceptionHandler();
        return handler;
    }

    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ModelDrivenBeanGeneratingRegistryPostProcessor modelDrivenBeanGeneratingRegistryPostProcessor() {
        return new ModelDrivenBeanGeneratingRegistryPostProcessor();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        RestRequestNormalizerFilter restRequestNormalizerFilter = new RestRequestNormalizerFilter();
        registrationBean.setFilter(restRequestNormalizerFilter);
        registrationBean.addUrlPatterns("/api/*", "/apiauth/*", "/ws/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
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
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        super.configureHandlerExceptionResolvers(exceptionResolvers);
        exceptionResolvers.add(restExceptionHandler());
        LOGGER.debug("configureHandlerExceptionResolvers: {}", exceptionResolvers);
    }

}