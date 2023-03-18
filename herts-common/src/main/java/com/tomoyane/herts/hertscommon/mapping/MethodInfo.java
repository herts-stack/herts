package com.tomoyane.herts.hertscommon.mapping;

import io.grpc.MethodDescriptor;

public class MethodInfo {
    private MethodDescriptor.MethodType methodType;
    private String serviceName;
    private String[] methodNames;
    private Class<?>[] methodReturnTypes;

    public MethodDescriptor.MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodDescriptor.MethodType methodType) {
        this.methodType = methodType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String[] getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(String[] methodNames) {
        this.methodNames = methodNames;
    }

    public Class<?>[] getMethodReturnTypes() {
        return methodReturnTypes;
    }

    public void setMethodReturnTypes(Class<?>[] methodReturnTypes) {
        this.methodReturnTypes = methodReturnTypes;
    }
}
