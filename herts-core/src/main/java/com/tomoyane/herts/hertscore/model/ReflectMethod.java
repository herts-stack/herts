package com.tomoyane.herts.hertscore.model;

import java.lang.reflect.Method;

public class ReflectMethod {
    private final String className;
    private final Method[] methods;

    private ReflectMethod(String className, Method[] methods) {
        this.className = className;
        this.methods = methods;
    }

    public static ReflectMethod create(String className, Method[] methods) {
        return new ReflectMethod(className, methods);
    }

    public String getClassName() {
        return className;
    }

    public Method[] getMethods() {
        return methods;
    }
}
