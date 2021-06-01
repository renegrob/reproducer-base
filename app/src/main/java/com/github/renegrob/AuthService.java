package com.github.renegrob;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
//import io.quarkus.smallrye.jwt.runtime.auth.JWTAuthMechanism;

@Singleton
@Startup
public class AuthService {
/*
    @Inject
    JWTAuthMechanism jwtAuthenticationMechanism;

    @PostConstruct
    void init() {
        System.out.println(jwtAuthenticationMechanism.getClass().getName());
    }

 */
}
