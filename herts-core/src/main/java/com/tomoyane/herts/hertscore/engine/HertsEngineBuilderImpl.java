package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsCoreBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.descriptor.HertsUnaryDescriptor;
import com.tomoyane.herts.hertscommon.marshaller.HertsMethod;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.descriptor.HertsStreamingDescriptor;
import com.tomoyane.herts.hertscore.BidirectionalStreamingServiceCore;
import com.tomoyane.herts.hertscore.ClientStreamingServiceCore;
import com.tomoyane.herts.hertscore.ServerStreamingServiceCore;
import com.tomoyane.herts.hertscore.UnaryServiceCore;
import com.tomoyane.herts.hertscore.handler.HertsCoreCStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreSStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreBMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreUMethodHandler;
import com.tomoyane.herts.hertscommon.service.HertsService;
import com.tomoyane.herts.hertscore.model.ReflectMethod;
import com.tomoyane.herts.hertscore.validator.HertsServiceValidator;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.ServerBuilder;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.stub.ServerCalls;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerServiceDefinition;
import io.grpc.MethodDescriptor;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HertsEngineBuilderImpl implements HertsEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsEngineBuilderImpl.class.getSimpleName());

    private final Map<BindableService, ServerInterceptor> services;
    private final List<HertsCoreType> hertsCoreTypes;
    private final List<HertsService> hertsServices;
    private final int port;
    private final ServerCredentials credentials;

    private Server server;

    private HertsEngineBuilderImpl(Builder builder) {
        this.port = builder.getPort();
        this.credentials = builder.getCredentials();
        this.hertsCoreTypes = builder.getHertsCoreTypes();
        this.services = builder.getServices();
        this.hertsServices = builder.getHertsServices();
    }

    public static class Builder implements HertsEngineBuilder {
        private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
        private final List<HertsCoreType> hertsCoreTypes = new ArrayList<>();
        private final List<HertsService> hertsServices = new ArrayList<>();
        private int port = 9000;
        private ServerCredentials credentials;

        private Builder() {
        }

        private Builder(int port) {
            this.port = port;
        }

        public static HertsEngineBuilder create(int port) {
            return new Builder(port);
        }

        public static HertsEngineBuilder create() {
            return new Builder();
        }

        @Override
        public List<HertsService> getHertsServices() {
            return hertsServices;
        }

        @Override
        public Map<BindableService, ServerInterceptor> getServices() {
            return services;
        }

        @Override
        public List<HertsCoreType> getHertsCoreTypes() {
            return hertsCoreTypes;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public ServerCredentials getCredentials() {
            return credentials;
        }

        @Override
        public HertsEngineBuilder addService(HertsService hertsService, @Nullable ServerInterceptor interceptor) {
            if (hertsService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }

            this.hertsServices.add(hertsService);
            this.hertsCoreTypes.add(hertsService.getHertsCoreType());

            BindableService bindableService;
            switch (hertsService.getHertsCoreType()) {
                case Unary:
                    bindableService = registerUnaryService((UnaryServiceCore) hertsService);
                    break;
                case BidirectionalStreaming:
                    bindableService = registerBidirectionalStreamingService((BidirectionalStreamingServiceCore) hertsService);
                    break;
                case ServerStreaming:
                    bindableService = registerServerStreamingService((ServerStreamingServiceCore) hertsService);
                    break;
                case ClientStreaming:
                    bindableService = registerClientStreamingService((ClientStreamingServiceCore) hertsService);
                    break;
                default:
                    throw new HertsCoreBuildException("HertsCoreType is invalid");
            }

            if (interceptor == null) {
                this.services.put(bindableService, null);
            } else {
                this.services.put(bindableService, interceptor);
            }
            return this;
        }

        @Override
        public HertsEngineBuilder secure(ServerCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        @Override
        public HertsEngineBuilder addCustomService(BindableService grpcService, HertsCoreType hertsCoreType, @Nullable ServerInterceptor interceptor) {
            if (grpcService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }

            this.hertsCoreTypes.add(hertsCoreType);
            this.services.put(grpcService, interceptor);
            return this;
        }

        @Override
        public HertsEngine build() {
            if (this.hertsCoreTypes.size() == 0 || this.services.size() == 0) {
                throw new HertsCoreBuildException("Please register HertsCoreService");
            }
            if (!HertsServiceValidator.isSameHertsCoreType(this.hertsCoreTypes)) {
                throw new HertsCoreBuildException("Please register same HertsCoreService. Not supported multiple different services");
            }

            var validateMsg = HertsServiceValidator.validateRegisteredServices(this.hertsServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreBuildException(validateMsg);
            }
            return new HertsEngineBuilderImpl(this);
        }
    }

    @Override
    public void start() {
        try {
            ServerBuilder<?> serverBuilder;

            if (this.credentials != null) {
                serverBuilder = Grpc.newServerBuilderForPort(this.port, this.credentials);
            } else {
                serverBuilder = Grpc.newServerBuilderForPort(this.port, InsecureServerCredentials.create());
            }

            for (Map.Entry<BindableService, ServerInterceptor> service : this.services.entrySet()) {
                serverBuilder = serverBuilder.addService(service.getKey());
                if (service.getValue() != null) {
                    serverBuilder = serverBuilder.addService(ServerInterceptors.intercept(service.getKey(), service.getValue()));
                }
            }

            this.server = serverBuilder.build();
            this.server.start();
            logger.info("Started Herts server. gRPC type " + this.hertsCoreTypes.get(0));
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

    @Override
    public HertsCoreType getHertCoreType() {
        if (this.hertsCoreTypes.size() == 0) {
            return null;
        }
        return this.hertsCoreTypes.get(0);
    }

    private static ReflectMethod generateReflectMethod(String serviceName) {
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + serviceName);
        }

        Method[] methods = thisClass.getDeclaredMethods();
        return ReflectMethod.create(serviceName, methods);
    }

    private static List<HertsMethod> generateHertsMethod(HertsCoreType coreType, Method[] methods, String serviceName) {
        List<HertsMethod> hertsMethods = new ArrayList<>();
        for (Method method : methods) {
            HertsMethod hertsMethod = new HertsMethod();
            hertsMethod.setHertsCoreType(coreType);
            hertsMethod.setCoreServiceName(serviceName);
            hertsMethod.setMethodName(method.getName());
            hertsMethod.setMethodReturnType(method.getReturnType());
            hertsMethod.setParameters(method.getParameterTypes());
            hertsMethods.add(hertsMethod);
        }
        return hertsMethods;
    }

    private static BindableService registerBidirectionalStreamingService(BidirectionalStreamingServiceCore core) {
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsCoreType.BidirectionalStreaming, reflectMethod.getMethods(), reflectMethod.getClassName());
                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getClassName(), hertsMethods);
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
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsCoreType.ClientStreaming, reflectMethod.getMethods(), reflectMethod.getClassName());
                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getClassName(), hertsMethods);
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
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsCoreType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getClassName());
                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getClassName(), hertsMethods);
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
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsCoreType.Unary, reflectMethod.getMethods(), reflectMethod.getClassName());
                HertsUnaryDescriptor descriptor = HertsGrpcDescriptor.generateGrpcDescriptor(reflectMethod.getClassName(), hertsMethods);
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
