package com.restdude.config;

import com.restdude.util.ConfigurationFactory;
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
@Configuration
public class SwaggerConfig {


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
        return UiConfiguration.DEFAULT;
    }

    private ApiInfo apiInfo() {
        org.apache.commons.configuration.Configuration config = ConfigurationFactory.getConfiguration();

        String appName = config.getString(ConfigurationFactory.APP_NAME, null);
        String appVersion = config.getString(ConfigurationFactory.APP_VERSION, null);

        String contactName = config.getString(ConfigurationFactory.CONTACT_NAME, null);
        String contactUrl = config.getString(ConfigurationFactory.CONTACT_URL);
        String contactEmail = config.getString(ConfigurationFactory.CONTACT_EMAIL, null);

        String licenseName = config.getString(ConfigurationFactory.LICENSE_NAME, null);
        String licenseUrl = config.getString(ConfigurationFactory.LICENSE_URL, null);


        Contact contact = new Contact(contactName, contactUrl, contactEmail);
        return new ApiInfo(appName + " API Reference " + appVersion,
                "Automatically-generated documentation based on [Swagger](http://swagger.io/) and created by [Springfox](http://springfox.github.io/springfox/).",
                appVersion, "urn:tos", contact, licenseName,
                licenseUrl);
    }
}
