package com.github.renegrob.config;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

import io.smallrye.config.ConfigMapping;

// @ConfigMapping(prefix = "my.app.rest.config") // this way client() will work
@ConfigMapping(prefix = "my-app.rest-config") // does not work!
public interface MyRestClientConfig {

    Optional<RestClientConfig> client();
    Optional<RestClientConfig> myClient(); // does not work!

    interface RestClientConfig {

        URI uri();

        KeystoreConfig keystore();

        interface KeystoreConfig {
            Optional<String> type();

            Path path();

            String password();
        }

    }
}
