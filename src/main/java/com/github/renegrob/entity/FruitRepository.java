package com.github.renegrob.entity;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class FruitRepository implements PanacheRepository<Fruit> {

    public Fruit findByName(String name){
        return find("name", name).firstResult();
    }
}