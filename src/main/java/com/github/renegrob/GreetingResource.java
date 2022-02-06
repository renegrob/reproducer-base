package com.github.renegrob;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/hello-resteasy")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Hello RESTEasy", description = "*italic* **bold** and normal text.")
    public String hello() {
        return "Hello RESTEasy";
    }
}
