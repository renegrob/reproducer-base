package com.github.renegrob;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableContext;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class MyIdentityProvider implements IdentityProvider<MyAuthenticationRequest> {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    MyService myService;

    @Override
    public Class<MyAuthenticationRequest> getRequestType() {
        return MyAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(MyAuthenticationRequest request,
            AuthenticationRequestContext context) {
        context = fixedAuthenticationRequestContext(context);
        return context.runBlocking(authenticateBlocking());
    }

    Supplier<SecurityIdentity> authenticateBlocking() {
        return new Supplier<SecurityIdentity>() {
            @Override
            public SecurityIdentity get() {
                try {
                    final String result = myService.useTenant();
                    LOG.info("MyService result: " + result);  // works
                    return QuarkusSecurityIdentity.builder().setAnonymous(true).build();
                } catch (SecurityException e) {
                    LOG.debug("Authentication failed", e);
                    throw new AuthenticationFailedException(e);
                }
            }
        };
    }

    private AuthenticationRequestContext fixedAuthenticationRequestContext(final AuthenticationRequestContext originalContext) {
        return new AuthenticationRequestContext() {
            @Override
            public Uni<SecurityIdentity> runBlocking(Supplier<SecurityIdentity> supplier) {
                final InjectableContext.ContextState state = Arc.container().requestContext().getState();
                return originalContext.runBlocking(supplierWithRequestContext(state, supplier));
            }

            private Supplier<SecurityIdentity> supplierWithRequestContext(InjectableContext.ContextState state, Supplier<SecurityIdentity> supplier) {
                return new Supplier<SecurityIdentity>() {

                    @Override
                    public SecurityIdentity get() {
                        Arc.container().requestContext().activate(state);
                        try {
                            return supplier.get();
                        }finally {
                            Arc.container().requestContext().deactivate();
                        }
                    }
                };
            }
        };
    }

}
