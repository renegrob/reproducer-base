package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.ArrayList;
import java.util.List;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.smallrye.openapi.deployment.OpenApiFilteredIndexViewBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.runtime.util.JandexUtil;

public class OpenApiModBuildStep {

    @BuildStep
    void addOpenApiModConfigFilter(BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionProducer,
            OpenApiFilteredIndexViewBuildItem apiFilteredIndexViewBuildItem, OpenApiModConfig config) {

        System.out.println("annotations: " + config.annotations);

        final MethodAnnotations methodAnnotations = getMyAnnotationMethodReferences(
                config, apiFilteredIndexViewBuildItem);

        addToOpenAPIDefinitionProducer.produce(
                new AddToOpenAPIDefinitionBuildItem(new OpenApiModConfigFilter(config, methodAnnotations)));
    }

    private MethodAnnotations getMyAnnotationMethodReferences(
            OpenApiModConfig config, OpenApiFilteredIndexViewBuildItem apiFilteredIndexViewBuildItem) {

        MethodAnnotations methodAnnotations = new MethodAnnotations();
        for (String annotationName : config.annotations.keySet()) {
            List<AnnotationInstance> annotationInstances = new ArrayList<>();
            annotationInstances.addAll(apiFilteredIndexViewBuildItem.getIndex().getAnnotations(DotName.createSimple(annotationName)));
            for (AnnotationInstance ai : annotationInstances) {
                if (ai.target().kind().equals(AnnotationTarget.Kind.METHOD_PARAMETER)) {

                }
                if (ai.target().kind().equals(AnnotationTarget.Kind.METHOD)) {
                    MethodInfo method = ai.target().asMethod();
                    String ref = JandexUtil.createUniqueMethodReference(method.declaringClass(), method);
                    //System.out.println(String.format("method annotationInstance: %s -> %s", ref, ai));
                    methodAnnotations.forMethod(ref).addAnnotation(ai);
                }
                if (ai.target().kind().equals(AnnotationTarget.Kind.CLASS)) {
                    ClassInfo classInfo = ai.target().asClass();
                    List<MethodInfo> methods = classInfo.methods();
                    for (MethodInfo method : methods) {
                        String ref = JandexUtil.createUniqueMethodReference(classInfo, method);
                        //System.out.println(String.format("class annotationInstance: %s -> %s", ref, ai));
                        methodAnnotations.forMethod(ref).addAnnotation(ai);
                    }
                }
            }
        }
        return methodAnnotations;
    }
}
