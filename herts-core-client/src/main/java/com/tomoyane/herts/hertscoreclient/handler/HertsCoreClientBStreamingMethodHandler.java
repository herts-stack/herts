package com.tomoyane.herts.hertscoreclient.handler;

import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.exception.HertsStreamingReqBodyException;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HertsCoreClientBStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsCoreClientBStreamingMethodHandler> implements InvocationHandler {
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final Map<String, MethodDescriptor<Object, Object>> descriptors = new HashMap<>();
    private final HertsCoreService hertsCoreService;
    private final String serviceName;

    public HertsCoreClientBStreamingMethodHandler(Channel channel, CallOptions callOptions, HertsCoreService hertsCoreService) {
        super(channel, callOptions);
        this.hertsCoreService = hertsCoreService;
        this.serviceName = hertsCoreService.getClass().getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(this.serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + HertsCoreService.class.getName());
        }

        Method[] methods = hertsServiceClass.getDeclaredMethods();
        for (Method method : methods) {
            methodTypes.put(method.getName(), method.getReturnType());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String methodName = method.getName();
        MethodDescriptor<Object, Object> methodDescriptor = this.descriptors.get(methodName);
        if (methodDescriptor == null) {
            methodDescriptor = HertsGrpcDescriptor
                    .generateStramingMethodDescriptor(HertsType.BidirectionalStreaming, this.serviceName, methodName);
            this.descriptors.put(methodName, methodDescriptor);
        }

        StreamObserver<Object> bytes = null;
        if (args != null) {
            bytes = (StreamObserver<Object>) args[0];
        }
        if (bytes == null) {
            throw new HertsStreamingReqBodyException("Streaming body data is null");
        }
        return ClientCalls.asyncBidiStreamingCall(getChannel().newCall(methodDescriptor, getCallOptions()), bytes);
    }

    @Override
    protected HertsCoreClientBStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsCoreClientBStreamingMethodHandler(channel, callOptions, hertsCoreService);
    }
}
