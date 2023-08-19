package org.hertsstack.gateway;

import org.hertsstack.core.context.SharedServiceContext;
import org.hertsstack.core.exception.ServiceMethodNotfoundException;
import org.hertsstack.core.service.HertsService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class RpcDefinition {
    private final ConcurrentMap<String, Method> methods;
    private final ConcurrentMap<String, List<Parameter>> methodParameters;
    private final ConcurrentMap<String, Class<?>> methodReturnTypes;
    private final HertsService hertsService;

    private RpcDefinition(HertsService serviceInstance,
                          ConcurrentMap<String, Method> methods,
                          ConcurrentMap<String, List<Parameter>> methodParameters,
                          ConcurrentMap<String, Class<?>> methodReturnTypes) {
        this.hertsService = serviceInstance;
        this.methods = methods;
        this.methodParameters = methodParameters;
        this.methodReturnTypes = methodReturnTypes;

    }

    public static RpcDefinition create(HertsService serviceInstance) {
        ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();
        ConcurrentMap<String, List<Parameter>> methodParameters = new ConcurrentHashMap<>();
        ConcurrentMap<String, Class<?>> methodReturnTypes = new ConcurrentHashMap<>();
        Method[] defMethods = serviceInstance.getClass().getDeclaredMethods();
        try {
            for (Method method : defMethods) {
                if (Arrays.asList(SharedServiceContext.Reflection.ignoreMethodNames).contains(method.getName())) {
                    continue;
                }

                List<Parameter> parameters = Arrays.asList(serviceInstance.getClass()
                        .getMethod(method.getName(), method.getParameterTypes()).getParameters());

                methodReturnTypes.put(method.getName(), method.getReturnType());
                methods.put(method.getName(), method);
                methodParameters.put(method.getName(), parameters);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return new RpcDefinition(serviceInstance, methods, methodParameters, methodReturnTypes);
    }

    public HertsService getHertsService() {
        return hertsService;
    }

    public ConcurrentMap<String, Method> getMethods() {
        return methods;
    }

    public ConcurrentMap<String, List<Parameter>> getMethodParameters() {
        return methodParameters;
    }

    public ConcurrentMap<String, Class<?>> getMethodReturnTypes() {
        return methodReturnTypes;
    }

    public Object callMethod(String methodName, Object... params) throws InvocationTargetException, IllegalAccessException {
        Method method = this.methods.get(methodName);
        if (method == null) {
            throw new ServiceMethodNotfoundException(methodName + " is not defined");
        }

        return method.invoke(this.hertsService, params);
    }
}
