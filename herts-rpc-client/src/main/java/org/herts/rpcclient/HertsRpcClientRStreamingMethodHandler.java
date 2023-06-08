package org.herts.rpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ClientCalls;
import org.herts.core.modelx.HertsRpcMsg;
import org.herts.core.context.HertsType;
import org.herts.core.descriptor.HertsGrpcDescriptor;
import org.herts.core.exception.HertsServiceNotFoundException;
import org.herts.core.exception.rpc.HertsRpcErrorException;
import org.herts.core.serializer.HertsSerializer;
import org.herts.core.service.HertsService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts rpc client handler
 * Reactive streaming Method Handler
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcClientRStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientRStreamingMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final ConcurrentMap<String, Method> methodInfos = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, MethodDescriptor<byte[], byte[]>> descriptors = new ConcurrentHashMap<>();
    private final Class<?> hertsRpcService;
    private final String serviceName;

    public HertsRpcClientRStreamingMethodHandler(Channel channel, CallOptions callOptions, Class<?> hertsRpcService) {
        super(channel, callOptions);
        this.hertsRpcService = hertsRpcService;
        this.serviceName = hertsRpcService.getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(hertsRpcService.getName());
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
        MethodDescriptor<byte[], byte[]> methodDescriptor = this.descriptors.get(methodName);
        if (methodDescriptor == null) {
            methodDescriptor = HertsGrpcDescriptor
                    .generateMethodDescriptor(HertsType.Unary, this.serviceName, methodName);
            this.descriptors.put(methodName, methodDescriptor);
        }

        Method cachedMethod = this.methodInfos.get(methodName);
        Class<?> returnType = cachedMethod.getReturnType();
        byte[] bytes = new byte[]{};
        if (args != null) {
            Class<?>[] parameterTypes = cachedMethod.getParameterTypes();
            bytes = this.serializer.serialize(new HertsRpcMsg(args, parameterTypes));
        }

        byte[] res;
        try {
            res = ClientCalls.blockingUnaryCall(getChannel(), methodDescriptor, getCallOptions(), bytes);
        } catch (StatusRuntimeException ex) {
            throw new HertsRpcErrorException(ex);
        }
        if (returnType.getName().equals("void")) {
            return null;
        }
        return this.serializer.deserialize(res, returnType);
    }

    @Override
    protected HertsRpcClientRStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientRStreamingMethodHandler(channel, callOptions, hertsRpcService);
    }
}
