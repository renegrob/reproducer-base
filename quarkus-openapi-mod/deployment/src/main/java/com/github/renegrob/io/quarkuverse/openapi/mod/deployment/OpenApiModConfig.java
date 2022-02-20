package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;
import java.util.Optional;

import javax.swing.text.html.Option;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "openapi-mod", phase = ConfigPhase.BUILD_TIME)
public class OpenApiModConfig {

    @ConfigGroup
    public static class OATemplates {
        /**
         * templates
         */
        @ConfigItem
        Optional<String> operationId;

        /**
         * templates
         */
        @ConfigItem
        Optional<String> description;

        /**
         * templates
         */
        @ConfigItem
        Optional<String> summary;
    }

    /**
     * annotations
     */
    @ConfigItem
    Map<String, OATemplates> annotations;
}
