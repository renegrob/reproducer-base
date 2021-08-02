package com.github.renegrob.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyServiceImpl implements MyService {
    @Override
    public String doSomething() {
        return "something";
    }
}
