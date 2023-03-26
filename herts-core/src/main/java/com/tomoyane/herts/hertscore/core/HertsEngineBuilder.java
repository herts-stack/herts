package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsCoreBuildException;
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
import com.tomoyane.herts.hertscore.service.HertsService;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.stub.ServerCalls;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerServiceDefinition;
import io.grpc.MethodDescriptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HertsEngineBuilder implements HertsEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsEngineBuilder.class.getSimpleName());

    private final BindableService bindableService;
    private final HertsCoreType hertsCoreType;
    private final ServerInterceptor interceptor;
    private final int port;

    private Server server;

    private HertsEngineBuilder(Builder builder) {
        this.port = builder.port;
        this.bindableService = builder.bindableService;
        this.hertsCoreType = builder.hertsCoreType;
        this.interceptor = builder.interceptor;
    }

    public static class Builder {
        private int port = 9000;
        private HertsCoreType hertsCoreType;
        private BindableService bindableService;
        private ServerInterceptor interceptor;

        private Builder() {
        }

        private Builder(int port) {
            this.port = port;
        }

        public static Builder create(int port) {
            return new Builder(port);
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder service(HertsService hertsService) {
            this.hertsCoreType = hertsService.getHertsCoreType();
            switch (hertsService.getHertsCoreType()) {
                case Unary:
                    this.bindableService = registerUnaryService((UnaryServiceCore) hertsService);
                    break;
                case BidirectionalStreaming:
                    this.bindableService = registerBidirectionalStreamingService((BidirectionalStreamingServiceCore) hertsService);
                    break;
                case ServerStreaming:
                    this.bindableService = registerServerStreamingService((ServerStreamingServiceCore) hertsService);
                    break;
                case ClientStreaming:
                    this.bindableService = registerClientStreamingService((ClientStreamingServiceCore) hertsService);
                    break;
            }
            return this;
        }

        public Builder customService(BindableService grpcService, HertsCoreType hertsCoreType) {
            this.hertsCoreType = hertsCoreType;
            this.bindableService = grpcService;
            return this;
        }

        public Builder interceptor(ServerInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public HertsEngine build() {
            if (this.hertsCoreType == null || this.bindableService == null) {
                throw new HertsCoreBuildException("Please register HertsCoreService");
            }
            return new HertsEngineBuilder(this);
        }
    }

    @Override
    public void start() {
        try {
            var builder = Grpc
                    .newServerBuilderForPort(this.port, InsecureServerCredentials.create())
                    .addService(this.bindableService);

            if (this.interceptor != null) {
                builder = builder.addService(ServerInterceptors.intercept(this.bindableService, interceptor));
            }

            this.server = builder.build();
            this.server.start();
            logger.info("Started Herts server " + hertsCoreType);
            server.awaitTermination();
        } catch (IOException | InterruptedException ex) {
            throw new HertsCoreBuildException(ex);
        }
    }

    @Override
    public Server getServer() {
        if (this.server == null) {
            return null;
        }
        return this.server;
    }

    private static BindableService registerBidirectionalStreamingService(BidirectionalStreamingServiceCore core) {
        String serviceName = core.getClass().getName();
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + UnaryServiceCore.class.getName());
        }

        Method[] methods = thisClass.getDeclaredMethods();
        return new BindableService() {
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

    private static BindableService registerClientStreamingService(ClientStreamingServiceCore core) {
        String serviceName = core.getClass().getName();
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + UnaryServiceCore.class.getName());
        }

        Method[] methods = thisClass.getDeclaredMethods();
        return new BindableService() {
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

    private static BindableService registerServerStreamingService(ServerStreamingServiceCore core) {
        String serviceName = core.getClass().getName();
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + UnaryServiceCore.class.getName());
        }

        Method[] methods = thisClass.getDeclaredMethods();
        return new BindableService() {
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

    private static BindableService registerUnaryService(UnaryServiceCore core) {
        String serviceName = core.getClass().getName();
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + UnaryServiceCore.class.getName());
        }

        Method[] methods = thisClass.getDeclaredMethods();
        return new BindableService() {
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
