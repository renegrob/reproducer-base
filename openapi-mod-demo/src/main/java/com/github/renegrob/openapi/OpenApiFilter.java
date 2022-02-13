package com.github.renegrob.openapi;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Operation;

public class OpenApiFilter<filterOperation> implements OASFilter {

    @Override
    public Operation filterOperation(Operation operation) {
        if (operation.getSummary() == null) {
            if (operation.getOperationId() != null) {
                //operation.setSummary(operation.getOperationId().split("_", 2)[1]);
            }
        }
        return OASFilter.super.filterOperation(operation);
    }
}
