package com.tomoyane.herts.hertscore.model;

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
    private final String className;
    private final List<Method> methods = new ArrayList<>();

    private ReflectMethod(String className, Method[] methods) {
        this.className = className;
        this.methods.addAll(Arrays.asList(methods));
    }

    /**
     * Factory method
     * @param className Class name
     * @param methods method list
     * @return ReflectMethod instance
     */
    public static ReflectMethod create(String className, Method[] methods) {
        return new ReflectMethod(className, methods);
    }

    public void printMethodName() {
        for (Method method : this.methods) {
            System.out.println(method.getName());
        }
    }

    public void add(Method method) {
        this.methods.add(method);
    }

    public String getClassName() {
        return className;
    }

    public Method[] getMethods() {
        return methods.toArray(Method[]::new);
    }
}
