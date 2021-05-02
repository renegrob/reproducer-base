package com.github.renegrob;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.vertx.ext.web.RoutingContext;

@RequestScoped
public class MyTenantResolver {

    @Inject
    RoutingContext routingContext;

    public String resolveTenant() {
        return routingContext.request().getHeader("X-Tenant");
    }
}
