package com.github.renegrob;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.renegrob.entity.FruitRepository;

@Transactional
@Path("/orm-test")
public class OrmTestResource {

    @Inject
    FruitRepository fruitRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        fruitRepository.deleteAllByColor("green");
        return "OK";
    }
}