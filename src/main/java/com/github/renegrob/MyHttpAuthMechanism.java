package com.github.renegrob;

import java.util.Optional;
import java.util.Set;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.quarkus.arc.Arc;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

@Alternative
@Priority(1)
@ApplicationScoped
public class MyHttpAuthMechanism implements HttpAuthenticationMechanism {

    @Inject
    CurrentVertxRequest currentVertxRequest;

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        final MyAuthenticationRequest authenticationRequest = new MyAuthenticationRequest();
        authenticationRequest.setAttribute(RoutingContext.class.getName(), context);
        return authenticate(identityProviderManager, authenticationRequest);
    }

    Uni<SecurityIdentity> authenticate(IdentityProviderManager identityProviderManager, MyAuthenticationRequest authenticationRequest) {
        Arc.container().requestContext().activate(null);
        try {
            currentVertxRequest.setCurrent(authenticationRequest.getAttribute(RoutingContext.class.getName()));
            return identityProviderManager.authenticate(authenticationRequest);
        }finally {
            Arc.container().requestContext().deactivate();
        }
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return Uni.createFrom().optional(Optional.empty());
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return Set.of(MyAuthenticationRequest.class);
    }

    @Override
    public HttpCredentialTransport getCredentialTransport() {
        return new HttpCredentialTransport(HttpCredentialTransport.Type.OTHER_HEADER, "GENERAL");
    }
}


