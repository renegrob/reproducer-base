package com.github.renegrob.myext.deployment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Paths;

import com.google.common.base.Strings;

import io.smallrye.openapi.api.models.OperationImpl;

public class MyConfigFilter implements OASFilter {

    private Map<String, List<String>> methodReferences = Map.of();

    public MyConfigFilter() {
    }

    public MyConfigFilter(Map<String, List<String>> myAnnotationMethodReferences) {
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

                                if (methodReferences.keySet().contains(operationImpl.getMethodRef())) {
                                    List<String> values = methodReferences.get(operationImpl.getMethodRef());
                                    operation.setSummary(Strings.nullToEmpty(operation.getSummary()) + values);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
