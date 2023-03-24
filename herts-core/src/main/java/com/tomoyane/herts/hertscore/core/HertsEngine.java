package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.descriptor.HertsUnaryDescriptor;
import com.tomoyane.herts.hertscommon.marshaller.HertsMethod;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.descriptor.HertsStreamingDescriptor;
import com.tomoyane.herts.hertscore.handler.HertsCoreCStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreSStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreBMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreUMethodHandler;

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
    private HertsCoreType hertsCoreType;

    public HertsEngine() {
    }

    public HertsEngine(int port) {
        this.port = port;
    }

    public void register(HertsCoreBase core) {
        this.hertsCoreType = core.getCoreType();
        switch (core.getCoreType()) {
            case Unary:
                registerUnaryService((UnaryServiceCore) core);
                break;
            case BidirectionalStreaming:
                registerBidirectionalStreamingService((BidirectionalStreamingServiceCore) core);
                break;
            case ServerStreaming:
                registerServerStreamingService((ServerStreamingServiceCore) core);
                break;
            case ClientStreaming:
                registerClientStreamingService((ClientStreamingServiceCore) core);
                break;
        }
    }

    public void registerCustomService(BindableService grpcService, HertsCoreType hertsCoreType) {
        this.hertsCoreType = hertsCoreType;
        this.bindableService = grpcService;
    }

    public void start() {
        try {
            var server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                    .addService(this.bindableService)
                    .build()
                    .start();

            logger.info("Started Herts server " + hertsCoreType);
            server.awaitTermination();
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void registerBidirectionalStreamingService(BidirectionalStreamingServiceCore core) {
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
                    hertsMethod.setHertsCoreType(HertsCoreType.BidirectionalStreaming);
                    hertsMethod.setCoreServiceName(serviceName);
                    hertsMethod.setMethodName(method.getName());
                    hertsMethod.setMethodReturnType(method.getReturnType());
                    hertsMethod.setParameters(method.getParameterTypes());
                    hertsMethods.add(hertsMethod);
                }

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(serviceName, hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsCoreBMethodHandler<Object, Object> handler = new HertsCoreBMethodHandler<>(hertsMethods.get(index));
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncBidiStreamingCall(handler));
                    index++;
                }
                return builder.build();
            }
        };
    }

    private void registerClientStreamingService(ClientStreamingServiceCore core) {
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
                    hertsMethod.setHertsCoreType(HertsCoreType.ServerStreaming);
                    hertsMethod.setCoreServiceName(serviceName);
                    hertsMethod.setMethodName(method.getName());
                    hertsMethod.setMethodReturnType(method.getReturnType());
                    hertsMethod.setParameters(method.getParameterTypes());
                    hertsMethods.add(hertsMethod);
                }
                logger.info(hertsMethods.toString());

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(serviceName, hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsCoreCStreamingMethodHandler<Object, Object> handler = new HertsCoreCStreamingMethodHandler<>(hertsMethods.get(index));
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncClientStreamingCall(handler));
                    index++;
                }
                return builder.build();
            }
        };
    }

    private void registerServerStreamingService(ServerStreamingServiceCore core) {
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
                    hertsMethod.setHertsCoreType(HertsCoreType.ServerStreaming);
                    hertsMethod.setCoreServiceName(serviceName);
                    hertsMethod.setMethodName(method.getName());
                    hertsMethod.setMethodReturnType(method.getReturnType());
                    hertsMethod.setParameters(method.getParameterTypes());
                    hertsMethods.add(hertsMethod);
                }
                logger.info(hertsMethods.toString());

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(serviceName, hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsCoreSStreamingMethodHandler<Object, Object> handler = new HertsCoreSStreamingMethodHandler<>(hertsMethods.get(index));
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncServerStreamingCall(handler));
                    index++;
                }
                return builder.build();
            }
        };
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
                    hertsMethod.setHertsCoreType(HertsCoreType.Unary);
                    hertsMethod.setCoreServiceName(serviceName);
                    hertsMethod.setMethodName(method.getName());
                    hertsMethod.setMethodReturnType(method.getReturnType());
                    hertsMethod.setParameters(method.getParameterTypes());
                    hertsMethods.add(hertsMethod);
                }

                HertsUnaryDescriptor descriptor = HertsGrpcDescriptor.generateGrpcDescriptor(serviceName, hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<byte[], byte[]> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsCoreUMethodHandler<byte[], byte[]> handler = new HertsCoreUMethodHandler<>(hertsMethods.get(index));
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncUnaryCall(handler));
                    index++;
                }
                return builder.build();
            }
        };
    }
}
