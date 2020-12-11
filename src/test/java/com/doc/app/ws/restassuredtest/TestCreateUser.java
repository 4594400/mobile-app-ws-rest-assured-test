package com.doc.app.ws.restassuredtest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ARGUMENTS --tests "com.doc.app.ws.restassuredtest.TestCreateUser.testCreateUser" --debug
public class TestCreateUser {
    private final String CONTEXT_PATH = "/mobile-app-ws";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

    }

    @Test
    void testCreateUser(){
        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Vancouver");
        shippingAddress.put("country", "Canada");
        shippingAddress.put("streetName", "123 Street");
        shippingAddress.put("postalCode", "ABCCBA");
        shippingAddress.put("type", "shipping");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("city", "Vancouver");
        billingAddress.put("country", "Canada");
        billingAddress.put("streetName", "123 Street");
        billingAddress.put("postalCode", "ABCCBA");
        billingAddress.put("type", "billing");

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName","Mike");
        userDetails.put("lastName","Ivanov");
        userDetails.put("email","4amazomws@gmail.com");
        userDetails.put("password","123456");
        userDetails.put("addresses",userAddresses);

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);

        Response response = given()
                .contentType("application/json"/*ContentType.JSON*/ )
                .accept("application/json")
                .body(userDetails)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response();

        String userId = response.jsonPath().getString("userId");
        System.out.println("userId = " + userId);
        assertNotNull(userId);
        assertTrue(userId.length() == 30);

        String bodyString = response.body().asString();
        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");

            assertNotNull(addresses);
            assertTrue(addresses.length() == userAddresses.size());

            String addressId = addresses.getJSONObject(0).getString("addressId");
            System.out.println("addressId = " + addressId);
            assertNotNull(addressId);
            assertTrue(addressId.length() == 30);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
