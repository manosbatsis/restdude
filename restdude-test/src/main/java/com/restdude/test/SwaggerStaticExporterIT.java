/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-test, https://manosbatsis.github.io/restdude/restdude-test
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.get;

/**
 * Generates static swagger docs
 */
public class SwaggerStaticExporterIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerStaticExporterIT.class);

    @Test(priority = 10, description = "Test the swagger endpoint and create the static files documentation")
    public void testCreateStaticDocs() throws Exception {
        try {
            // get swagger document
            String json = get(WEBCONTEXT_PATH + "/v2/api-docs").asString();

            // create confluence
            this.makeDocs(json, Paths.get("target/swagger2asciidoc"), MarkupLanguage.ASCIIDOC);

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
    private void makeDocs(String json, Path outputDirectory, MarkupLanguage markupLanguage) {
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