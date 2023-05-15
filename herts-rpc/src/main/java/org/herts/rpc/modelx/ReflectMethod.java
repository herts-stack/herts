package org.herts.rpc.modelx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Herts refection method
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReflectMethod {
    private final String serviceName;
    private final String serviceImplName;
    private final List<Method> methods = new ArrayList<>();

    private ReflectMethod(String className, Method[] methods, String classImplName) {
        this.serviceName = className;
        this.serviceImplName = classImplName;
        this.methods.addAll(Arrays.asList(methods));
    }

    /**
     * Factory method
     * @param serviceName Service interface name
     * @param serviceImplName Service implement class name
     * @param methods method list
     * @return ReflectMethod instance
     */
    public static ReflectMethod create(String serviceName, String serviceImplName, Method[] methods) {
        return new ReflectMethod(serviceName, methods, serviceImplName);
    }

    public void printMethodName() {
        System.out.println(this.serviceName);
        for (Method method : this.methods) {
            System.out.println(method.getName());
        }
    }

    public void add(Method method) {
        this.methods.add(method);
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public Method[] getMethods() {
        return methods.toArray(Method[]::new);
    }
}
