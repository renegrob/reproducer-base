package com.github.renegrob.service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
public class MyServiceOtherImpl implements MyService {
    @Override
    public String doSomething() {
        return "something else";
    }
}
