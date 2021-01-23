package com.github.renegrob;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CacheResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .body("{ \"data\": \"value\" }")
          .when().post("/cache/key1")
          .then()
             .statusCode(204);
    }

}