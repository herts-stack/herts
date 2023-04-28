package org.herts.rpcclient.handler;

import org.herts.common.context.HertsType;
import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.exception.HertsStreamingResBodyException;
import org.herts.common.context.HertsMsg;
import org.herts.common.serializer.HertsSerializer;
import org.herts.common.service.HertsService;

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

public class HertsRpcClientSStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientSStreamingMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final ConcurrentMap<String, MethodDescriptor<Object, Object>> descriptors = new ConcurrentHashMap<>();
    private final Class<?> hertsRpcService;
    private final String serviceName;

    public HertsRpcClientSStreamingMethodHandler(Channel channel, CallOptions callOptions, Class<?> hertsRpcService) {
        super(channel, callOptions);
        this.hertsRpcService = hertsRpcService;
        this.serviceName = hertsRpcService.getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(this.serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + HertsService.class.getName());
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
        int index = 0;
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
