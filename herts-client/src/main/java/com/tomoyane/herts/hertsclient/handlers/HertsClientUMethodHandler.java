package com.tomoyane.herts.hertsclient.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.marshaller.HertsMsg;

import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HertsClientUMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsClientUMethodHandler> implements InvocationHandler {
    private static final Logger logger = HertsLogger.getLogger(HertsClientUMethodHandler.class.getSimpleName());

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final HertsService hertsService;
    private final String serviceName;

    public HertsClientUMethodHandler(Channel channel, CallOptions callOptions, HertsService hertsService) {
        super(channel, callOptions);
        this.hertsService = hertsService;
        this.serviceName = hertsService.getClass().getName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(this.serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + HertsService.class.getName());
        }

        Method[] methods = hertsServiceClass.getDeclaredMethods();
        for (Method method : methods) {
            methodTypes.put(method.getName(), method.getReturnType());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        MethodDescriptor<byte[], byte[]> methodDescriptor = HertsGrpcDescriptor
                .generateMethodDescriptor(HertsCoreType.Unary, this.serviceName, methodName);

        byte[] bytes = new byte[]{};
        if (args != null) {
            int index = 0;
            Class<?>[] classTypes = new Class<?>[args.length];
            for (Object arg : args) {
                classTypes[index] = arg.getClass();
                index++;
            }
            bytes = this.objectMapper.writeValueAsBytes(new HertsMsg(args, classTypes));
        }

        var res = ClientCalls.blockingUnaryCall(getChannel(), methodDescriptor, getCallOptions(), bytes);
        return this.objectMapper.readValue(res, this.methodTypes.get(methodName));
    }

    private Object byteArrayToObject(byte[] bytes, Class<?> classType) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    @Override
    protected HertsClientUMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsClientUMethodHandler(channel, callOptions, hertsService);
    }
}
