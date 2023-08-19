package org.hertsstack.core.modelx;

import org.hertsstack.core.context.HertsType;

import java.lang.reflect.Parameter;

/**
 * Herts rpc method for internal registration
 *
 * @author Herts Contributer
 */
public class RegisteredMethod {
    private HertsType hertsType;
    private String coreServiceName;
    private String coreImplServiceName;
    private String methodName;
    private Class<?> methodReturnType;
    private Class<?>[] parameterClasses;
    private Parameter[] parameters;

    public RegisteredMethod() {
    }

    public RegisteredMethod(HertsType methodType,
                            String coreServiceName,
                            String coreImplServiceName,
                            String methodName,
                            Class<?> methodReturnType,
                            Class<?>[] parameters) {

        this.hertsType = methodType;
        this.coreServiceName = coreServiceName;
        this.coreImplServiceName = coreImplServiceName;
        this.methodName = methodName;
        this.methodReturnType = methodReturnType;
        this.parameterClasses = parameters;
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

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }

    public void setParameterClasses(Class<?>[] parameterClasses) {
        this.parameterClasses = parameterClasses;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
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
