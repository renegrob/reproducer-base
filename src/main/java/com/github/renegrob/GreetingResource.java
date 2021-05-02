package com.github.renegrob;

import java.lang.invoke.MethodHandles;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.vertx.ext.web.RoutingContext;

@Path("/hello-resteasy")
public class GreetingResource {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    @Inject
    RoutingContext routingContext;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        final String tenant = routingContext.request().getHeader("X-Tenant");
        LOG.info("Tenant outside runBlocking: " + tenant);  // works

        return "Hello RESTEasy";
    }
}