package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Paths;
import com.google.common.base.Strings;

import io.smallrye.common.expression.Expression;
import io.smallrye.openapi.api.models.OperationImpl;

import static io.smallrye.common.expression.Expression.Flag.GENERAL_EXPANSION;
import static io.smallrye.common.expression.Expression.Flag.NO_SMART_BRACES;

public class OpenApiModConfigFilter implements OASFilter {

    private OpenApiModConfig config = new OpenApiModConfig();
    private Map<String, AnnotationInstanceValues> methodReferences = Map.of();

    public OpenApiModConfigFilter() {
    }

    public OpenApiModConfigFilter(OpenApiModConfig config, Map<String, AnnotationInstanceValues> myAnnotationMethodReferences) {
        this.config = config;
        this.methodReferences = myAnnotationMethodReferences;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (!methodReferences.isEmpty()) {
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
                                    AnnotationInstanceValues aiv = methodReferences.get(operationImpl.getMethodRef());
                                    final OpenApiModConfig.OATemplates oaTemplates = config.annotations.get(aiv.getName());
                                    for (Map.Entry<String, String> template : oaTemplates.templates.entrySet()) {

                                        OASOperationAttribute operationAttribute = OASOperationAttribute.parseCamelCase(template.getKey());
                                        final Expression expression = createExpression(template);

                                        switch (operationAttribute) {
                                            case OPERATION_ID:
                                                operation.setOperationId(applyTemplate(expression, aiv, operation.getOperationId()));
                                                break;
                                            case SUMMARY:
                                                operation.setSummary(applyTemplate(expression, aiv, operation.getSummary()));
                                                break;
                                            case DESCRIPTION:
                                                operation.setDescription(applyTemplate(expression, aiv, operation.getDescription()));
                                                break;
                                        }
                                        // System.out.println(((OperationImpl) operation).getMethodRef() + " -> "+ template.getKey() + template.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Expression createExpression(Map.Entry<String, String> template) {
        return Expression.compile(template.getValue().replace("#{", "${"), NO_SMART_BRACES,
                GENERAL_EXPANSION);
    }

    private String applyTemplate(Expression template, AnnotationInstanceValues aiv, String oldValue) {
        final String oldValueOrEmpty = Strings.nullToEmpty(oldValue);
        return template.evaluate((c, b) -> {
            if ("oldValue".equals(c.getKey())) {
                b.append(oldValueOrEmpty);
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
    }
}
