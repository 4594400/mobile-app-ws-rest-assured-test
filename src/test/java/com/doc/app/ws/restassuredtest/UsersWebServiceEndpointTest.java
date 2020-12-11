package com.doc.app.ws.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * For ordering JUnit4: @FixMethodOrder(MethodSorters.NAME_ASCENDING)
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersWebServiceEndpointTest {
    private final String CONTEXT_PATH = "/mobile-app-ws";
    private final String EMAIL_ADDRESS = "4amazomws@gmail.com";
    private final String JSON = "application/json";


    private static String authorizationHeader;
    private static String userId;

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
    @Order(1)
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

        //System.out.println("response header = " + response.getHeaders().toString());

        authorizationHeader = response.header("Authorization");
        userId = response.header("UserId");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }

    @Test
    @Order(2)
    void testGetUserDetails(){
        Response response = given()
                .pathParam("id", userId)
                .header("Authorization", authorizationHeader)
                .accept(JSON)
                .when().get(CONTEXT_PATH + "/users/{id}")
                .then().statusCode(200)
                .contentType(JSON)
                .extract().response();

        String userPublicId = response.jsonPath().getString("userId");
        String userEmail = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");
        List<Map<String, String>> addresses = response.jsonPath().getList("addresses");
        String addressId = addresses.get(0).get("addressId");

        assertNotNull(userPublicId);
        assertNotNull(userEmail);
        assertNotNull(firstName);
        assertNotNull(lastName);
        assertEquals(EMAIL_ADDRESS, userEmail);
        assertTrue(addresses.size() == 2);
        assertTrue(addressId.length() == 30);

    }


}
