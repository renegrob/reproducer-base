package com.github.renegrob;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.logmanager.Logger;
import com.github.renegrob.entity.FruitRepository;

import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

@Singleton
@Startup
public class ScheduledTask {

    @Inject
    FruitRepository fruitRepository;

    @Inject
    StatelessRepo statelessRepo;

    @Inject
    SlowBean slowBean;

    @Scheduled(every = "2s", concurrentExecution = SKIP)
    public void task() {
        System.out.println("executing scheduled task...");
        System.out.println(slowBean);
        slowBean.test();
        statelessRepo.upsertApple();
        //fruitRepository.deleteAllByColor("green");
    }
}
