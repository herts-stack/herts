package org.hertsstack.rpc;

import org.hertsstack.core.logger.Logging;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Herts refection method
 * @author Herts Contributer
 */
public class ReflectMethod {
    private static final java.util.logging.Logger logger = Logging.getLogger(ReflectMethod.class.getSimpleName());

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
        String[] rpcPaths = this.serviceName.split("\\.");
        String rpcPath = rpcPaths[rpcPaths.length-1];
        logger.info(rpcPath + " stats");
        for (Method method : this.methods) {
            logger.info(rpcPath + "/" + method.getName());
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
