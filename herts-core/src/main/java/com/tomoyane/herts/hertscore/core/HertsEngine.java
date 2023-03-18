package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.exceptions.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.mapping.HertsDescriptor;
import com.tomoyane.herts.hertscommon.mapping.HertsMethod;
import com.tomoyane.herts.hertscommon.descriptors.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscore.handler.HertsCoreMethodHandler;

import io.grpc.BindableService;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.MethodDescriptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HertsEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsEngine.class.getSimpleName());

    private int port = 9000;
    private BindableService bindableService;

    public HertsEngine() {
    }

    public HertsEngine(int port) {
        this.port = port;
    }

    public void register(HertsCoreBase core) {
        switch (core.getRpcType()) {
            case Unary:
                registerUnaryService((UnaryServiceCore) core);
                break;
            case ClientStreaming:
                registerUnaryService((UnaryServiceCore) core);
                break;
            case ServerStreaming:
                registerUnaryService((UnaryServiceCore) core);
                break;
            case BidirectionalStreaming:
                registerUnaryService((UnaryServiceCore) core);
                break;
        }
    }

    public void register(BindableService grpcService) {
    }

    public void start() {
        try {
            var server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                    .addService(this.bindableService)
                    .build()
                    .start();

            logger.info("Started Herts server");
            server.awaitTermination();
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void registerUnaryService(UnaryServiceCore core) {
        String serviceName = core.getClass().getName();
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + UnaryServiceCore.class.getName());
        }

        Method[] methods = thisClass.getDeclaredMethods();
        this.bindableService = new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {

                List<HertsMethod> hertsMethods = new ArrayList<>();
                for (Method method : methods) {
                    HertsMethod hertsMethod = new HertsMethod();
                    hertsMethod.setMethodType(MethodDescriptor.MethodType.UNARY);
                    hertsMethod.setCoreServiceName(serviceName);
                    hertsMethod.setMethodName(method.getName());
                    hertsMethod.setMethodReturnType(method.getReturnType());
                    hertsMethod.setParameters(method.getParameterTypes());
                    hertsMethods.add(hertsMethod);
                }

                HertsDescriptor descriptor = HertsGrpcDescriptor.getGrpcDescriptor(serviceName, hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<byte[], byte[]> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsCoreMethodHandler<byte[], byte[]> handler = new HertsCoreMethodHandler<>(hertsMethods.get(index));
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncUnaryCall(handler));
                    index++;
                }
                return builder.build();
            }
        };
    }
}
