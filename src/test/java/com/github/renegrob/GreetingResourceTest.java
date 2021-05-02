package com.github.renegrob;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.ext.web.RoutingContext;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given().auth().basic("john.doe", "s3cr3t").header("X-Tenant", "Quarkus-Tenant")
          .when().get("/hello-resteasy")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

}