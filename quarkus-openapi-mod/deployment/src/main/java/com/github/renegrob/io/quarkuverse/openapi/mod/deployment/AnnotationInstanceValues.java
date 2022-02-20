package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

public class AnnotationInstanceValues {

    private String name;
    private Map<String, String[]> valueMap = new LinkedHashMap<>();

    public void add(AnnotationInstance annotationInstance) {
        this.name = annotationInstance.name().toString();
        for (AnnotationValue v : annotationInstance.values()) {
            if (v.kind() == AnnotationValue.Kind.ARRAY) {
                valueMap.put(v.name(), v.asStringArray());
            } else {
                valueMap.put(v.name(), new String[]{v.asString()});
            }
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, String[]> getValueMap() {
        return valueMap;
    }
}
