package org.herts.common.service;

import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HertsDuplexStreamingInvoker implements InvocationHandler {
    private final StreamObserver<Object[]> streamObservers;

    public HertsDuplexStreamingInvoker(StreamObserver<Object[]> streamObservers) {
        this.streamObservers = streamObservers;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Object[] methodParameters = new Object[args.length];
        methodParameters[0] = method.getName();

        int index = 1;
        for (Object arg : args) {
            methodParameters[index] = arg;
            index++;
        }

        System.out.println("Ca;;;;;;;;;;;;;;;");
        this.streamObservers.onNext(methodParameters);
        return proxy;
    }
}
