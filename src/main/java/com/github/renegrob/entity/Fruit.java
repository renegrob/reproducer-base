package com.github.renegrob.entity;

import javax.persistence.Entity;

import com.github.renegrob.INamedEntity;

@Entity
public class Fruit extends BaseEntity implements INamedEntity {

    private String name;

    private String color;


    public Fruit() {
    }

    public Fruit(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Fruit create(String name, String color) {
        Fruit fruit = new Fruit();
        fruit.setName(name);
        fruit.setColor(color);
        return fruit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
