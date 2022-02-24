package com.github.renegrob.io.quarkuverse.openapi.mod.deployment.converter;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.openapi.runtime.io.JsonUtil;

public class JsonConverter implements Converter<Object> {

    public static JsonConverter INSTANCE = new JsonConverter();

    private JsonConverter() {
    }

    @Override
    public Consumer<String> from(Consumer<Object> set) {
        return new Consumer<>() {

            @Override
            public void accept(String s) {
                set.accept(JsonUtil.parseValue(s));
            }
        };
    }

    @Override
    public Supplier<String> to(Supplier<Object> get) {
        return new Supplier<>() {

            @Override
            public String get() {
                final ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.writeValueAsString(get.get());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
