package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.MethodParameterInfo;

import io.smallrye.openapi.runtime.util.JandexUtil;

public class OperationAnnotationInfo {

    private Map<String, MethodAnnotationHolder> methods = new LinkedHashMap<>();

    public MethodAnnotationHolder getByMethodRef(String ref) {
        return methods.get(ref);
    }

    public MethodAnnotationHolder createForMethod(MethodInfo method) {
        String ref = JandexUtil.createUniqueMethodReference(method.declaringClass(), method);
        return methods.computeIfAbsent(ref, k -> new MethodAnnotationHolder(method));
    }

    public boolean isEmpty() {
        return methods.isEmpty();
    }

    static abstract class AbstractAnnotationHolder {

        private Map<String, AnnotationInstanceValues> annotationInstanceValues = new LinkedHashMap<>();

        public void addAnnotation(AnnotationInstance ai) {
            final String key = ai.name().toString();
            annotationInstanceValues.computeIfAbsent(key, k -> new AnnotationInstanceValues(key, ai.target())).addValues(ai.values());
        }

        public Map<String, String[]> getAnnotation(String className) {
            final AnnotationInstanceValues annotationInstanceValues = this.annotationInstanceValues.get(className);
            return annotationInstanceValues != null ? annotationInstanceValues.asMap() : null;
        }

        public Collection<AnnotationInstanceValues> values() {
            return annotationInstanceValues.values();
        }
    }

    public static class MethodAnnotationHolder extends AbstractAnnotationHolder {

        private MethodInfo method;
        private Map<String, ParameterAnnotationHolder> parameters = new LinkedHashMap<>();

        public MethodAnnotationHolder(MethodInfo method) {
            this.method = method;
        }

        public MethodInfo getMethod() {
            return method;
        }

        public ParameterAnnotationHolder createForParameter(MethodParameterInfo parameter) {
            String ref = JandexUtil.createUniqueMethodParameterRef(parameter);
            return parameters.computeIfAbsent(ref, k -> new ParameterAnnotationHolder(parameter));
        }

        public ParameterAnnotationHolder byParamRef(String paramRef) {
            return parameters.get(paramRef);
        }
    }

    public static class ParameterAnnotationHolder extends AbstractAnnotationHolder {

        private MethodParameterInfo parameter;

        public ParameterAnnotationHolder(MethodParameterInfo parameter) {
            this.parameter = parameter;
        }

        public MethodParameterInfo getParameter() {
            return parameter;
        }
    }

    public static class AnnotationInstanceValues {

        private String name;
        private AnnotationTarget target;
        private Map<String, String[]> valueMap = new LinkedHashMap<>();

        public AnnotationInstanceValues(String name, AnnotationTarget target) {
            this.name = name;
            this.target = target;
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

        public AnnotationTarget getTarget() {
            return target;
        }
    }
}
