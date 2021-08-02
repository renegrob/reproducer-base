package com.github.renegrob.entity;

import java.util.List;

import javax.inject.Singleton;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@Singleton
public class FruitRepository implements PanacheRepository<Fruit> {

    public List<Fruit> findByColor(String color) {
        return find("color = :color", Parameters.with("color", color)).list();
    }

    public void deleteAllByColor(String color) {
        delete("color = :color", Parameters.with("color", color));
    }
}