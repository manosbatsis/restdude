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
import com.restdude.util.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Test(/*singleThreaded = true, */description = "Test JSON API 1.1 compliance")
@SuppressWarnings("unused")
public class JsonApiIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonApiIT.class);



    @Test(description = "Test invalid credentials", priority = 30)
    public void testSingleResourceDocument() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext lctx = this.getLoggedinContext("admin", "admin");
        RequestSpecification requestSpec = getRequestSpec(lctx.ssoToken, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8);


        // get a country document
        Response rs = given().spec(requestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/countries/GR");

        // validate response
        rs.then().log().all().assertThat()
                .statusCode(200)
                .body("data.id", equalTo("GR"))
                .body("data.attributes.name", equalTo("Greece"));


    }


}
