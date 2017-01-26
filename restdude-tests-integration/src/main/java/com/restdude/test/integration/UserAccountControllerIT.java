/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-tests-integration, https://manosbatsis.github.io/restdude/restdude-tests-integration
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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

import com.restdude.auth.userAccount.model.UserAccountRegistration;
import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.domain.users.model.User;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import com.restdude.util.HashUtils;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Test(description = "Test user account endpoint")
@SuppressWarnings("unused")
public class UserAccountControllerIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountControllerIT.class);

    @Test(priority = 10, description = "Test username change")
    public void testUpdateUsername() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext adminLoginContext = this.getLoggedinContext("simple", "simple");

        // --------------------------------
        // Update username
        // --------------------------------
        Response rs = changeUsername(adminLoginContext, new UsernameChangeRequest.Builder().username("simplenew").password("simple").build());


        // --------------------------------
        //  Update first/last name
        // --------------------------------
        User user = given().spec(adminLoginContext.requestSpec)
                .body(new User.Builder()
                        .firstName("Newfirst")
                        .lastName("Newlast")
                        .build())
                .log().all()
                .patch(WEBCONTEXT_PATH + "/api/rest/users/" + adminLoginContext.userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                // test assertions
                .body("pk", equalTo(adminLoginContext.userId))
                .body("firstName", equalTo("Newfirst"))
                .body("lastName", equalTo("Newlast"))
                .body("username", equalTo("simplenew"))
                // get model
                .extract().as(User.class);

        // --------------------------------
        //  Update username again
        // --------------------------------
        rs = changeUsername(adminLoginContext, new UsernameChangeRequest.Builder().username("simple").password("simple").build());
    }

    protected Response changeUsername(Loggedincontext loginContext, UsernameChangeRequest usernameChangeRequest) {
        LOGGER.debug("changeUsername, loginContext: {}, usernameChangeRequest: {}", loginContext, usernameChangeRequest);
        Response rs = given().spec(loginContext.requestSpec)
                .body(usernameChangeRequest)
                .log().all()
                .put(WEBCONTEXT_PATH + "/api/auth/account/username")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .cookie(Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME, notNullValue())
                .body("pk", equalTo(loginContext.userId))
                .body("username", equalTo(usernameChangeRequest.getUsername()))
                // get model
                .extract().response();

        // Get result cookie and user pk
        loginContext.ssoToken = rs.getCookie(Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME);

        RequestSpecification requestSpec = getRequestSpec(loginContext.ssoToken);
        loginContext.requestSpec = requestSpec;

        return rs;
    }

    @Test(description = "Test logging in with correct credentials")
    public void testAnonymousRemember() throws Exception {

        ExtractableResponse<Response> rs = given().accept(JSON_UTF8).contentType(JSON_UTF8).when()
                .get(WEBCONTEXT_PATH + "/api/auth/userDetails")
                .then()
                .log().all()
                .assertThat()
                .body("username", equalTo("anonymousUser")).extract();
    }

    @Test(description = "Test logging in with correct credentials")
    public void testCorrectLogin() throws Exception {
        this.getLoggedinContext("admin", "admin");
    }


    @Test(priority = 10, description = "Test registration with password set during confirmation")
    public void testRegistrationWithPasswordSetOnConfirm() throws Exception {

        String email = "ittestreg1@UserAccountControllerIT.com";
        RequestSpecification spec = this.getRequestSpec(null);

        // register
        User user = given().spec(spec)
                .log().all()
                .body(new UserAccountRegistration.Builder()
                        .firstName("Firstname")
                        .lastName("LastName")
                        .email(email)
                        .build())
                .post(WEBCONTEXT_PATH + "/api/auth/account")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                // test assertions
                .body("pk", notNullValue())
                .body("emailHash", equalTo(HashUtils.md5Hex(email)))
                // get model
                .extract().as(User.class);

        // obtain confirmation token
        String confirmationToken = this.getConfirmationToken(user);
        Assert.assertNotNull(confirmationToken);

        // confirm registration
        HashMap<String, String> confirmationRequestBody = new HashMap<>();
        confirmationRequestBody.put("email", email);
        confirmationRequestBody.put("resetPasswordToken", confirmationToken);
        confirmationRequestBody.put("password", "123");
        confirmationRequestBody.put("passwordConfirmation", "123");

        user = given().spec(spec)
                .log().all()
                .body(confirmationRequestBody)
                .put(WEBCONTEXT_PATH + "/api/auth/account")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                // test assertions
                .body("pk", notNullValue())
                .body("emailHash", equalTo(HashUtils.md5Hex(email)))
                .cookie(Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME, notNullValue())
                // get model
                .extract().as(User.class);

        // test successful login
        Loggedincontext adminContext = this.getLoggedinContext(email, "123");

    }


    @Test(priority = 10, description = "Test registration with password set during initial submission")
    public void testRegistrationWithPasswordSetOnInitialSubmission() throws Exception {

        String email = "ittestreg2@UserAccountControllerIT.com";
        RequestSpecification spec = this.getRequestSpec(null);

        UserAccountRegistration reg = new UserAccountRegistration.Builder()
                .firstName("Firstname")
                .lastName("LastName")
                .password("123")
                .passwordConfirmation("123")
                .email(email)
                .build();
        LOGGER.debug("Registering user: {}", reg);
        // register
        User user = given().spec(spec)
                .log().all()
                .body(reg)
                .post(WEBCONTEXT_PATH + "/api/auth/account")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                // test assertions
                .body("pk", notNullValue())
                .body("emailHash", equalTo(HashUtils.md5Hex(email)))
                // get model
                .extract().as(User.class);

        // ensure user cannot login before confirmation
        //this.getLoggedinContext(email, "123", false);

        // obtain confirmation token
        String confirmationToken = this.getConfirmationToken(user);
        Assert.assertNotNull(confirmationToken);

        // confirm registration
        HashMap<String, String> confirmationRequestBody = new HashMap<>();
        confirmationRequestBody.put("email", email);
        confirmationRequestBody.put("resetPasswordToken", confirmationToken);

        user = given().spec(spec)
                .log().all()
                .body(confirmationRequestBody)
                .put(WEBCONTEXT_PATH + "/api/auth/account")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                // test assertions
                .body("pk", notNullValue())
                .body("emailHash", equalTo(HashUtils.md5Hex(email)))
                .cookie(Constants.REQUEST_AUTHENTICATION_TOKEN_COOKIE_NAME, notNullValue())
                // get model
                .extract().as(User.class);

        // test successful login
        Loggedincontext adminContext = this.getLoggedinContext(email, "123");

    }


}
