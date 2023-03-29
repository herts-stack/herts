package com.tomoyane.herts.tools;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        try {
            Class<TestClass> thisClass = TestClass.class;
            Method[] methods = thisClass.getDeclaredMethods();

            for (Method method : methods) {
                System.out.println("== Method name");
                System.out.println(method.getName());
                System.out.println(method.getReturnType().getName());

                if (method.getParameterTypes().length > 0) {
                    System.out.println("====");
                    for (var a : method.getParameterTypes()) {
                        System.out.println(a.getName());
                    }
                }
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    public static class TestClass {
        public void foo(int a, String b) { }

        public int bar() { return 12; }

        public String baz() { return ""; }
    }
}