package org.herts.common.modelx;

import org.herts.common.context.HertsType;

/**
 * Herts rpc method for internal registration
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsMethod {
    private HertsType hertsType;
    private String coreServiceName;
    private String coreImplServiceName;
    private String methodName;
    private Class<?> methodReturnType;
    private Class<?>[] parameters;

    public HertsMethod() {
    }

    public HertsMethod(HertsType methodType,
                       String coreServiceName,
                       String coreImplServiceName, String methodName,
                       Class<?> methodReturnType,
                       Class<?>[] parameters) {

        this.hertsType = methodType;
        this.coreServiceName = coreServiceName;
        this.coreImplServiceName = coreImplServiceName;
        this.methodName = methodName;
        this.methodReturnType = methodReturnType;
        this.parameters = parameters;
    }

    public HertsType getHertsCoreType() {
        return hertsType;
    }

    public void setHertsCoreType(HertsType hertsType) {
        this.hertsType = hertsType;
    }

    public String getCoreServiceName() {
        return coreServiceName;
    }

    public void setCoreServiceName(String coreServiceName) {
        this.coreServiceName = coreServiceName;
    }

    public String getCoreImplServiceName() {
        return coreImplServiceName;
    }

    public void setCoreImplServiceName(String coreImplServiceName) {
        this.coreImplServiceName = coreImplServiceName;
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
        return "HertsCoreType=" + this.hertsType + ", " +
                "CoreServiceName=" + this.coreServiceName + ", " +
                "MethodName=" + this.methodName + ", " +
                "MethodReturnType=" + this.methodReturnType;
    }
}
