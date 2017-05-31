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

import com.restdude.test.AbstractControllerIT;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Test(/*singleThreaded = true, */description = "Test dynamic JPA specifications used in default search stack")
@SuppressWarnings("unused")
public class SpecificationsControllerIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationsControllerIT.class);

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

    @Test(description = "Test simple type properties")
    public void testSimpleTypeProperty() throws Exception {
        LOGGER.debug("testSimpleTypeProperty");
        given().spec(adminRequestSpec).param("name", "Greece")
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/countries")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("content[0].name", equalTo("Greece"));
    }

    @Test(description = "Test related entity properties")
    public void testRelatedEntityTypeProperty() throws Exception {
        LOGGER.debug("testRelatedEntityTypeProperty");
        given().spec(adminRequestSpec).
                param("parent", "AS")
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/countries")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200).
                body("content[0].parent.id", equalTo("AS"));
    }


    @Test(description = "Test related entity IDs")
    public void testRelatedEntityId() throws Exception {
        LOGGER.debug("testRelatedEntityId");
        for(String parentIdPath : new String[]{"parent", "parent.id"}){
            given().spec(adminRequestSpec)
                    .param(parentIdPath, "AS")
                    .log().all()
                    .get(WEBCONTEXT_PATH + "/api/rest/countries")
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .assertThat()
                    .body("content[0].parent.id", equalTo("AS"));
        }
    }

    @Test(description = "Test pathFragment to related entities simple type property")
    public void testPathToRelatedSimpleTypeProperty() throws Exception {
        LOGGER.debug("testPathToRelatedSimpleTypeProperty");
        given().spec(adminRequestSpec).
                param("parent.name", "Oceania")
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/countries")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200).
                body("content[0].parent.name", equalTo("Oceania"));
    }
}
