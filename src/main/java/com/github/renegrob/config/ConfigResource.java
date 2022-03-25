package com.github.renegrob.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/config")
public class ConfigResource {

    @ConfigProperty(name = "my-app.name")
    String appName;

    @Inject
    MyRestClientConfig config;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> dumpConfig() {
        Map<String, Object> configMap = new LinkedHashMap<>();
        config.myClient().ifPresent(restClientConfig ->
            configMap.put("myClient", toMap(restClientConfig))
        );
        config.client().ifPresent(restClientConfig ->
                configMap.put("client", toMap(restClientConfig))
        );
        configMap.put("appName", appName);
        return configMap;
    }

    private Map<String, Object> toMap(MyRestClientConfig.RestClientConfig restClientConfig) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("baseURI", restClientConfig.uri());
        map.put("keystore", Map.of(
                "type", restClientConfig.keystore().type(),
                "path", restClientConfig.keystore().path(),
                "password", restClientConfig.keystore().password()));
        return map;
    }
}
