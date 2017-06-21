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
import com.restdude.auth.userAccount.model.UserAccountRegistration;
import com.restdude.domain.cases.model.SpaceApp;
import com.restdude.domain.error.model.ClientError;
import com.restdude.domain.error.model.ErrorLog;
import com.restdude.domain.error.model.ErrorsApplication;
import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.repository.ClientErrorRepository;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.users.model.User;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Test(/*singleThreaded = true, */description = "Test REST error responses")
@SuppressWarnings("unused")
public class RestErrorsIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorsIT.class);

    @Test(description = "Test duplicate username subscription attempt", priority = 10)
    public void testDuplicateUsername() throws Exception {
        LOGGER.debug("Test duplicate username subscription attempt");
        RequestSpecification spec = this.getRequestSpec(null);
        SystemError error = given().spec(spec)
                .log().all()
                .body(new UserAccountRegistration.Builder()
                        .username("operator")
                        .firstName("Firstname")
                        .lastName("LastName")
                        .email("testDuplicateUsername@" + this.getClass().getSimpleName() + ".com")
                        .build())
                .post(WEBCONTEXT_PATH + "/api/auth/account")
                .then()
                .log().all()
                .assertThat()
                // test assertions
                .statusCode(400)
                .body("httpStatusCode", is(400))
                // get model
                .extract().as(SystemError.class);

    }

    @Test(description = "Test duplicate email subscription attempt", priority = 20)
    public void testDuplicateEmail() throws Exception {

        RequestSpecification spec = this.getRequestSpec(null);
        SystemError error = given().spec(spec)
                .log().all()
                .body(new UserAccountRegistration.Builder()
                        .email("operator@" + TEST_EMAIL_DOMAIN)
                        .build())
                .post(WEBCONTEXT_PATH + "/api/auth/account")
                .then()
                .log().all()
                .assertThat()
                // test assertions
                .statusCode(400)
                .body("httpStatusCode", is(400))
                // get model
                .extract().as(SystemError.class);

    }

    @Test(description = "Test invalid credentials", priority = 30)
    public void testInvalidCredentials() throws Exception {

        Loggedincontext lctx = new Loggedincontext();
        // create a login request body
        Map<String, String> loginSubmission = new HashMap<String, String>();
        loginSubmission.put("username", "admin");
        loginSubmission.put("password", "invalid");

        // attempt login and test for a proper result
        Response rs = given().accept(MIME_APPLICATION_JSON_UTF8).contentType(MIME_APPLICATION_JSON_UTF8).body(loginSubmission).when()
                .post(WEBCONTEXT_PATH + "/api/auth/userDetails");

        // validate login
        rs.then().log().all().assertThat()
                .statusCode(401)
                .body("httpStatusCode", is(401));

        Assert.assertFalse(StringUtils.isNotBlank(rs.getCookie(Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME)));

    }

    @Test(description = "Test not remembered", priority = 40)
    public void testNotRemembered() throws Exception {
        LOGGER.info("testNotRemembered");
        Response rs = given().spec(getRequestSpec("invalid")).log().all()
                .get(WEBCONTEXT_PATH + "/api/auth/userDetails");
        rs.then().log().all().assertThat()
                // test assertions
                .statusCode(401)
                .body("httpStatusCode", is(401));

        Assert.assertFalse(StringUtils.isNotBlank(rs.getCookie(Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME)));

    }

    @Test(description = "Test not found", priority = 50)
    public void testNotFound() throws Exception {

        Loggedincontext adminLoginContext = this.getAdminContext();
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;
        // select user
        SystemError error = given().spec(adminRequestSpec)
                .get(WEBCONTEXT_PATH + "/api/rest/users/invalid")
                .then().assertThat()
                // test assertions
                .statusCode(404)
                .body("httpStatusCode", is(404))
                .extract().as(SystemError.class);
    }

    @Test(description = "Test invalid target ", priority = 60)
    public void testInvalidTarget() throws Exception {

        Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;
        // select user
        SystemError error = given().spec(adminRequestSpec)
                .log().all()
                .body(new Friendship(new User(adminLoginContext.userId), new User("3c1cd4dc-05fb-49ef-b929-f08d0f0b7c73")))
                .post(WEBCONTEXT_PATH + "/api/rest/" + Friendship.API_PATH)
                .then()
                .log().all()
                .assertThat()
                // test assertions
                .statusCode(anyOf(is(HttpStatus.SC_INTERNAL_SERVER_ERROR), is(HttpStatus.SC_BAD_REQUEST)))
                .body("httpStatusCode", anyOf(is(HttpStatus.SC_INTERNAL_SERVER_ERROR), is(HttpStatus.SC_BAD_REQUEST)))
                .extract().as(SystemError.class);
    }

    @Test(description = "Test invalid target ", priority = 70)
    public void testSearchSystemErrors() throws Exception {

        Loggedincontext adminLoginContext = this.getLoggedinContext("operator", "operator");
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;
        // select user
        JsonNode errorsPage = given().spec(adminRequestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/" + SystemError.API_PATH)
                .then().log().all().assertThat()
                // test assertions
                .statusCode(200)
                .body("content[0].title", notNullValue())
                .body("content[1].title", notNullValue())
                .extract().as(JsonNode.class);
    }

    @Test(description = "Test case index", priority = 75)
    public void testCaseIndexAndComments() throws Exception {

        Loggedincontext adminLoginContext = this.getLoggedinContext("operator", "operator");
        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;
        // select user
        JsonNode errorsPage = given().spec(adminRequestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/" + SystemError.API_PATH)
                .then().log().all().assertThat()
                // test assertions
                .statusCode(200)
                .body("content[0].entryIndex", notNullValue())
                .body("content[1].title", notNullValue())
                .extract().as(JsonNode.class);
        String errorId = errorsPage.get("content").get(0).get("id").asText();

        LOGGER.debug("testCaseIndexAndComments, found error case id: {}", errorId);
        JsonNode comments = given().spec(adminRequestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/" + SystemError.API_PATH + "/" + errorId + "/comments")
                .then().log().all().assertThat()
                // test assertions
                .statusCode(200)
                .extract().as(JsonNode.class);

        Assert.assertTrue(comments.isArray());
        if(comments.size() > 0){

            Assert.assertNotNull(comments.get(0).get("detail").asText());
            Assert.assertNotNull(comments.get(0).get("createdDate").asText());
            Assert.assertNotNull(comments.get(0).get("author").get("username").asText());
        }
    }

    @Test(description = "Test client error submission", priority = 80)
    public void testClientErrorSubmission() throws Exception {

        Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");

        RequestSpecification adminRequestSpec = adminLoginContext.requestSpec;
        // get Web UI client errors app
        JsonNode clientApps = given().spec(adminRequestSpec)
                .param("name", ClientErrorRepository.ERRORS_WORKFLOW_NAME)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/" + SpaceApp.API_PATH_FRAGMENT)
                .then().log().all().assertThat()
                // test assertions
                .statusCode(200)
                .body("content[0].id", notNullValue())
                .extract().as(JsonNode.class);

        String appId = clientApps.get("content").get(0).get("id").asText();
        ErrorsApplication clientErrorsApp = new ErrorsApplication();
        clientErrorsApp.setId(appId);

        for (int i = 0; i < 1; i++) {
            String detail = "ClientError #" + i + " created by " + this.getClass().getName();
            ClientError error = new ClientError();
            error.setParent(clientErrorsApp);
            ErrorLog log = new ErrorLog();
            log.setRootCauseMessage("Section 1.10.32 of \"de Finibus Bonorum et Malorum\", written by Cicero in 45 BC");
            log.setStacktrace("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur");

            error.setTitle("Client Error #" + i + "!");
            error.setDetail(detail);
            error.setErrorLog(log);

            error = given().spec(adminRequestSpec)
                    .log().all()
                    .body(error)
                    .post(WEBCONTEXT_PATH + "/api/rest/" + ClientError.API_PATH)
                    .then().log().all().assertThat()
                    // test assertions
                    .statusCode(201)
                    .body("title", notNullValue())
                    .body("detail", equalTo(detail))
                    .body("createdBy.id", equalTo(adminLoginContext.userId))
                    .extract().as(ClientError.class);

        }
    }

}
