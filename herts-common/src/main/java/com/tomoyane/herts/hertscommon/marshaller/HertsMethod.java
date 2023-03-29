package com.tomoyane.herts.hertscommon.marshaller;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;

public class HertsMethod {
    private HertsCoreType hertsCoreType;
    private String coreServiceName;
    private String methodName;
    private Class<?> methodReturnType;
    private Class<?>[] parameters;

    public HertsMethod() {
    }

    public HertsMethod(HertsCoreType methodType,
                       String coreServiceName,
                       String methodName,
                       Class<?> methodReturnType,
                       Class<?>[] parameters) {

        this.hertsCoreType = methodType;
        this.coreServiceName = coreServiceName;
        this.methodName = methodName;
        this.methodReturnType = methodReturnType;
        this.parameters = parameters;
    }

    public HertsCoreType getHertsCoreType() {
        return hertsCoreType;
    }

    public void setHertsCoreType(HertsCoreType hertsCoreType) {
        this.hertsCoreType = hertsCoreType;
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
        return "HertsCoreType=" + this.hertsCoreType + ", " +
                "CoreServiceName=" + this.coreServiceName  + ", " +
                "MethodName=" + this.methodName  + ", " +
                "MethodReturnType=" + this.methodReturnType;
    }
}
