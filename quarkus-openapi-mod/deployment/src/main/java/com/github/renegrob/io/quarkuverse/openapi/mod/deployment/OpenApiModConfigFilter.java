package com.github.renegrob.io.quarkuverse.openapi.mod.deployment;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Paths;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.OpenApiModConfig.OAMOperationTemplate;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.OpenApiModConfig.OAMParameterTemplate;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.OperationAnnotationInfo.AnnotationInstanceValues;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.OperationAnnotationInfo.MethodAnnotationHolder;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.OperationAnnotationInfo.ParameterAnnotationHolder;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.attributeresolver.MethodAttributeResolver;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.converter.BooleanConverter;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.converter.Converter;
import com.github.renegrob.io.quarkuverse.openapi.mod.deployment.converter.JsonConverter;
import com.google.common.base.Strings;

import io.smallrye.common.expression.Expression;
import io.smallrye.openapi.api.models.OperationImpl;
import io.smallrye.openapi.api.models.parameters.ParameterImpl;

import static io.smallrye.common.expression.Expression.Flag.GENERAL_EXPANSION;
import static io.smallrye.common.expression.Expression.Flag.NO_SMART_BRACES;

public class OpenApiModConfigFilter implements OASFilter {

    private OpenApiModConfig config = new OpenApiModConfig();
    private OperationAnnotationInfo operationAnnotationInfo = new OperationAnnotationInfo();
    private OpenAPI appliedTo;

    public OpenApiModConfigFilter() {
    }

    public OpenApiModConfigFilter(OpenApiModConfig config, OperationAnnotationInfo operationAnnotationInfo) {
        this.config = config;
        this.operationAnnotationInfo = operationAnnotationInfo;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (!operationAnnotationInfo.isEmpty()) {
            if (appliedTo == openAPI) {
                return;
            }
            appliedTo = openAPI;
            System.out.println("Apply filter: " + this);
            Paths paths = openAPI.getPaths();
            if (paths != null) {
                Map<String, PathItem> pathItems = paths.getPathItems();
                if (pathItems != null) {
                    Set<Map.Entry<String, PathItem>> pathItemsEntries = pathItems.entrySet();
                    for (Map.Entry<String, PathItem> pathItem : pathItemsEntries) {
                        Map<PathItem.HttpMethod, Operation> operations = pathItem.getValue().getOperations();
                        if (operations != null) {
                            for (Operation operation : operations.values()) {
                                final String methodRef = ((OperationImpl) operation).getMethodRef();
                                applyModifications(operation, methodRef);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyModifications(Operation operation, String methodRef) {
        final MethodAnnotationHolder methodAnnotationHolder = operationAnnotationInfo.getByMethodRef(methodRef);

        if (methodAnnotationHolder != null) {
            System.out.println("method: " + methodAnnotationHolder.getMethod().name());

            MethodAttributeResolver methodAttributeResolver = new MethodAttributeResolver(methodAnnotationHolder.getMethod());

            for (AnnotationInstanceValues aiv: methodAnnotationHolder.values()) {
                final OAMOperationTemplate operationTemplate = config.methodAnnotations.get(aiv.getName());

                applyTemplate(operationTemplate.operationId, operation::setOperationId,
                            operation::getOperationId,
                            aiv, methodAttributeResolver);
                applyTemplate(operationTemplate.summary, operation::setSummary, operation::getSummary,
                        aiv, methodAttributeResolver);
                applyTemplate(operationTemplate.description, operation::setDescription,
                        operation::getDescription,
                        aiv, methodAttributeResolver);
            }

            if (operation.getParameters() != null) {
                for (Parameter parameter : operation.getParameters()) {
                    final String paramRef = ParameterImpl.getParamRef(parameter);
                    ParameterAnnotationHolder parameterAnnotationHolder = (paramRef != null) ? methodAnnotationHolder.byParamRef(paramRef) : null;
                    if (parameterAnnotationHolder != null) {
                        System.out.println("method: " + methodAnnotationHolder.getMethod().name() + "." + parameter.getName() + " apply" + parameterAnnotationHolder.values().stream().map(AnnotationInstanceValues::getName).collect(
                                Collectors.toList()));
                        for (AnnotationInstanceValues aiv : parameterAnnotationHolder.values()) {
                            final OAMParameterTemplate parameterTemplate = config.parameterAnnotations.get(aiv.getName());
                            // TODO: ensure only allowed characters are used!
                            applyTemplate(parameterTemplate.description, parameter::setDescription,
                                    parameter::getDescription,
                                    aiv, methodAttributeResolver);
                            applyTemplate(parameterTemplate.required, parameter::setRequired,
                                    parameter::getRequired, BooleanConverter.INSTANCE,
                                    aiv, methodAttributeResolver);
                           applyTemplate(parameterTemplate.example, parameter::setExample,
                                    parameter::getExample, JsonConverter.INSTANCE,
                                    aiv, methodAttributeResolver);

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

    private <T> void applyTemplate(Optional<String> template, Consumer<T> set, Supplier<T> getOldValue, Converter<T> converter,
            AnnotationInstanceValues aiv, Function<String, String> valueResolver) {
        applyTemplate(template, converter.from(set), converter.to(getOldValue), aiv, valueResolver);
    }


    private void applyTemplate(Optional<String> template, Consumer<String> set, Supplier<String> getOldValue,
            AnnotationInstanceValues aiv, Function<String, String> valueResolver) {
        if (template.isEmpty()) {
            return;
        }
        final Expression expression = createExpression(template.get());

        final String oldValue = Strings.nullToEmpty(getOldValue.get());
        String result = expression.evaluate((c, b) -> {
            final String[] values = aiv.asMap().get(c.getKey());
            if (values == null || values.length == 0) {
                if ("oldValue".equals(c.getKey())) {
                    b.append(oldValue);
                    return;
                }
                String value = valueResolver.apply(c.getKey());
                if (value != null) {
                    b.append(value);
                    return;
                }
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
