package com.github.renegrob.io.quarkuverse.openapi.mod.deployment.attributeresolver;

import org.jboss.jandex.MethodParameterInfo;
import org.jboss.jandex.Type;

public class ParameterAttributeResolver extends MethodAttributeResolver {

    public static final String METHOD_PREFIX = "method.";
    private final MethodParameterInfo parameter;

    public ParameterAttributeResolver(MethodParameterInfo parameter) {
        super(parameter.method());
        this.parameter = parameter;
    }

    @Override
    public String apply(String name) {
        if (name.startsWith(METHOD_PREFIX)) {
            return super.apply(name.substring(METHOD_PREFIX.length()));
        }
        switch (name) {
            case "parameterName":
                return parameter.name();
            case "simpleType":
                return parameterType().name().withoutPackagePrefix();
            case "parameterType":
                return parameterType().name().toString();
            default:
                return null;
        }
    }

    private Type parameterType() {
        return parameter.method().parameters().get(parameter.position());
    }
}
