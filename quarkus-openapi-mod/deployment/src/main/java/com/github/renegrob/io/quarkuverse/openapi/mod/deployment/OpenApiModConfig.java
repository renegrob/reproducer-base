package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "openapi-mod", phase = ConfigPhase.BUILD_TIME)
public class OpenApiModConfig {

    @ConfigGroup
    public static class OAMOperationTemplate {
        /**
         * operationId
         */
        @ConfigItem
        Optional<String> operationId;

        /**
         * description
         */
        @ConfigItem
        Optional<String> description;

        /**
         * summary
         */
        @ConfigItem
        Optional<String> summary;
    }

    @ConfigGroup
    public static class OAMParameterTemplate {
        /**
         * required
         */
        @ConfigItem
        Optional<String> required;

        /**
         * description
         */
        @ConfigItem
        Optional<String> description;

        /**
         * summary
         */
        @ConfigItem
        Optional<String> summary;

        /**
         * exmaple
         */
        @ConfigItem
        Optional<String> example;
    }

    /**
     * methodAnnotations
     */
    @ConfigItem
    Map<String, OAMOperationTemplate> methodAnnotations;

    /**
     * parameterAnnotations
     */
    @ConfigItem
    Map<String, OAMParameterTemplate> parameterAnnotations;

}
