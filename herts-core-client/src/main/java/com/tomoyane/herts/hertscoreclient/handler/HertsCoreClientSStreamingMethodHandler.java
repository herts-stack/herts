package com.tomoyane.herts.hertscoreclient.handler;

import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.exception.HertsStreamingResBodyException;
import com.tomoyane.herts.hertscommon.context.HertsMsg;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
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

public class HertsCoreClientSStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsCoreClientSStreamingMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final Map<String, MethodDescriptor<Object, Object>> descriptors = new HashMap<>();
    private final HertsCoreService hertsCoreService;
    private final String serviceName;

    public HertsCoreClientSStreamingMethodHandler(Channel channel, CallOptions callOptions, HertsCoreService hertsCoreService) {
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
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        MethodDescriptor<Object, Object> methodDescriptor = this.descriptors.get(methodName);
        if (methodDescriptor == null) {
            methodDescriptor = HertsGrpcDescriptor
                    .generateStramingMethodDescriptor(HertsType.ServerStreaming, this.serviceName, methodName);
            this.descriptors.put(methodName, methodDescriptor);
        }

        byte[] requestBytes;
        var index = 0;
        Object[] req = new Object[args.length-1];
        Class<?>[] classTypes = new Class<?>[args.length];
        Object observer = null;
        for (Object obj : args) {
            if (obj instanceof StreamObserver) {
                observer = obj;
            } else {
                req[index] = obj;
            }
            classTypes[index] = obj.getClass();
            index++;
        }

        requestBytes = this.serializer.serialize(new HertsMsg(req, classTypes));
        StreamObserver<Object> responseObserver = (StreamObserver<Object>) observer;
        if (responseObserver == null) {
            throw new HertsStreamingResBodyException("Streaming response observer body data is null");
        }
        ClientCalls.asyncServerStreamingCall(getChannel().newCall(methodDescriptor, getCallOptions()), requestBytes, responseObserver);
        return proxy;
    }

    @Override
    protected HertsCoreClientSStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsCoreClientSStreamingMethodHandler(channel, callOptions, hertsCoreService);
    }
}
