package com.github.renegrob;

import java.net.ServerSocket;
import java.net.URL;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(value = GreetingResourceTest.PortBlockerExtension.class)
@QuarkusTest
public class GreetingResourceTest {

    @Inject
    Logger log;

    @TestHTTPResource
    URL url;

    @Test
    public void testHelloEndpoint() {
        log.info(url);
        given()
          .when().get("/hello-resteasy")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

    static class PortBlockerExtension implements Extension, BeforeAllCallback, AfterAllCallback {

        private ServerSocket serverSocket;

        @Override
        public void afterAll(ExtensionContext context) throws Exception {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        }

        @Override
        public void beforeAll(ExtensionContext context) throws Exception {
            System.setProperty("quarkus.http.test-port", "0");
            serverSocket = new ServerSocket(8081);
        }
    }
}
