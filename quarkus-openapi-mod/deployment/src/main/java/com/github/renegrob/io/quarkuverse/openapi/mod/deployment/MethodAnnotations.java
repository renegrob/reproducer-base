package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

public class MethodAnnotations {

    private Map<String, AnnotationInstances> map = new LinkedHashMap<>();

    public AnnotationInstances forMethod(String ref) {
        return map.computeIfAbsent(ref, k -> new AnnotationInstances());
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public static class AnnotationInstances {

        private Map<String, AnnotationInstanceValues> map = new LinkedHashMap<>();

        public void addAnnotation(AnnotationInstance ai) {
            final String key = ai.name().toString();
            map.computeIfAbsent(key, k -> new AnnotationInstanceValues(key)).addValues(ai.values());
        }

        public Map<String, String[]> getAnnotation(String className) {
            final AnnotationInstanceValues annotationInstanceValues = map.get(className);
            return annotationInstanceValues != null ? annotationInstanceValues.asMap() : null;
        }

        public Collection<AnnotationInstanceValues> values() {
            return map.values();
        }
    }

    public static class AnnotationInstanceValues {

        private String name;
        private Map<String, String[]> valueMap = new LinkedHashMap<>();

        public AnnotationInstanceValues(String name) {
            this.name = name;
        }

        public void addValues(List<AnnotationValue> values) {
            for (AnnotationValue v : values) {
                if (v.kind() == AnnotationValue.Kind.ARRAY) {
                    valueMap.put(v.name(), v.asStringArray());
                } else {
                    valueMap.put(v.name(), new String[]{v.asString()});
                }
            }
        }

        public Map<String, String[]> asMap() {
            return valueMap;
        }

        public String getName() {
            return name;
        }
    }
}
