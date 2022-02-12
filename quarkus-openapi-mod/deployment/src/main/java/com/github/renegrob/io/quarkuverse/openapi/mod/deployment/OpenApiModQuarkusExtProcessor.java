package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

@SuppressWarnings("unused")
class OpenApiModQuarkusExtProcessor {

    private static final String FEATURE = "quarkus-openapi-mod-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

}
