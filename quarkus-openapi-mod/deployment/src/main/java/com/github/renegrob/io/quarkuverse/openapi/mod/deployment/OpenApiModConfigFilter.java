package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Paths;
import com.google.common.base.Strings;

import io.quarkus.runtime.util.StringUtil;
import io.smallrye.common.expression.Expression;
import io.smallrye.openapi.api.models.OperationImpl;

import static io.smallrye.common.expression.Expression.Flag.GENERAL_EXPANSION;
import static io.smallrye.common.expression.Expression.Flag.NO_SMART_BRACES;

public class OpenApiModConfigFilter implements OASFilter {

    private OpenApiModConfig config = new OpenApiModConfig();
    private Map<String, Map<String, AnnotationInstanceValues>> methodReferences = Map.of();
    private OpenAPI appliedTo;

    public OpenApiModConfigFilter() {
    }

    public OpenApiModConfigFilter(OpenApiModConfig config, Map<String, Map<String, AnnotationInstanceValues>> myAnnotationMethodReferences) {
        this.config = config;
        this.methodReferences = myAnnotationMethodReferences;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (!methodReferences.isEmpty()) {
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
                                if (methodReferences.containsKey(operationImpl.getMethodRef())) {
                                    final Map<String, AnnotationInstanceValues> annotationInstances = methodReferences.get(
                                            operationImpl.getMethodRef());
                                    for (Map.Entry<String, AnnotationInstanceValues> entry: annotationInstances.entrySet()) {
                                        final OpenApiModConfig.OATemplates oaTemplates = config.annotations.get(entry.getKey());

                                        applyTempate(oaTemplates.operationId, operation::setOperationId,
                                                operation::getOperationId,
                                                entry.getValue());
                                        applyTempate(oaTemplates.summary, operation::setSummary, operation::getSummary,
                                                entry.getValue());
                                        applyTempate(oaTemplates.description, operation::setDescription,
                                                operation::getDescription,
                                                entry.getValue());
                                    }
                                }
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

    private void applyTempate(String template, Consumer<String> set, Supplier<String> getOldValue,
            AnnotationInstanceValues aiv) {
        if (template.equals(OpenApiModConfig.EMPTY)) {
            return;
        }
        final Expression expression = createExpression(template);

        final String oldValue = Strings.nullToEmpty(getOldValue.get());
        String result = expression.evaluate((c, b) -> {
            if ("oldValue".equals(c.getKey())) {
                b.append(oldValue);
                return;
            }
            final String[] values = aiv.getValueMap().get(c.getKey());
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
