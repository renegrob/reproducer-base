package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.List;
import java.util.Map;

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
        Map<String, String> templates;

        @Override
        public String toString() {
            return String.valueOf(templates);
        }
    }

    /**
     * annotations
     */
    @ConfigItem
    Map<String, OATemplates> annotations;
}
