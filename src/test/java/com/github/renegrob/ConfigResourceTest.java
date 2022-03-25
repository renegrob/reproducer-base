package com.github.renegrob;

import java.util.Map;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ConfigResourceTest {

    @Test
    public void testConfigAppNmae() {
        given()
                .when().get("/config")
                .then()
                .statusCode(200)
                .body("appName", equalTo("My App"));
    }

    @Test
    public void testConfigClient() {
        given()
                .when().get("/config")
                .then()
                .statusCode(200)
                .body("client.baseURI", equalTo("http://localhost:8080"))
                .body("client.keystore.path", equalTo("file:///home/quarkus/keystores/my-keystore.p12"))
                .body("client.keystore.password", equalTo("p@ssw0rd"));
    }

    @Test
    public void testConfigMyClient() {
        given()
                .when().get("/config")
                .then()
                .statusCode(200)
                .body("appName", equalTo("My App"))
                .body("myClient.baseURI", equalTo("http://localhost:8080"))
                .body("myClient.keystore.path", equalTo("file:///home/quarkus/keystores/my-keystore.p12"))
                .body("myClient.keystore.password", equalTo("p@ssw0rd"));
    }

}
