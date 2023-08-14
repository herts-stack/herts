package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.context.SharedServiceContext;
import org.hertsstack.core.service.HertsReceiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Internal reactive observer
 *
 * @author Herts Contributer
 */
class InternalReactiveObserver implements StreamObserver<Object> {
    private final ConcurrentMap<String, Method> reflectMethods = new ConcurrentHashMap<>();
    private final Object coreObject;

    public InternalReactiveObserver(HertsReceiver hertsReceiver) {
        this.coreObject = hertsReceiver;

        Class<?> hertsReceiverClass = hertsReceiver.getClass();
        Method[] methods = hertsReceiverClass.getDeclaredMethods();
        for (Method method : methods) {
            this.reflectMethods.put(method.getName(), method);
        }
    }

    @Override
    public void onNext(Object value) {
        List<Object> receivedData = (List<Object>) value;
        if (receivedData.size() == 0) {
            return;
        }
        String methodName = (String) receivedData.get(0);
        if (methodName.equals(SharedServiceContext.Rpc.REGISTERED_METHOD_NAME)) {
            return;
        }
        Method reflectMethod = this.reflectMethods.get(methodName);
        if (reflectMethod == null) {
            return;
        }

        receivedData.remove(0);
        Object[] request = receivedData.toArray();
        try {
            if (request.length == 0) {
                reflectMethod.invoke(this.coreObject);
            } else {
                reflectMethod.invoke(this.coreObject, request);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
    }
}
