package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "openapi-mod", phase = ConfigPhase.BUILD_TIME)
public class OpenApiModConfig {
}
