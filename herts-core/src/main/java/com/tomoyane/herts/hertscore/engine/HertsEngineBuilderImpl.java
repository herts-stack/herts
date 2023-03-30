package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsCoreBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsNotSupportParameterTypeException;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.descriptor.HertsUnaryDescriptor;
import com.tomoyane.herts.hertscommon.marshaller.HertsMethod;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.descriptor.HertsStreamingDescriptor;
import com.tomoyane.herts.hertscore.BidirectionalStreamingCoreServiceCore;
import com.tomoyane.herts.hertscore.ClientStreamingCoreServiceCore;
import com.tomoyane.herts.hertscore.HertsCoreInterceptor;
import com.tomoyane.herts.hertscore.HertsCoreInterceptorBuilderImpl;
import com.tomoyane.herts.hertscore.ServerStreamingCoreServiceCore;
import com.tomoyane.herts.hertscore.UnaryCoreServiceCore;
import com.tomoyane.herts.hertscore.handler.HertsCoreCStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreSStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreBMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreUMethodHandler;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertscore.model.ReflectMethod;
import com.tomoyane.herts.hertscore.validator.HertsServiceValidator;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Metadata;
import io.grpc.ServerBuilder;
import io.grpc.ServerCall;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HertsEngineBuilderImpl implements HertsEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsEngineBuilderImpl.class.getSimpleName());

    private final Map<BindableService, ServerInterceptor> services;
    private final List<HertsCoreType> hertsCoreTypes;
    private final List<HertsCoreService> hertsCoreServices;
    private final GrpcServerOption option;
    private final ServerCredentials credentials;

    private Server server;

    private HertsEngineBuilderImpl(Builder builder) {
        this.option = builder.getOption();
        this.credentials = builder.getCredentials();
        this.hertsCoreTypes = builder.getHertsCoreTypes();
        this.services = builder.getServices();
        this.hertsCoreServices = builder.getHertsServices();
    }

    public static class Builder implements HertsEngineBuilder {
        private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
        private final List<HertsCoreType> hertsCoreTypes = new ArrayList<>();
        private final List<HertsCoreService> hertsCoreServices = new ArrayList<>();
        private GrpcServerOption option;
        private ServerCredentials credentials;

        private Builder() {
            this.option = new GrpcServerOption();
        }

        private Builder(GrpcServerOption option) {
            this.option = option;
        }

        public static HertsEngineBuilder create(GrpcServerOption option) {
            return new Builder(option);
        }

        public static HertsEngineBuilder create() {
            return new Builder();
        }

        @Override
        public List<HertsCoreService> getHertsServices() {
            return hertsCoreServices;
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
        public GrpcServerOption getOption() {
            return option;
        }

        @Override
        public ServerCredentials getCredentials() {
            return credentials;
        }

        @Override
        public HertsEngineBuilder addService(HertsCoreService hertsCoreService, @Nullable ServerInterceptor interceptor) {
            if (hertsCoreService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }

            this.hertsCoreServices.add(hertsCoreService);
            this.hertsCoreTypes.add(hertsCoreService.getHertsCoreType());

            BindableService bindableService;
            switch (hertsCoreService.getHertsCoreType()) {
                case Unary:
                    bindableService = registerUnaryService((UnaryCoreServiceCore) hertsCoreService);
                    break;
                case BidirectionalStreaming:
                    bindableService = registerBidirectionalStreamingService((BidirectionalStreamingCoreServiceCore) hertsCoreService);
                    break;
                case ServerStreaming:
                    bindableService = registerServerStreamingService((ServerStreamingCoreServiceCore) hertsCoreService);
                    break;
                case ClientStreaming:
                    bindableService = registerClientStreamingService((ClientStreamingCoreServiceCore) hertsCoreService);
                    break;
                default:
                    throw new HertsCoreBuildException("HertsCoreType is invalid");
            }

            if (interceptor == null) {
                var defaultInterceptor = new HertsCoreInterceptor() {
                    @Override
                    public void setResponseMetadata(Metadata metadata) {
                    }
                    @Override
                    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {

                    }
                };
                this.services.put(bindableService, HertsCoreInterceptorBuilderImpl.Builder.create(defaultInterceptor).build());
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

            var validateMsg = HertsServiceValidator.validateRegisteredServices(this.hertsCoreServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreBuildException(validateMsg);
            }

            if (!HertsServiceValidator.isValidStreamingRpc(this.hertsCoreServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
            return new HertsEngineBuilderImpl(this);
        }
    }

    @Override
    public void start() {
        try {
            ServerBuilder<?> serverBuilder;

            if (this.credentials != null) {
                serverBuilder = Grpc.newServerBuilderForPort(this.option.getPort(), this.credentials);
            } else {
                serverBuilder = Grpc.newServerBuilderForPort(this.option.getPort(), InsecureServerCredentials.create());
            }

            for (Map.Entry<BindableService, ServerInterceptor> service : this.services.entrySet()) {
                serverBuilder = serverBuilder.addService(service.getKey());
                if (service.getValue() != null) {
                    serverBuilder = serverBuilder.addService(ServerInterceptors.intercept(service.getKey(), service.getValue()));
                }
            }

            if (this.option.getMaxInboundMessageSize() > 0) {
                serverBuilder = serverBuilder.maxInboundMessageSize(this.option.getMaxInboundMessageSize());
            }
            if (this.option.getKeepaliveTimeMilliSec() != null) {
                serverBuilder = serverBuilder.keepAliveTime(this.option.getKeepaliveTimeMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getKeepaliveTimeoutMilliSec() != null) {
                serverBuilder = serverBuilder.keepAliveTimeout(this.option.getKeepaliveTimeoutMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getHandshakeTimeoutMilliSec() != null) {
                serverBuilder = serverBuilder.handshakeTimeout(this.option.getHandshakeTimeoutMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getMaxConnectionAgeMilliSec() != null) {
                serverBuilder = serverBuilder.maxConnectionAge(this.option.getMaxConnectionAgeMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getMaxConnectionIdleMilliSec() != null) {
                serverBuilder = serverBuilder.maxConnectionIdle(this.option.getMaxConnectionIdleMilliSec(), TimeUnit.MILLISECONDS);
            }
            this.server = serverBuilder.build();
            this.server.start();
            logger.info("Started Herts server. gRPC type " + this.hertsCoreTypes.get(0) + " Port " + this.option.getPort());
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

    private static BindableService registerBidirectionalStreamingService(BidirectionalStreamingCoreServiceCore core) {
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

    private static BindableService registerClientStreamingService(ClientStreamingCoreServiceCore core) {
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

    private static BindableService registerServerStreamingService(ServerStreamingCoreServiceCore core) {
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

    private static BindableService registerUnaryService(UnaryCoreServiceCore core) {
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
