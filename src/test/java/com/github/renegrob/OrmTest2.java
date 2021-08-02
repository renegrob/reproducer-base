package com.github.renegrob;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.renegrob.entity.Fruit;
import com.github.renegrob.entity.FruitRepository;
import com.github.renegrob.service.MyService;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@QuarkusTest
@TestProfile(TestOtherProfile.class)
public class OrmTest2 {

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
        assertThat(myService.doSomething()).isEqualTo("something else");
    }

}