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
package com.restdude.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.restdude.domain.misc.model.Host;
import com.restdude.domain.users.model.User;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.Constants;
import com.restdude.websocket.Destinations;
import com.restdude.websocket.client.DefaultStompSessionHandler;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testng.annotations.BeforeClass;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.*;

/**
 * Base class for rest-assured based controller integration testing
 */
@SuppressWarnings("unused")
public class AbstractControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractControllerIT.class);

    protected static final String MIME_APPLICATION_JSON_UTF8 = "application/json; charset=UTF-8";
    protected static final String MIME_APPLICATION_VND_API_JSON_UTF8 = "application/vnd.api+json; charset=UTF-8";

    protected static final Configuration CONFIG = ConfigurationFactory.getConfiguration();

    protected String WEBSOCKET_URI;
    protected String WEBCONTEXT_PATH;
    protected String TEST_EMAIL_DOMAIN;
    private   Loggedincontext adminContext;


    @BeforeClass
    public void setup() {

        // log request/response in errors
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        Configuration config = ConfigurationFactory.getConfiguration();
        LOGGER.info("setup, restdude.contextPath: {}", config.getString(ConfigurationFactory.APP_CONTEXT_PATH));
        this.WEBCONTEXT_PATH = config.getString(ConfigurationFactory.APP_CONTEXT_PATH, "/restdude");
        this.TEST_EMAIL_DOMAIN = config.getString(ConfigurationFactory.TEST_EMAIL_DOMAIN, "restdude.com");

        // pickup from the jetty port
        RestAssured.port = CONFIG.getInt("jetty.http.port", 8080);
        this.WEBSOCKET_URI = new StringBuffer("ws://localhost:")
                .append(RestAssured.port)
                .append(WEBCONTEXT_PATH)
                .append("/ws")
                .toString();
        LOGGER.info("setup, websocket URL {}", this.WEBSOCKET_URI);


        // configure our object mapper
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                // config object mapper
                new ObjectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
                    @Override
                    public ObjectMapper create(Class aClass, String s) {
                        ObjectMapper objectMapper = new ObjectMapper()
                                .registerModule(new ParameterNamesModule())
                                .registerModule(new Jdk8Module())
                                .registerModule(new JavaTimeModule());

                        // Disable features
                        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
                        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
                        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

                        // enable features
                        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

                        return objectMapper;
                    }
                }));


        this.adminContext = this.getLoggedinContext("admin", "admin");

    }



    public Loggedincontext getAdminContext() {
        return adminContext;
    }

    protected static Configuration getConfig() {
        return CONFIG;
    }

    protected StompSession getStompSession(String url, Loggedincontext loginContext) {
        return getStompSession(url, loginContext, new DefaultStompSessionHandler());
    }

    protected StompSession getStompSession(String url, Loggedincontext loginContext, StompSessionHandler sessionHandler) {
        return getStompSession(url, loginContext, sessionHandler, null, null);

    }

    protected StompSession getStompSession(String url, Loggedincontext loginContext, StompSessionHandler sessionHandler,
                                           WebSocketHttpHeaders handshakeHeaders, StompHeaders connectHeaders) {

        StompSession ownerSession = null;
        try{
            LOGGER.debug("Creating STOMP session for user {}:{}, url: {}", loginContext.userId, loginContext.ssoToken, url);
            if (sessionHandler == null) {
                sessionHandler = new DefaultStompSessionHandler();
            }

            // add auth
            if (handshakeHeaders == null) {
                handshakeHeaders = new WebSocketHttpHeaders();
            }
            handshakeHeaders.add("Authorization", "Basic " + loginContext.ssoToken);


            ownerSession = getWebSocketStompClient().connect(url, handshakeHeaders, connectHeaders, sessionHandler).get(5, SECONDS);
        } catch (Exception e) {
            LOGGER.error("Failed obtainint a STOMP session", e);
            throw new RuntimeException(e);
        }

        return ownerSession;
    }


    protected WebSocketStompClient getWebSocketStompClient() {
        // setup websocket
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        // support JSON messages
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    protected BlockingQueue<JsonNode> getActivityQueue(StompSession stompSession) {
        BlockingQueue<JsonNode> queue = new LinkedBlockingDeque<JsonNode>();
        StompSession.Subscription subscription = stompSession.subscribe("/user" + Destinations.USERQUEUE_UPDATES_ACTIVITY,
                new DefaultStompFrameHandler<JsonNode>(stompSession, JsonNode.class, queue));
        return queue;
    }

    protected BlockingQueue<JsonNode> getStatusChangeQueue(StompSession stompSession) {
        BlockingQueue<JsonNode> queue = new LinkedBlockingDeque<JsonNode>();
        StompSession.Subscription subscription = stompSession.subscribe("/user" + Destinations.USERQUEUE_UPDATES_STATE,
                new DefaultStompFrameHandler<JsonNode>(stompSession, JsonNode.class, queue));
        return queue;
    }

    protected String getConfirmationToken(User user) {
        LOGGER.debug("getConfirmationToken for user: {}, logging in as admin...", user);
        Loggedincontext adminLoginContext = this.getLoggedinContext("admin", "admin");

        JsonNode credentialsNode = given().spec(adminLoginContext.requestSpec)
                .log().all()
                .param("user", user.getId())
                .get(WEBCONTEXT_PATH + "/api/rest/userCredentials")
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(JsonNode.class);
        String token = credentialsNode.get("content").get(0).get("resetPasswordToken").asText();
        LOGGER.debug("getConfirmationToken returning credentials token: {}", token);
        return token;
    }

    /**
     * Login using the given credentials and return the JWT token
     *
     * @param username
     * @param password
     * @return
     */
    protected Loggedincontext getJwtLoggedinContext(String username, String password) {
        return this.getJwtLoggedinContext(username, password, false, false);
    }

    /**
     * Login using the given credentials and return the Single Sign-On token
     *
     * @param username
     * @param password
     * @return
     */
    protected Loggedincontext getJwtLoggedinContext(String username, String password, boolean assertFailed, boolean debug) {
        Loggedincontext lctx = new Loggedincontext();


        // attempt login and test for a proper result
        Response rs = getJwtLoginResponse(username, password, debug);

        String accessJwt = rs.getCookie("access_token");

        // Get result cookie and user id
        lctx.jwtAccessToken = rs.getCookie(Constants.JWT_AUTHENTICATION_TOKEN_COOKIE_NAME);
        lctx.userId = rs.jsonPath().getString("id");

        RequestSpecification requestSpec = getJwtRequestSpec(assertFailed ? null : lctx.jwtAccessToken);
        lctx.requestSpec = requestSpec;

        return lctx;
    }

    protected Response getJwtLoginResponse(String username, String password, boolean debug) {
        Response rs;

        // create a login request body
        Map<String, String> loginSubmission = new HashMap<String, String>();
        loginSubmission.put("username", username);
        loginSubmission.put("password", password);

        RequestSpecification requestSpecification = given().accept(MIME_APPLICATION_JSON_UTF8).contentType(MIME_APPLICATION_JSON_UTF8).body(loginSubmission);

        if(debug){
            rs = requestSpecification.log().all().when()
                    .post(WEBCONTEXT_PATH + "/api/auth/jwt/access");
            rs.then().log().all().assertThat().statusCode(201);
        }
        else{
            rs = requestSpecification.when()
                    .post(WEBCONTEXT_PATH + "/api/auth/jwt/access");
            rs.then().assertThat().statusCode(201);
        }
        return rs;
    }

    /**
     * Login using the given credentials and return the Single Sign-On token
     *
     * @param username
     * @param password
     * @return
     */
    protected Loggedincontext getLoggedinContext(String username, String password) {
        return this.getLoggedinContext(username, password, false);
    }

    /**
     * Login using the given credentials and return the Single Sign-On token
     *
     * @param username
     * @param password
     * @return
     */
    protected Loggedincontext getLoggedinContext(String username, String password, boolean assertFailed) {
        Loggedincontext lctx = new Loggedincontext();
        // create a login request body
        Map<String, String> loginSubmission = new HashMap<String, String>();
        loginSubmission.put("username", username);
        loginSubmission.put("password", password);

        // attempt login and test for a proper result
        Response rs = given().accept(MIME_APPLICATION_JSON_UTF8).contentType(MIME_APPLICATION_JSON_UTF8).body(loginSubmission).log().all().when()
                .post(WEBCONTEXT_PATH + "/api/auth/userDetails");

        // validate login
        rs.then().log().all()
                .assertThat()
                .statusCode(200)
                .cookie(Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, assertFailed
                        ? anyOf(equalTo("invalid"), nullValue())
                        : allOf(not("invalid"), notNullValue()))
                .content("id", assertFailed ? nullValue() : notNullValue());

        // Get result cookie and user id
        lctx.ssoToken = rs.getCookie(Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME);
        lctx.userId = rs.jsonPath().getString("id");

        RequestSpecification requestSpec = getRequestSpec(assertFailed ? null : lctx.ssoToken);
        lctx.requestSpec = requestSpec;

        return lctx;
    }

    protected RequestSpecification getRequestSpec(String ssoToken) {
        return this.getRequestSpec(ssoToken, Constants.BASIC_AUTHENTICATION_TOKEN_COOKIE_NAME, MIME_APPLICATION_JSON_UTF8, MIME_APPLICATION_JSON_UTF8);
    }

    protected RequestSpecification getJwtRequestSpec(String jwrAccessToken) {
        return this.getRequestSpec(jwrAccessToken, Constants.JWT_AUTHENTICATION_TOKEN_COOKIE_NAME, MIME_APPLICATION_JSON_UTF8, MIME_APPLICATION_JSON_UTF8);
    }

    protected RequestSpecification getRequestSpec(String cookieValue, String cookieName, String accept, String contentType) {
        // extend the global spec we have already set to add the SSO token
        RequestSpecification requestSpec;
        RequestSpecBuilder b = new RequestSpecBuilder().setAccept(accept).setContentType(contentType);
        if (cookieValue != null) {
            b.addCookie(cookieName, cookieValue);
        }
        requestSpec = b.build();
        return requestSpec;
    }

    protected User getUserByUsernameOrEmail(RequestSpecification requestSpec, String userNameOrEmail) {
        return given().spec(requestSpec).log().all().get(WEBCONTEXT_PATH + "/api/rest/users/byUserNameOrEmail/{userNameOrEmail}", userNameOrEmail).then().log().all().extract().as(User.class);
    }

    protected Host getRandomHost(RequestSpecification someRequestSpec) {
        // obtain a random Host id
        String id = given().spec(someRequestSpec)
                .get(WEBCONTEXT_PATH + "/api/rest/hosts").then()
                .assertThat().body("content[0].id", notNullValue()).extract().path("content[0].id");

        Host host = new Host();
        host.setId(id);
        return host;
    }

    public static class Loggedincontext {
        public String userId;
        public String ssoToken;
        public RequestSpecification requestSpec;
        public String jwtAccessToken;
    }

    /**
     * Handle stream connection info updates by adding them to a local queue storage
     */
    public static class DefaultInitialDataStompFrameHandler<T> implements StompFrameHandler {

        private Class datumClass;
        private StompSession session;
        public List<T> initialData;

        private DefaultInitialDataStompFrameHandler() {
        }

        public DefaultInitialDataStompFrameHandler(StompSession session, Class datumClass) {
            this.session = session;
            this.datumClass = datumClass;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            try {
                return this.getClass().getField("initialData").getType();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            LOGGER.info("handleFrame: payload: " + payload);
            this.initialData = (List<T>) payload;
        }

    }

    /**
     * Handle stream connection info updates by adding them to a local queue storage
     */
    public static class DefaultStompFrameHandler<T> implements StompFrameHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStompFrameHandler.class);

        private Class messageClazz;
        private StompSession session;
        private BlockingQueue<T> blockingQueue;
        private String name = "";

        private DefaultStompFrameHandler() {
        }

        public DefaultStompFrameHandler(StompSession session, Class messageClazz, BlockingQueue<T> blockingQueue) {
            this(session, messageClazz, blockingQueue, messageClazz.getSimpleName());
        }

        public DefaultStompFrameHandler(StompSession session, Class messageClazz, BlockingQueue<T> blockingQueue, String name) {
            this.session = session;
            this.blockingQueue = blockingQueue;
            this.messageClazz = messageClazz;
            this.name = name;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return this.messageClazz;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            LOGGER.debug("{} handleFrame: {}, headers: {}, payload: {}", name, messageClazz.getSimpleName(), headers, payload);
            this.blockingQueue.offer((T) payload);
        }

    }
}
