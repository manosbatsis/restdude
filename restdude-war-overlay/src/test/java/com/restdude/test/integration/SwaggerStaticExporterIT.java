package com.restdude.test.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Generates static swagger docs
 */
@Test(singleThreaded = true, description = "Swagger documentation test")
public class SwaggerStaticExporterIT extends com.restdude.test.SwaggerStaticExporterIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerStaticExporterIT.class);

}