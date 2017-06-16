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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import com.fasterxml.jackson.databind.JsonNode;
import com.restdude.test.AbstractControllerIT;
import com.restdude.util.Constants;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
@Test(/*singleThreaded = true, */description = "Test relationship endpoints")
@SuppressWarnings("unused")
public class SleepIT extends AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepIT.class);

    Loggedincontext adminLoginContext;
    RequestSpecification adminRequestSpec;

    @BeforeClass
    public void setup() {
        super.setup();
    }

    @Test(description = "Sleep for 10 seconds", priority = 1)
    public void testSleep() throws Exception {
        log.debug("Sleeping....");
        Thread.sleep(10000);
        log.debug("Slept");
    }

}
