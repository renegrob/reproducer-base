package com.github.renegrob.io.quarkuverse.openapi.mod.deployment.converter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Converter<T> {
    Consumer<String> from(Consumer<T> set);
    Supplier<String> to(Supplier<T> get);
}
