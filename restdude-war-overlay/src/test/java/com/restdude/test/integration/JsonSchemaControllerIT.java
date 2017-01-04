/**
 * calipso-hub-webapp - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.test.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.restdude.test.AbstractControllerIT;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Test(/*singleThreaded = true, */description = "JSON Schema tests")
@SuppressWarnings("unused")
public class JsonSchemaControllerIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaControllerIT.class);

    @Test(priority = 20, description = "Test JSON schema retreival")
    public void testPatch() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;

        // --------------------------------
        // Gwt user schema
        // --------------------------------
        JsonNode schema = given().spec(adminRequestSpec)
                .log().all()
                .get("/calipso/api/rest/users/jsonschema")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                // get model
                .extract().as(JsonNode.class);
    }


}
