package com.github.renegrob.resteasyjackson;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/resteasy-jackson/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

    @RolesAllowed(value = {"Role1", "Role2"})
    @GET
    @Path("{parameter}")
    public Map<String, String> list(@HeaderParam("X-MyHeader") String myHeader, @PathParam("parameter") String parameter) {
        return new HashMap<>();
    }


}
