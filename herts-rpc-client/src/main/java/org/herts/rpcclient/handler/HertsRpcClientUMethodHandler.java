package org.herts.rpcclient.handler;

import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.context.HertsMsg;
import org.herts.common.serializer.HertsSerializer;
import org.herts.common.service.HertsRpcService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HertsRpcClientUMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsRpcClientUMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final Map<String, MethodDescriptor<byte[], byte[]>> descriptors = new HashMap<>();
    private final HertsRpcService hertsRpcService;
    private final String serviceName;

    public HertsRpcClientUMethodHandler(Channel channel, CallOptions callOptions, HertsRpcService hertsRpcService) {
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
        MethodDescriptor<byte[], byte[]> methodDescriptor = this.descriptors.get(methodName);
        if (methodDescriptor == null) {
            methodDescriptor = HertsGrpcDescriptor
                    .generateMethodDescriptor(HertsType.Unary, this.serviceName, methodName);
            this.descriptors.put(methodName, methodDescriptor);
        }

        byte[] bytes = new byte[]{};
        if (args != null) {
            int index = 0;
            Class<?>[] classTypes = new Class<?>[args.length];
            for (Object arg : args) {
                classTypes[index] = arg.getClass();
                index++;
            }
            bytes = this.serializer.serialize(new HertsMsg(args, classTypes));
        }

        var res = ClientCalls.blockingUnaryCall(getChannel(), methodDescriptor, getCallOptions(), bytes);
        return this.serializer.deserialize(res, this.methodTypes.get(methodName));
    }

    @Override
    protected HertsRpcClientUMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsRpcClientUMethodHandler(channel, callOptions, hertsRpcService);
    }
}
