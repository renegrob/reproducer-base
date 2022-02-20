package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

public class AnnotationInstances {

    private Map<String, AnnotationInstanceValues> annotationMap = new LinkedHashMap<>();

    public void add(AnnotationInstance annotationInstance) {
        final AnnotationInstanceValues values = new AnnotationInstanceValues();
        values.add(annotationInstance);
        annotationMap.put(annotationInstance.name().toString(), values);
    }

    public AnnotationInstanceValues getByName(String name) {
        return annotationMap.get(name);
    }
}
