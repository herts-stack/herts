package com.tomoyane.herts.hertsclient.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomoyane.herts.hertscommon.descriptors.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.exceptions.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.mapping.HertsMsg;

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

public class HertsClientBlockingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsClientBlockingMethodHandler> implements InvocationHandler {
    private static final Logger logger = HertsLogger.getLogger(HertsClientBlockingMethodHandler.class.getSimpleName());

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final HertsService hertsService;
    private final String serviceName;

    public HertsClientBlockingMethodHandler(Channel channel, CallOptions callOptions, HertsService hertsService) {
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
                .generateMethodDescriptor(MethodDescriptor.MethodType.UNARY, this.serviceName, methodName);

        byte[] bytes = new byte[]{};
        if (args != null) {
            int index = 0;
            Map<String, Object> d = new HashMap<>();
            for (Object arg : args) {
                d.put("key_" + index, arg);
                index++;
            }

            HertsMsg msg = new HertsMsg(d);
            bytes = this.objectMapper.writeValueAsBytes(msg);
        }

        var res = ClientCalls.blockingUnaryCall(getChannel(), methodDescriptor, getCallOptions(), bytes);
//        System.out.println(new String(res));
//        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(res));
//        String a = SerializationUtils.deserialize(bis);
//        System.out.println(a);

        var responseBytes = this.objectMapper.readValue(res, this.methodTypes.get(methodName));
//        System.out.println(responseBytes);

        return responseBytes;
    }

    private Object byteArrayToObject(byte[] bytes, Class<?> classType) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    @Override
    protected HertsClientBlockingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsClientBlockingMethodHandler(channel, callOptions, hertsService);
    }
}
