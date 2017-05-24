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
import com.restdude.hypermedia.jsonapi.JsonApiModelResourceDocument;
import com.restdude.hypermedia.util.JsonApiModelBasedDocumentBuilder;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Test(/*singleThreaded = true, */description = "Test JSON API 1.1 compliance")
@SuppressWarnings("unused")
public class JsonApiIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonApiIT.class);



    @Test(description = "Test invalid credentials", priority = 30)
    public void testGetSingleResourceDocument() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext lctx = this.getAdminContext();
        RequestSpecification requestSpec = getRequestSpec(lctx.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8);


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
    @Test(description = "Test search filters etc.", priority = 40 )
    public void testSearch() throws Exception {

        String[] mimes = {
                "application/vnd.api+json",
                "application/vnd.api+json;charset=UTF-8",
                "application/vnd.api+json;version=1;charset=UTF-8"};
        for(String mime : mimes){
            this.search(mime);
        }

    }
    private void search(String contentType) throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext lctx = this.getAdminContext();
        RequestSpecification requestSpec = getRequestSpec(lctx.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8);


        // get a country document
        Response rs = given().spec(requestSpec)
                .log().all()
                .param("parent.pk", "EU")
                .param("name", "Greece")
                .get(WEBCONTEXT_PATH + "/api/rest/countries");

        // validate response
        rs.then().log().all().assertThat()
                .statusCode(200)
                .body("data[0].id", equalTo("GR"))
                .body("data[0].attributes.name", equalTo("Greece"));

    }

    @Test(description = "Test creating an entity as a JSON API Document/Resource", priority = 50 )
    public void testCreate() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext lctx = this.getAdminContext();
        RequestSpecification requestSpec = getRequestSpec(lctx.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8);

        Role role = new Role("ROLE_TEST", "Test role");
        JsonApiModelResourceDocument<Role, String> document = new JsonApiModelBasedDocumentBuilder<Role, String>("roles")
                .withData(role)
                .buildModelDocument();

        // get a country document
        Response rs = given().spec(requestSpec)
                .log().all()
                .body(document)
                .post(WEBCONTEXT_PATH + "/api/rest/roles");

        // validate response
        rs.then().log().all().assertThat()
                .statusCode(201)
                .body("data.id", notNullValue())
                .body("data.attributes.name", equalTo("ROLE_TEST"));

        // search for persisted
        // get a country document
        rs = given().spec(requestSpec)
                .log().all()
                .param("name", "ROLE_TEST")
                .get(WEBCONTEXT_PATH + "/api/rest/roles");

        // validate response
        rs.then().log().all().assertThat()
                .statusCode(200)
                .body("data[0].id", notNullValue())
                .body("data[0].attributes.name", equalTo("ROLE_TEST"));

    }

    //TODO
    /*
    @Test(description = "Test search filters etc.", priority = 40 )
    public void testSearchDialects() throws Exception {

        // --------------------------------
        // Login
        // --------------------------------
        Loggedincontext lctx = this.getAdminContext();
        RequestSpecification requestSpec = getRequestSpec(lctx.ssoToken, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8, AbstractControllerIT.MIME_APPLICATION_VND_API_JSON_UTF8);


        // get a country document
        Response rs = given().spec(requestSpec)
                .log().all()
                .param("filter[country.source][prefix]", "G")
                .param("filter[country.callingCode][not][postfix]", "9")
                .get(WEBCONTEXT_PATH + "/api/rest/countries");

        // validate response
        rs.then().log().all().assertThat()
                .statusCode(200)
                .body("data.id", equalTo("GR"))
                .body("data.attributes.source", equalTo("Greece"));

    }
    */


}
