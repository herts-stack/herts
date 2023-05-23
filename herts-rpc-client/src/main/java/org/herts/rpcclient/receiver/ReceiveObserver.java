package org.herts.rpcclient.receiver;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsSystemContext;
import org.herts.common.service.HertsReceiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReceiveObserver implements StreamObserver<Object> {
    private final ConcurrentMap<String, Method> reflectMethods = new ConcurrentHashMap<>();
    private final Object coreObject;

    public ReceiveObserver(HertsReceiver hertsReceiver) {
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
        if (methodName.equals(HertsSystemContext.Rpc.REGISTERED_METHOD_NAME)) {
            return;
        }
        var reflectMethod = this.reflectMethods.get(methodName);
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
        System.out.println("Comp!");
    }
}
