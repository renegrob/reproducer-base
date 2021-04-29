package com.github.renegrob;

import javax.enterprise.context.RequestScoped;

import io.vertx.core.Vertx;

@RequestScoped
public class MyTenantResolver {

    private String tenant;

    void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String resolveTenant() {
        //return Vertx.currentContext().get("tenant");
        return tenant;
    }
}
