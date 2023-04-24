package org.herts.rpcclient.handler;

import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.exception.HertsStreamingReqBodyException;
import org.herts.common.service.HertsRpcService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HertsRpcClientBStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientBStreamingMethodHandler> implements InvocationHandler {
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final Map<String, MethodDescriptor<Object, Object>> descriptors = new HashMap<>();
    private final HertsRpcService hertsRpcService;
    private final String serviceName;

    public HertsRpcClientBStreamingMethodHandler(Channel channel, CallOptions callOptions, HertsRpcService hertsRpcService) {
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
    protected HertsRpcClientBStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientBStreamingMethodHandler(channel, callOptions, hertsRpcService);
    }
}
