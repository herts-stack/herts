package com.tomoyane.herts.hertscommon.mapping;

import io.grpc.MethodDescriptor;

public class HertsMethod {
    private MethodDescriptor.MethodType methodType;
    private String coreServiceName;
    private String methodName;
    private Class<?> methodReturnType;
    private Class<?>[] parameters;

    public HertsMethod() {
    }

    public HertsMethod(MethodDescriptor.MethodType methodType,
                       String coreServiceName,
                       String methodName,
                       Class<?> methodReturnType,
                       Class<?>[] parameters) {

        this.methodType = methodType;
        this.coreServiceName = coreServiceName;
        this.methodName = methodName;
        this.methodReturnType = methodReturnType;
        this.parameters = parameters;
    }

    public MethodDescriptor.MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodDescriptor.MethodType methodType) {
        this.methodType = methodType;
    }

    public String getCoreServiceName() {
        return coreServiceName;
    }

    public void setCoreServiceName(String coreServiceName) {
        this.coreServiceName = coreServiceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(Class<?> methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "MethodType=" + this.methodType + ", " +
                "CoreServiceName=" + this.coreServiceName  + ", " +
                "MethodName=" + this.methodName  + ", " +
                "MethodReturnType=" + this.methodReturnType;
    }}
