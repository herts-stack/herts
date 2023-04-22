package com.tomoyane.herts.hertsrpcclient.handler;

import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.exception.HertsStreamingResBodyException;
import com.tomoyane.herts.hertscommon.context.HertsMsg;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertscommon.service.HertsRpcService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HertsRpcClientSStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientSStreamingMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final Map<String, MethodDescriptor<Object, Object>> descriptors = new HashMap<>();
    private final HertsRpcService hertsRpcService;
    private final String serviceName;

    public HertsRpcClientSStreamingMethodHandler(Channel channel, CallOptions callOptions, HertsRpcService hertsRpcService) {
        super(channel, callOptions);
        this.hertsRpcService = hertsRpcService;
        this.serviceName = hertsRpcService.getClass().getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(this.serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + HertsRpcService.class.getName());
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
    protected HertsRpcClientSStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientSStreamingMethodHandler(channel, callOptions, hertsRpcService);
    }
}
