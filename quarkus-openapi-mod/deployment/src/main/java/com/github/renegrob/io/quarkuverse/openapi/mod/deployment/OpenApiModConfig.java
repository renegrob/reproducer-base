package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "openapi-mod", phase = ConfigPhase.BUILD_TIME)
public class OpenApiModConfig {

    final static String EMPTY = "com.github.renegrob.io.quarkuverse.openapi.mod.deployment.empty-value";

    @ConfigGroup
    public static class OATemplates {
        /**
         * templates
         */
        @ConfigItem(defaultValue = EMPTY)
        String operationId;

        /**
         * templates
         */
        @ConfigItem(defaultValue = EMPTY)
        String description;

        /**
         * templates
         */
        @ConfigItem(defaultValue = EMPTY)
        String summary;
    }

    /**
     * annotations
     */
    @ConfigItem
    Map<String, OATemplates> annotations;
}
