package com.github.renegrob.config;

import java.math.BigDecimal;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.github.renegrob.MyAnnotation;
import com.github.renegrob.MySummary;

@Path("/config")
public class ConfigResource {

    @ConfigProperty(name = "constant.speed-of-sound-in-meter-per-second", defaultValue = "343")
    int speedOfSound;

    @ConfigProperty(name = "display.mach")
    Optional<Integer> displayMach;

    @ConfigProperty(name = "display.unit.name")
    String displayUnitName;

    @ConfigProperty(name = "display.unit.factor")
    BigDecimal displayUnitFactor;

    @GET
    @Path("supersonic")
    @Produces(MediaType.TEXT_PLAIN)
    @MySummary("Supersonic")
    @MyAnnotation({"Test3", "Test4"})
    public String supersonic() {
        final int mach = displayMach.orElse(1);
        final BigDecimal speed = BigDecimal.valueOf(speedOfSound)
            .multiply(displayUnitFactor)
            .multiply(BigDecimal.valueOf(mach));
        return String.format("Mach %d is %.3f %s",
            mach,
            speed,
            displayUnitName
        );
    }
}
