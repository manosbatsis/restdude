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
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Test(/*singleThreaded = true, */description = "Test relationship endpoints")
@SuppressWarnings("unused")
public class RelationsIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationsIT.class);

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

    @Test(description = "Test ToOne")
    public void testSimpleTypeProperty() throws Exception {
        LOGGER.debug("testSimpleTypeProperty, simple JSON");
        JsonNode eu = this.getCountryParent(adminRequestSpec);

        Assert.assertEquals(eu.get("pk").asText(), "EU");
        Assert.assertEquals(eu.get("name").asText(), "Europe");


        LOGGER.debug("testSimpleTypeProperty, JSON API");
        eu = this.getCountryParent(getRequestSpec(adminLoginContext.ssoToken, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8));

        Assert.assertEquals(eu.get("data").get("id").asText(), "EU");
        Assert.assertEquals(eu.get("data").get("attributes").get("name").asText(), "Europe");

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
