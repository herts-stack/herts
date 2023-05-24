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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts rpc client handler
 * Server streaming Method Handler
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcClientSStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientSStreamingMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final ConcurrentMap<String, Method> methodInfos = new ConcurrentHashMap<>();
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
            this.methodInfos.put(method.getName(), method);
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

        int index = 0;
        List<Object> methodObservers = new ArrayList<>();
        Object[] methodParameters = new Object[args.length-1];
        Class<?>[] parameterTypes = this.methodInfos.get(methodName).getParameterTypes();
        for (Object arg : args) {
            if (arg instanceof StreamObserver) {
                methodObservers.add(arg);
            } else {
                methodParameters[index] = arg;
            }
            index++;
        }

        if (methodObservers.size() != 1) {
            throw new HertsStreamingResBodyException("Streaming response observer body data is null");
        }

        byte[] requestBytes = this.serializer.serialize(new HertsMsg(methodParameters, parameterTypes));
        StreamObserver<Object> responseObserver = (StreamObserver<Object>) methodObservers.get(0);
        ClientCalls.asyncServerStreamingCall(getChannel().newCall(methodDescriptor, getCallOptions()), requestBytes, responseObserver);
        return proxy;
    }

    @Override
    protected HertsRpcClientSStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientSStreamingMethodHandler(channel, callOptions, hertsRpcService);
    }
}
