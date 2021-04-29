package com.github.renegrob;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyService {

    @Inject
    MyTenantResolver tenantResolver;

    public String useTenant() {
        return "TENANT IS <" + tenantResolver.resolveTenant() + ">";
    }
}
