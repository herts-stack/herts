package com.tomoyane.herts.hertsclient.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.marshaller.HertsMsg;
import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HertsClientServerStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsClientServerStreamingMethodHandler> implements InvocationHandler {
    private static final Logger logger = HertsLogger.getLogger(HertsClientServerStreamingMethodHandler.class.getSimpleName());

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final HertsService hertsService;
    private final String serviceName;

    public HertsClientServerStreamingMethodHandler(Channel channel, CallOptions callOptions, HertsService hertsService) {
        super(channel, callOptions);
        this.hertsService = hertsService;
        this.serviceName = hertsService.getClass().getName();
//        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

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
        MethodDescriptor<Object, Object> methodDescriptor = HertsGrpcDescriptor
                .generateStramingMethodDescriptor(HertsCoreType.ServerStreaming, this.serviceName, methodName);

        byte[] requestBytes = new byte[]{};

        System.out.println(args.length);
        var index = 0;
        Object[] req = new Object[args.length-1];
        Class<?>[] classTypes = new Class<?>[args.length];
        Object observer = null;
        for (Object obj : args) {
            System.out.println(obj.getClass().getSimpleName());
            if (obj instanceof StreamObserver) {
                observer = obj;
            } else {
                req[index] = obj;
            }
            classTypes[index] = obj.getClass();
            index++;
        }

        if (args != null) {
            requestBytes = this.objectMapper.writeValueAsBytes(new HertsMsg(req, classTypes));
        }

        StreamObserver<Object> responseObserver = null;
        if (args != null) {
            responseObserver = (StreamObserver<Object>) observer;
        }
        if (responseObserver == null) {
            throw new RuntimeException("sasasasasasasaasasa");
        }
        ClientCalls.asyncServerStreamingCall(getChannel().newCall(methodDescriptor, getCallOptions()), requestBytes, responseObserver);
        return proxy;
    }

    @Override
    protected HertsClientServerStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsClientServerStreamingMethodHandler(channel, callOptions, hertsService);
    }
}
