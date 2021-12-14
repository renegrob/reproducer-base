package com.github.renegrob;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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