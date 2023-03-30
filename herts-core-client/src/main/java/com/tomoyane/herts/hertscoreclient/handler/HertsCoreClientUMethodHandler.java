package com.tomoyane.herts.hertscoreclient.handler;

import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.marshaller.HertsMsg;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HertsCoreClientUMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsCoreClientUMethodHandler> implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer();
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final Map<String, MethodDescriptor<byte[], byte[]>> descriptors = new HashMap<>();
    private final HertsCoreService hertsCoreService;
    private final String serviceName;

    public HertsCoreClientUMethodHandler(Channel channel, CallOptions callOptions, HertsCoreService hertsCoreService) {
        super(channel, callOptions);
        this.hertsCoreService = hertsCoreService;
        this.serviceName = hertsCoreService.getClass().getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(this.serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + HertsCoreService.class.getName());
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
                    .generateMethodDescriptor(HertsCoreType.Unary, this.serviceName, methodName);
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
    protected HertsCoreClientUMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsCoreClientUMethodHandler(channel, callOptions, hertsCoreService);
    }
}
