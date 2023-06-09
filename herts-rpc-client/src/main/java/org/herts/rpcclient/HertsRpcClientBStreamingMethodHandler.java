package org.herts.rpcclient;

import org.herts.core.descriptor.CustomGrpcDescriptor;
import org.herts.core.context.HertsType;
import org.herts.core.exception.ServiceNotFoundException;
import org.herts.core.exception.StreamBodyException;
import org.herts.core.service.HertsService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts rpc client handler
 * Bidirectional streaming Method Handler
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcClientBStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientBStreamingMethodHandler> implements InvocationHandler {
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final ConcurrentMap<String, MethodDescriptor<Object, Object>> descriptors = new ConcurrentHashMap<>();
    private final Class<?> hertsRpcService;
    private final String serviceName;

    public HertsRpcClientBStreamingMethodHandler(Channel channel, CallOptions callOptions, Class<?> hertsRpcService) {
        super(channel, callOptions);
        this.hertsRpcService = hertsRpcService;
        this.serviceName = hertsRpcService.getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(this.serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new ServiceNotFoundException("Unknown class name. Allowed class is " + HertsService.class.getName());
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
            methodDescriptor = CustomGrpcDescriptor
                    .generateStramingMethodDescriptor(HertsType.BidirectionalStreaming, this.serviceName, methodName);
            this.descriptors.put(methodName, methodDescriptor);
        }

        StreamObserver<Object> bytes = null;
        if (args != null) {
            bytes = (StreamObserver<Object>) args[0];
        }
        if (bytes == null) {
            throw new StreamBodyException("Streaming body data is null");
        }
        return ClientCalls.asyncBidiStreamingCall(getChannel().newCall(methodDescriptor, getCallOptions()), bytes);
    }

    @Override
    protected HertsRpcClientBStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientBStreamingMethodHandler(channel, callOptions, hertsRpcService);
    }
}
