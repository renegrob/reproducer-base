package com.github.renegrob;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/hello-resteasy")
public class GreetingResource {

    /*
      components = @Components(
    parameters = {
      @Parameter(name = "application", in = QUERY, description = "Name of the application", required = true),
      @Parameter(name = "address", in = PATH, description = "address", example = ADDRESS_EXAMPLE, required = true)
    },
    responses = {
      @APIResponse(name = "NotFound", responseCode = HTTP_NOT_FOUND, description = "Account not found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponseTO.class,
          example = "{ \"code\": 404, \"message\": \"Account not found\" }"))),
      @APIResponse(name = "AlreadyExists", responseCode = HTTP_CONFLICT, description = "An account with this Key Name already exists",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponseTO.class,
          example = "{ \"code\": 409, \"message\": \"An account with this Key Name already exists\" }"))),
      @APIResponse(name = "InternalError", responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponseTO.class,
          example = "{ \"code\": 500, \"message\": \"Internal server error\" }")))
    }
  )

    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JacksonResource.Quark.class))),
            @APIResponse(ref = "NotFound")})
*/
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Hello RESTEasy", description = "*italic* **bold** and normal text.")
    @MyRequirePrivileges({"Test3", "Test4"})
    public String hello() {
        return "Hello RESTEasy";
    }
}
