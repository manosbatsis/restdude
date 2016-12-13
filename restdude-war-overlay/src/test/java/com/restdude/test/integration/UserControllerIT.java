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

import com.restdude.domain.users.model.User;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Test(/*singleThreaded = true, */description = "User entity tests")
@SuppressWarnings("unused")
public class UserControllerIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerIT.class);

    @Test(priority = 20, description = "Test registration")
    public void testPatch() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;

        // --------------------------------
        // Patch
        // --------------------------------
        User user = given().spec(adminRequestSpec)
                .body(new User.Builder()
                        .firstName("Adminfirst")
                        .lastName("Adminlast")
                        .build())
                .log().all()
                .patch("/calipso/api/rest/users/" + adminLoginContext.userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                // test assertions
                .body("id", equalTo(adminLoginContext.userId))
                // get model
                .extract().as(User.class);
    }

    @Test(priority = 30, description = "Test image upload")
    public void testImageUpload() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------

        // login and get a request spec for stateless auth
        // essentially this posts to "/calipso/apiauth/userDetails"
        // with request body: {username: "admin", password: "admin"}
        Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;

        // --------------------------------
        // Upload user images
        // --------------------------------
        String file1 = "user_banner.png";
        String file2 = "user_avatar.jpg";
        final byte[] bytes1 = IOUtils.toByteArray(getClass().getResourceAsStream("/" + file1));
        final byte[] bytes2 = IOUtils.toByteArray(getClass().getResourceAsStream("/" + file2));

        // select user 	
        User user = given().spec(adminRequestSpec)
                .get("/calipso/api/rest/users/" + adminLoginContext.userId)
                .then().assertThat()
                // test assertions
                .body("id", notNullValue())
                // get model
                .extract().as(User.class);
        LOGGER.info("User before uploading files: {}", user);


        User userAfterUploading = given()
                .contentType("multipart/form-data")
                .cookies(Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME, adminLoginContext.ssoToken)
                .multiPart(new MultiPartSpecBuilder(bytes1)
                        .fileName(file1)
                        .controlName("bannerUrl")
                        .mimeType("image/png").build())
                .multiPart(new MultiPartSpecBuilder(bytes2)
                        .fileName(file1)
                        .controlName("avatarUrl")
                        .mimeType("image/jpeg").build())
                .when().post("/calipso/api/rest/users/" + user.getId() + "/files")
                .then()
                .statusCode(200)
                .extract().as(User.class);


        LOGGER.info("User after uploading files: {}", userAfterUploading);

        Assert.assertEquals(user, userAfterUploading);
    }


}
