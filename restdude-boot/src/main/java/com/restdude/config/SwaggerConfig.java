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


import com.restdude.util.ConfigurationFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
//@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${restdude.appVersion}")
    private String applicationVersion;

    @Value("${restdude.contact.name}")
    private String contactName;
    @Value("${restdude.contact.url}")
    private String contactUrl;
    @Value("${restdude.contact.email}")
    private String contactEmail;


    @Value("${restdude.license.name}")
    private String licenseName;
    @Value("${restdude.license.url}")
    private String licenseUrl;

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration((String) null);
    }

    private ApiInfo apiInfo() {
        org.apache.commons.configuration.Configuration config = ConfigurationFactory.getConfiguration();


        Contact contact = new Contact(contactName, contactUrl, contactEmail);
        return new ApiInfo(applicationName + " API Reference " + applicationVersion,
                "Automatically-generated documentation based on [Swagger](http://swagger.io/) and created by [Springfox](http://springfox.github.io/springfox/).",
                applicationVersion, "urn:tos", contact, licenseName,
                licenseUrl);
    }
}
