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
            String json = get("/calipso/v2/api-docs").asString();

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