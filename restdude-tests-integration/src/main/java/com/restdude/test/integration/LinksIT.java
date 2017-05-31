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
package com.restdude.test.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Test(/*singleThreaded = true, */description = "Test relationship endpoints")
@SuppressWarnings("unused")
public class LinksIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinksIT.class);

    Loggedincontext adminLoginContext;
    RequestSpecification adminRequestSpec;

    @BeforeClass
    public void setup() {
        super.setup();
        // parse JSON by default
        // RestAssured.defaultParser = Parser.JSON;

        this.adminLoginContext = this.getLoggedinContext("admin", "admin");
        this.adminRequestSpec = adminLoginContext.requestSpec;
    }

    @Test(description = "Test ToOne", priority = 10)
    public void testSimpleTypeProperty() throws Exception {
        LOGGER.debug("testSimpleTypeProperty, simple JSON");
        JsonNode eu = this.getCountryParent(adminRequestSpec);

        Assert.assertEquals(eu.get("id").asText(), "EU");
        Assert.assertEquals(eu.get("name").asText(), "Europe");


        LOGGER.debug("testSimpleTypeProperty, JSON API");
        eu = this.getCountryParent(getRequestSpec(adminLoginContext.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8));

        Assert.assertEquals(eu.get("data").get("id").asText(), "EU");
        Assert.assertEquals(eu.get("data").get("attributes").get("name").asText(), "Europe");

    }

    @Test(description = "Test paging", priority = 20)
    public void testRelatedPaging() throws Exception {
        LOGGER.debug("testRelatedPaging, simple JSON");
        ExtractableResponse country = given().spec(adminRequestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/continents/EU")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("links", not(emptyArray()))
                .extract();

        String countriesUrl = country.jsonPath().getString("links.find {it.rel == 'countries'}.href");
        LOGGER.debug("testRelatedPaging, countriesUrl: {}", countriesUrl);
        Assert.assertNotNull("Could not find an href for a link with rel 'countries'", countriesUrl);

        given().spec(adminRequestSpec)
                .log().all()
                .get(countriesUrl)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("content[0].parent.id", equalTo("EU"))
                .body("content[1].parent.id", equalTo("EU"))
                .body("content[2].parent.id", equalTo("EU"))
                .body("content[3].parent.id", equalTo("EU"));

        given().spec(adminRequestSpec)
                .log().all()
                .param("name", "Greec%")
                .get(countriesUrl)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("content[0].parent.id", equalTo("EU"))
                .body("content[0].name", equalTo("Greece"));

        LOGGER.debug("testRelatedPaging, JSON API");
        RequestSpecification adminJsonApiRequestSpec = getRequestSpec(adminLoginContext.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8);
        country = given().spec(adminJsonApiRequestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/continents/EU")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("links", not(emptyArray()))
                .extract();
        countriesUrl = country.jsonPath().getString("links.countries.href");
        LOGGER.debug("testRelatedPaging, countriesUrl: {}", countriesUrl);
        Assert.assertNotNull("Could not find an href for a link with rel 'countries'", countriesUrl);

        given().spec(adminJsonApiRequestSpec)
                .log().all()
                .get(countriesUrl)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("data[0].attributes.parent.id", equalTo("EU"))
                .body("data[1].attributes.parent.id", equalTo("EU"))
                .body("data[2].attributes.parent.id", equalTo("EU"))
                .body("data[3].attributes.parent.id", equalTo("EU"));
        given().spec(adminJsonApiRequestSpec)
                .log().all()
                .param("name", "Greec%")
                .get(countriesUrl)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("data[0].attributes.parent.id", equalTo("EU"))
                .body("data[0].attributes.name", equalTo("Greece"));

    }


    private JsonNode getCountryParent(RequestSpecification requestSpec) throws Exception {
        LOGGER.debug("testSimpleTypeProperty");
        return given().spec(requestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/countries/GR/relationships/parent")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract().as(JsonNode.class);
    }

}
