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
package com.restdude.test;


import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

/**
 * Generates static swagger docs in {@value SwaggerStaticExporterIT#GENERATED_ASCIIDOCS_PATH}
 */
public class SwaggerStaticExporterIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerStaticExporterIT.class);
    protected static final String GENERATED_ASCIIDOCS_PATH = "target/swagger2asciidoc";


    @Test(priority = 100, description = "Test the swagger controller and create the static files documentation")
    public void testCreateStaticDocs() throws Exception {
        try {

            Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");
            RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;
            // get swagger document
            String swaggerPath = WEBCONTEXT_PATH + "/v2/api-docs";
            String json = given().spec(adminRequestSpec).log().all().get(swaggerPath).then().statusCode(200).extract().asString();


            // create confluence
            Path targetFolder = Paths.get(SwaggerStaticExporterIT.GENERATED_ASCIIDOCS_PATH);
            LOGGER.debug("Creating static docs at: {}", targetFolder);
            this.makeDocs(json, targetFolder, MarkupLanguage.ASCIIDOC);

        } catch (Exception e) {
            LOGGER.error("Failed generating static docs", e);
            throw e;
        }
    }

    /**
     * Create documentation from the given swagger JSON input
     *
     * @param json            the swagger JSON input
     * @param outputDirectory the directory to create the docs into
     * @param markupLanguage  the markup language to use
     */
    protected void makeDocs(String json, Path outputDirectory, MarkupLanguage markupLanguage) {
        // config
        Swagger2MarkupConfig configMarkdown = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(markupLanguage)
                .withOutputLanguage(Language.EN)
                .withPathsGroupedBy(GroupBy.TAGS)
                .build();

        // create docs
        Swagger2MarkupConverter.from(json)
                .withConfig(configMarkdown)
                .build()
                .toFile(outputDirectory.resolve("index" + markupLanguage.getFileNameExtensions().get(0)));
    }

}