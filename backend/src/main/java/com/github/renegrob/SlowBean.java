package com.github.renegrob;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SlowBean {

    private volatile boolean ok;

    @PostConstruct
    void init() {
        try {
            Thread.sleep(3000);
            ok = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    public void test() {
        System.out.println("test: " + ok);
    }
}
