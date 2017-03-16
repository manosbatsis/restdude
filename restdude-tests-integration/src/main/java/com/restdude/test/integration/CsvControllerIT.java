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
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.restdude.auth.userAccount.model.UserAccountRegistration;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserRegistrationCodeBatch;
import com.restdude.domain.users.model.UserRegistrationCodeInfo;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Test(/*singleThreaded = true, */description = "Test dynamic JPA specifications used in default search stack")
@SuppressWarnings("unused")
public class CsvControllerIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvControllerIT.class);

    @Test(description = "Test batch management", priority = 10)
    public void testBatchLyfecycle() throws Exception {

        // login as admin
        Loggedincontext lctx = this.getAdminContext();

        // create and utilize 1 batch
        for (int i = 0; i < 1; i++) {

            // create batch
            //=========================
            UserRegistrationCodeBatch batch = new UserRegistrationCodeBatch();
            batch.setName("Integration Test Batch #" + i);
            batch.setBatchSize(10);
            batch.setDescription("Sample batch description text #" + i);

            // set expiration to six months
            //=========================
            LocalDate sixMonthsFromNow = LocalDate.now().plusMonths(6);
            batch.setExpirationDate(sixMonthsFromNow);

            // save batch
            //=========================
            batch = given().spec(lctx.requestSpec)
                    .log().all()
                    .body(batch)
                    .post(WEBCONTEXT_PATH + "/api/rest/registrationCodeBatches")
                    .then().assertThat()
                    .log().all()
                    .statusCode(201)
                    .body("pk", notNullValue())
                    .log().all()
                    .extract().as(UserRegistrationCodeBatch.class);

            // get batch codes as CSV
            //=========================
            RequestSpecification reqSpec = this.getRequestSpec(lctx.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, "text/csv", "text/csv");
            String csv = RestAssured.given().spec(reqSpec)
                    .log().all().get(WEBCONTEXT_PATH + "/api/rest/registrationCodeBatches/" + batch.getPk() + "/csv").then().log().all().statusCode(200).extract().response().getBody().print();
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(UserRegistrationCodeInfo.class).withHeader();
            MappingIterator<Map<String, String>> it = mapper.readerFor(Map.class)
                    .with(schema)
                    .readValues(csv);

            // Register a User for each code
            //==============================
            int j = 0;
            while (it.hasNext()) {
                Map<String, String> rowAsMap = it.next();
                // access by column name, as defined in the header row...

                RequestSpecification spec = this.getRequestSpec(null);
                User user = given().spec(spec)
                        .log().all()
                        .body(new UserAccountRegistration.Builder()
                                .firstName("Firstname")
                                .lastName("LastName")
                                .email("BetaCodeBatch_" + i + "_" + (j++) + "@" + this.getClass().getSimpleName() + ".com")
                                .registrationCode(rowAsMap.get("pk"))
                                .build())
                        .post(WEBCONTEXT_PATH + "/api/auth/account")
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(201)
                        // test assertions
                        .body("pk", notNullValue())
                        // get model
                        .extract().as(User.class);
            }


        }
    }

    @Test(description = "Test CSV output", priority = 20)
    public void testBatchCsv() throws Exception {
        LOGGER.debug("Test CSV output");
        // login as admin
        Loggedincontext lctx = this.getLoggedinContext("admin", "admin");

        // get initial data batch pk
        JsonNode batches = given().spec(lctx.requestSpec)
                .log().all()
                .get(WEBCONTEXT_PATH + "/api/rest/registrationCodeBatches")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("content[0].pk", notNullValue())
                .extract().as(JsonNode.class);
        String id = batches.get("content").get(0).get("pk").asText();

        // export code batch to CSV
        RequestSpecification reqSpec = this.getRequestSpec(lctx.ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, "text/csv", "text/csv");
        String csv = RestAssured.given().spec(reqSpec)
                .log().all().get(WEBCONTEXT_PATH + "/api/rest/registrationCodeBatches/" + id + "/csv").then().log().all().statusCode(200).extract().response().getBody().print();

        // verify multiple lines
        Assert.assertTrue(csv.split("\\r?\\n").length > 1);
    }

}
