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
import com.github.renegrob.service.MyService;

import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@QuarkusTest
public class OrmTest {

    @Inject
    MyService myService;

    @Inject
    FruitRepository fruitRepository;

    @BeforeEach
    public void setUp() {
        Fruit fruit = new Fruit();
        fruit.setColor("green");
        fruit.setName("apple");
        fruitRepository.persist(fruit);
    }


    @Test
    public void test() {
        fruitRepository.deleteAllByColor("green");
    }

    @Test
    public void testService() {
        assertThat(myService.doSomething()).isEqualTo("something");
    }

}