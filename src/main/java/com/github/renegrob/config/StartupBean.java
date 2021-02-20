package com.github.renegrob.config;

import java.math.BigDecimal;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Level;
import org.jboss.logmanager.LogManager;
import org.jboss.logmanager.Logger;

import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
@Unremovable
public class StartupBean {

    private static final Logger LOG = Logger.getLogger(StartupBean.class.getName());

    @ConfigProperty(name = "display.unit.name")
    String displayUnitName;

    @ConfigProperty(name = "fix.value")
    String fixValue;

    public void onStart(@Observes StartupEvent ev) {
        LOG.info("display.unit.name: " + displayUnitName);
        LOG.info("fix.value: " + fixValue);
    }
}
