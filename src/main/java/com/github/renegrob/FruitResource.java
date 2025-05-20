package com.github.renegrob;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.github.renegrob.entity.Fruit;
import com.github.renegrob.entity.FruitRepository;

@Path("/fruit")
public class FruitResource {

    @Inject
    FruitRepository repository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Fruit byName(@PathParam("name") String name) {
        return repository.findByName(name);
    }

}