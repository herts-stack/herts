package org.herts.common.service;

import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Herts reactive invocation handler for server side
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReactiveStreamingInvoker implements InvocationHandler {
    private final StreamObserver<Object> streamObservers;

    public HertsReactiveStreamingInvoker(StreamObserver<Object> streamObservers) {
        this.streamObservers = streamObservers;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        List<Object> methodParameters = new ArrayList<>();
        methodParameters.add(method.getName());
        if (args != null && args.length > 0) {
            methodParameters.addAll(Arrays.asList(args));
        }

        this.streamObservers.onNext(methodParameters);
        return proxy;
    }
}
