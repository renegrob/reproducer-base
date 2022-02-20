package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.quarkus.runtime.util.StringUtil;

public enum OASOperationAttribute {
    OPERATION_ID,
    SUMMARY,
    DESCRIPTION;

    public static OASOperationAttribute parseCamelCase(String value) {
        final String name = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(StringUtil.camelHumpsIterator(value), Spliterator.ORDERED),
                false).map(String::toUpperCase).collect(Collectors.joining("_"));
        return valueOf(name);
    }
}
