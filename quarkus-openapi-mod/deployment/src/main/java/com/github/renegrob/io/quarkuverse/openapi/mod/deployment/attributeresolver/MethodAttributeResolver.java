package com.github.renegrob.io.quarkuverse.openapi.mod.deployment.attributeresolver;

import java.util.function.Function;

import org.jboss.jandex.MethodInfo;

public class MethodAttributeResolver implements Function<String, String> {

    private final MethodInfo method;

    public MethodAttributeResolver(MethodInfo method) {
        this.method = method;
    }

    @Override
    public String apply(String name) {
        switch (name) {
            case "className":
                return method.declaringClass().name().toString();
            case "simpleClassName":
                return method.declaringClass().simpleName();
            case "methodName":
                return method.name();
            case "simpleReturnType":
                return method.returnType().name().withoutPackagePrefix();
            case "returnType":
                return method.returnType().name().toString();
            default:
                return null;
        }
    }
}
