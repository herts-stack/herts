package com.tomoyane.herts.hertsclient.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
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

public class HertsClientBStreamingMethodHandler extends io.grpc.stub.AbstractBlockingStub<HertsClientBStreamingMethodHandler> implements InvocationHandler, HertsService {
    private static final Logger logger = HertsLogger.getLogger(HertsClientBStreamingMethodHandler.class.getSimpleName());

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
    private final Map<String, Class<?>> methodTypes = new HashMap<>();
    private final HertsService hertsService;
    private final String serviceName;

    public HertsClientBStreamingMethodHandler(Channel channel, CallOptions callOptions, HertsService hertsService) {
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
        MethodDescriptor<Object, Object> methodDescriptor = HertsGrpcDescriptor
                .generateStramingMethodDescriptor(HertsCoreType.BidirectionalStreaming, this.serviceName, methodName);

        StreamObserver<Object> bytes = null;
        if (args != null) {
            bytes = (StreamObserver<Object>) args[0];
        }
        if (bytes == null) {
            throw new RuntimeException("sasasasasasasaasasa");
        }

        System.out.println("===Client call===" + methodName);

        StreamObserver<Object> streamObserver = ClientCalls.asyncBidiStreamingCall(getChannel().newCall(methodDescriptor, getCallOptions()), bytes);

        System.out.println("===Client called ===" + methodName);
        return streamObserver;
    }

    @Override
    protected HertsClientBStreamingMethodHandler build(Channel channel, CallOptions callOptions) {
        return new HertsClientBStreamingMethodHandler(channel, callOptions, hertsService);
    }

    @Override
    public HertsCoreType getHertsCoreType() {
        return null;
    }

    @Override
    public MethodDescriptor.MethodType getGrpcMethodType() {
        return null;
    }

    @Override
    public String[] getConnections() {
        return new String[0];
    }
}
