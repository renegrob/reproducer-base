package com.github.renegrob;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.arc.Arc;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class MyIdentityProvider implements IdentityProvider<MyAuthenticationRequest> {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    // does not work
    //@Inject
    //RoutingContext routingContext;

    @Inject
    MyService myService;

    @Inject
    MyTenantResolver tenantResolver;

    @Override
    public Class<MyAuthenticationRequest> getRequestType() {
        return MyAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(MyAuthenticationRequest request,
            AuthenticationRequestContext context) {
        RoutingContext routingContext = request.getAttribute(RoutingContext.class.getName());
        final String tenant = routingContext.request().getHeader("X-Tenant");
        LOG.info("Tenant outside runBlocking: " + tenant);  // works
        // final InjectableContext.ContextState state = Arc.container().requestContext().getState(); // is also not active here, so we cannot take a snapshot
        return context.runBlocking(new Supplier<SecurityIdentity>() {
            @Override
            public SecurityIdentity get() {
                try {
                    final String tenant = routingContext.request().getHeader("X-Tenant");
                    LOG.info("Tenant inside runBlocking: " + tenant);  // works
                    // Vertx.currentContext().put("TENANT", tenant); // does not work
                    Arc.container().requestContext().activate(null);
                    try {
                        tenantResolver.setTenant(tenant);
                        final String result = myService.useTenant();
                        LOG.info("MyService result: " + result);  // works
                    }finally {
                        Arc.container().requestContext().deactivate();
                    }
                    return QuarkusSecurityIdentity.builder().setAnonymous(true).build();
                } catch (SecurityException e) {
                    LOG.debug("Authentication failed", e);
                    throw new AuthenticationFailedException(e);
                }
            }
        });
    }
}
