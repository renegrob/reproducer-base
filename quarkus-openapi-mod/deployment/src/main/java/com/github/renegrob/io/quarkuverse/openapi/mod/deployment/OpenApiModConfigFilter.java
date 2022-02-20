package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Paths;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.MethodAnnotations.AnnotationInstanceValues;
import com.google.common.base.Strings;

import io.smallrye.common.expression.Expression;
import io.smallrye.openapi.api.models.OperationImpl;
import io.smallrye.openapi.api.models.parameters.ParameterImpl;

import static io.smallrye.common.expression.Expression.Flag.GENERAL_EXPANSION;
import static io.smallrye.common.expression.Expression.Flag.NO_SMART_BRACES;

public class OpenApiModConfigFilter implements OASFilter {

    private OpenApiModConfig config = new OpenApiModConfig();
    private MethodAnnotations methodAnnotations = new MethodAnnotations();
    private OpenAPI appliedTo;

    public OpenApiModConfigFilter() {
    }

    public OpenApiModConfigFilter(OpenApiModConfig config, MethodAnnotations methodAnnotations) {
        this.config = config;
        this.methodAnnotations = methodAnnotations;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (!methodAnnotations.isEmpty()) {
            if (appliedTo == openAPI) {
                return;
            }
            appliedTo = openAPI;
            System.out.println("Apply filter: " + this);
            Paths paths = openAPI.getPaths();
            if (paths != null) {
                Map<String, PathItem> pathItems = paths.getPathItems();
                if (pathItems != null && !pathItems.isEmpty()) {
                    Set<Map.Entry<String, PathItem>> pathItemsEntries = pathItems.entrySet();
                    for (Map.Entry<String, PathItem> pathItem : pathItemsEntries) {
                        Map<PathItem.HttpMethod, Operation> operations = pathItem.getValue().getOperations();
                        if (operations != null && !operations.isEmpty()) {

                            for (Operation operation : operations.values()) {

                                OperationImpl operationImpl = (OperationImpl) operation;
                                final MethodAnnotations.AnnotationInstances annotationInstances = methodAnnotations.forMethod(
                                        operationImpl.getMethodRef());
                                if (annotationInstances != null) {
                                    for (AnnotationInstanceValues aiv: annotationInstances.values()) {
                                        final OpenApiModConfig.OATemplates oaTemplates = config.annotations.get(aiv.getName());

                                        applyTemplate(oaTemplates.operationId, operation::setOperationId,
                                                    operation::getOperationId,
                                                    aiv);
                                        applyTemplate(oaTemplates.summary, operation::setSummary, operation::getSummary,
                                                aiv);
                                        applyTemplate(oaTemplates.description, operation::setDescription,
                                                operation::getDescription,
                                                aiv);
                                    }
                                }

                                // operation.getParameters().get(0).
                                // System.out.println(((OperationImpl) operation).getMethodRef() + " -> "+ template.getKey() + template.getValue());
                            }
                        }
                    }
                }
            }
        }
    }


    private Expression createExpression(String template) {
        return Expression.compile(template.replace("#{", "${"), NO_SMART_BRACES,
                GENERAL_EXPANSION);
    }

    private void applyTemplate(Optional<String> template, Consumer<String> set, Supplier<String> getOldValue,
            AnnotationInstanceValues aiv) {
        if (template.isEmpty()) {
            return;
        }
        final Expression expression = createExpression(template.get());

        final String oldValue = Strings.nullToEmpty(getOldValue.get());
        String result = expression.evaluate((c, b) -> {
            if ("oldValue".equals(c.getKey())) {
                b.append(oldValue);
                return;
            }
            final String[] values = aiv.asMap().get(c.getKey());
            if (values == null || values.length == 0) {
                if (!c.hasDefault()) {
                    throw new IllegalStateException(String.format("Unresolved variable: %s", c.getKey()));
                }
                c.expandDefault();
            } else {
                b.append(String.join(", ", values));
            }
        });
        set.accept(result.trim());
    }
}
