package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;

import com.github.renegrob.io.quarkuverse.openapi.mod.runtime.MyAnnotation;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.smallrye.openapi.deployment.OpenApiFilteredIndexViewBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.runtime.util.JandexUtil;

public class OpenApiModBuildStep {

    public static final DotName MY_ANNOTATION = DotName.createSimple(MyAnnotation.class.getName());

    @BuildStep
    void addMyFilter(BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionProducer,
            OpenApiFilteredIndexViewBuildItem apiFilteredIndexViewBuildItem) {

        // TODO: Use CDI to configure or build-time properties
        List<AnnotationInstance> annotationInstances = new ArrayList<>();
        annotationInstances.addAll(apiFilteredIndexViewBuildItem.getIndex().getAnnotations(MY_ANNOTATION));

        final Map<String, List<String>> myAnnotationMethodReferences = getMyAnnotationMethodReferences(
                apiFilteredIndexViewBuildItem);

        addToOpenAPIDefinitionProducer.produce(
                new AddToOpenAPIDefinitionBuildItem(new OpenApiModConfigFilter(myAnnotationMethodReferences)));

    }

    private Map<String, List<String>> getMyAnnotationMethodReferences(
            OpenApiFilteredIndexViewBuildItem apiFilteredIndexViewBuildItem) {
        List<AnnotationInstance> annotationInstances = new ArrayList<>();
        annotationInstances.addAll(apiFilteredIndexViewBuildItem.getIndex().getAnnotations(MY_ANNOTATION));
        Map<String, List<String>> methodReferences = new HashMap<>();
        for (AnnotationInstance ai : annotationInstances) {
            if (ai.target().kind().equals(AnnotationTarget.Kind.METHOD)) {
                MethodInfo method = ai.target().asMethod();
                String ref = JandexUtil.createUniqueMethodReference(method.declaringClass(), method);
                methodReferences.put(ref, List.of(ai.value().asStringArray()));
            }
            if (ai.target().kind().equals(AnnotationTarget.Kind.CLASS)) {
                ClassInfo classInfo = ai.target().asClass();
                List<MethodInfo> methods = classInfo.methods();
                for (MethodInfo method : methods) {
                    String ref = JandexUtil.createUniqueMethodReference(classInfo, method);
                    methodReferences.put(ref, List.of(ai.value().asStringArray()));
                }
            }
        }
        return methodReferences;
    }
}
