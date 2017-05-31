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

import com.restdude.domain.users.model.Role;
import com.restdude.test.AbstractControllerIT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Test(/*singleThreaded = true, */description = "Test JWT")
@SuppressWarnings("unused")
public class JwtIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtIT.class);



    @Test(description = "Test JWT login", priority = 10)
    public void testJwtLogin() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext lctx = getJwtLoggedinContext("admin", "admin", false, true);

        String accessJwt = lctx.jwtAccessToken;

        // ensure we got a cookie value
        Assert.assertTrue("JWT cookie must not be blank", StringUtils.isNotBlank(accessJwt));

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey("foobar")
                .parseClaimsJws(accessJwt);
        LOGGER.debug("Claims: {}", claims);
        String name = claims.getBody().get("name").toString();

        Assert.assertEquals("Admin User", name);

        // attempt to make an authorized request
        RequestSpecification requestSpec = lctx.requestSpec;

        Role role = given().spec(requestSpec)
                .log().all()
                .body(new Role("ROLE_TEST_JWT"))
                .post(WEBCONTEXT_PATH + "/api/rest/" + Role.API_PATH)
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("ROLE_TEST_JWT"))
                // get model
                .extract().as(Role.class);

    }


}
