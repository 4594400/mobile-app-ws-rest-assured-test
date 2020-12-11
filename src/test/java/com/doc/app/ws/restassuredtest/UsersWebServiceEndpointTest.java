package com.doc.app.ws.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsersWebServiceEndpointTest {
    private final String CONTEXT_PATH = "/mobile-app-ws";
    private final String EMAIL_ADDRESS = "4amazomws@gmail.com";
    private final String JSON = "application/json";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

    }

    /**
     * Before run test you should create user and verify EMAIL_VERIFICATION_STATUS  in DB
     * 1. update  USERS set EMAIL_VERIFICATION_STATUS  = 'true' where id = 1;
     * 2. go to URL in your email (verification token)
     */
    @Test
    void testUserLogin(){
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email",EMAIL_ADDRESS);
        loginDetails.put("password", "123456");

        Response response = given().contentType(JSON)
                .accept(JSON)
                .body(loginDetails)
                .when().post(CONTEXT_PATH + "/users/login")
                .then().statusCode(200)
                .extract().response();

        System.out.println("response header = " + response.getHeaders().toString());

        String authorizationHeader = response.header("Authorization");
        String userId = response.header("UserId");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }


}
