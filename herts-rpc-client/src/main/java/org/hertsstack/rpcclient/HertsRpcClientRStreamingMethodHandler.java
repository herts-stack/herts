package org.hertsstack.rpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ClientCalls;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.descriptor.CustomGrpcDescriptor;
import org.hertsstack.core.exception.ServiceNotFoundException;
import org.hertsstack.core.exception.rpc.RpcErrorException;
import org.hertsstack.core.modelx.InternalRpcMsg;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.core.service.HertsService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts rpc client handler
 * Reactive streaming Method Handler
 *
 * @author Herts Contributer
 */
class HertsRpcClientRStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientRStreamingMethodHandler> implements InvocationHandler {
    private final MessageSerializer serializer = new MessageSerializer();
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
            throw new ServiceNotFoundException("Unknown class name. Allowed class is " + HertsService.class.getName());
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
            methodDescriptor = CustomGrpcDescriptor
                    .generateMethodDescriptor(HertsType.Unary, this.serviceName, methodName);
            this.descriptors.put(methodName, methodDescriptor);
        }

        Method cachedMethod = this.methodInfos.get(methodName);
        Class<?> returnType = cachedMethod.getReturnType();
        byte[] bytes = new byte[]{};
        if (args != null) {
            Class<?>[] parameterTypes = cachedMethod.getParameterTypes();
            bytes = this.serializer.serialize(new InternalRpcMsg(args, parameterTypes));
        }

        byte[] res;
        try {
            res = ClientCalls.blockingUnaryCall(getChannel(), methodDescriptor, getCallOptions(), bytes);
        } catch (StatusRuntimeException ex) {
            throw new RpcErrorException(ex);
        }
        if (returnType.getName().equals("void")) {
            return null;
        }
        if (res == null || res.length == 0) {
            return null;
        }
        return this.serializer.deserialize(res, returnType);
    }

    @Override
    protected HertsRpcClientRStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientRStreamingMethodHandler(channel, callOptions, hertsRpcService);
    }
}
