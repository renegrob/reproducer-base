package com.github.renegrob;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.renegrob.entity.Fruit;
import com.github.renegrob.entity.FruitRepository;

import io.quarkus.test.junit.QuarkusTest;

@Transactional
@QuarkusTest
public class OrmTest {

    @Inject
    FruitRepository fruitRepository;

    @BeforeEach
    public void setUp() {
        Fruit fruit = new Fruit();
        fruit.setColor("green");
        fruit.setName("apple");
        fruitRepository.save(fruit);
    }


    @Test
    public void test() {
        fruitRepository.deleteAllByColor("green");
    }
}