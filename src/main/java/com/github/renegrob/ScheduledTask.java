package com.github.renegrob;

import javax.inject.Inject;

import org.jboss.logmanager.Logger;
import com.github.renegrob.entity.FruitRepository;

import io.quarkus.scheduler.Scheduled;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

public class ScheduledTask {

    @Inject
    FruitRepository fruitRepository;

    @Inject
    StatelessRepo statelessRepo;

    @Scheduled(every = "2m", concurrentExecution = SKIP)
    public void task() {
        System.out.println("executing scheduled task...");
        statelessRepo.upsertApple();
        //fruitRepository.deleteAllByColor("green");
    }
}
