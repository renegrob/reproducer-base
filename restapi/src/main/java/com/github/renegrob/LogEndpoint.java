package com.github.renegrob;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/log")
public class LogEndpoint {

    @POST
    @Path("info")
    public void logInfo(String logMessage) {
        System.out.println(logMessage);
    }

}
