package com.github.renegrob.io.quarkuverse.openapi.mod.deployment.converter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanConverter implements Converter<Boolean> {

    public static BooleanConverter INSTANCE = new BooleanConverter();

    private BooleanConverter() {
    }

    @Override
    public Consumer<String> from(Consumer<Boolean> set) {
        return new Consumer<>() {

            @Override
            public void accept(String s) {
                set.accept(Boolean.valueOf(s));
            }
        };
    }

    @Override
    public Supplier<String> to(Supplier<Boolean> get) {
        return new Supplier<>() {

            @Override
            public String get() {
                return String.valueOf(get.get());
            }
        };
    }
}
