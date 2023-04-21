package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsMetricsSetting;
import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.exception.HertsCoreBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsNotSupportParameterTypeException;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.descriptor.HertsUnaryDescriptor;
import com.tomoyane.herts.hertscommon.context.HertsMethod;
import com.tomoyane.herts.hertscommon.descriptor.HertsGrpcDescriptor;
import com.tomoyane.herts.hertscommon.descriptor.HertsStreamingDescriptor;
import com.tomoyane.herts.hertscommon.service.BidirectionalStreamingCoreServiceCore;
import com.tomoyane.herts.hertscommon.service.ClientStreamingCoreServiceCore;
import com.tomoyane.herts.hertscommon.service.ServerStreamingCoreServiceCore;
import com.tomoyane.herts.hertscommon.service.UnaryCoreServiceCore;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertscore.HertsCoreInterceptor;
import com.tomoyane.herts.hertscore.HertsCoreInterceptBuilder;
import com.tomoyane.herts.hertscore.handler.HertsCoreCStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreSStreamingMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreBMethodHandler;
import com.tomoyane.herts.hertscore.handler.HertsCoreUMethodHandler;
import com.tomoyane.herts.hertscore.model.ReflectMethod;
import com.tomoyane.herts.hertscore.validator.HertsRpcValidator;
import com.tomoyane.herts.hertsmetrics.HertsMetrics;
import com.tomoyane.herts.hertsmetrics.handler.HertsMetricsHandler;
import com.tomoyane.herts.hertsmetrics.server.HertsMetricsServer;

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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HertsCoreBuilder implements HertsCoreEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsCoreBuilder.class.getSimpleName());

    private final Map<BindableService, ServerInterceptor> services;
    private final List<HertsType> hertsTypes;
    private final List<HertsCoreService> hertsCoreServices;
    private final GrpcServerOption option;
    private final ServerCredentials credentials;
    private final HertsMetricsServer hertsMetricsServer;
    private final HertsMetrics hertsMetrics;

    private Server server;

    private HertsCoreBuilder(Builder builder) {
        this.option = builder.getOption();
        this.credentials = builder.getCredentials();
        this.hertsTypes = builder.getHertsCoreTypes();
        this.services = builder.getServices();
        this.hertsCoreServices = builder.getHertsServices();
        this.hertsMetricsServer = builder.hertsMetricsServer;
        this.hertsMetrics = builder.hertsMetrics;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(GrpcServerOption option) {
        return new Builder(option);
    }

    public static class Builder implements HertsCoreEngineBuilder {
        private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
        private final List<HertsType> hertsTypes = new ArrayList<>();
        private final List<HertsCoreService> hertsCoreServices = new ArrayList<>();
        private GrpcServerOption option;
        private ServerCredentials credentials;
        private HertsMetricsServer hertsMetricsServer;
        private HertsMetrics hertsMetrics;

        private Builder() {
            this.option = new GrpcServerOption();
        }

        private Builder(GrpcServerOption option) {
            this.option = option;
        }

        private static HertsMetrics getHertsMetrics(List<HertsCoreService> coreServices, HertsMetricsSetting metricsSetting) {
            HertsMetrics hertsMetrics;
            if (metricsSetting != null) {
                hertsMetrics = HertsMetricsHandler.builder()
                        .hertsCoreServiceInterface(coreServices)
                        .isErrRateEnabled(metricsSetting.isErrRateEnabled())
                        .isJvmEnabled(metricsSetting.isJvmEnabled())
                        .isLatencyEnabled(metricsSetting.isLatencyEnabled())
                        .isServerResourceEnabled(metricsSetting.isServerResourceEnabled())
                        .isRpsEnabled(metricsSetting.isRpsEnabled())
                        .build();
            } else {
                hertsMetrics = HertsMetricsHandler.builder().hertsCoreServiceInterface(null).build();
            }
            return hertsMetrics;
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
        public List<HertsType> getHertsCoreTypes() {
            return hertsTypes;
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
        public HertsCoreEngineBuilder addService(HertsCoreService hertsCoreService, @Nullable ServerInterceptor interceptor) {
            if (hertsCoreService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }

            this.hertsCoreServices.add(hertsCoreService);
            this.hertsTypes.add(hertsCoreService.getHertsType());

            BindableService bindableService;
            switch (hertsCoreService.getHertsType()) {
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
                this.services.put(bindableService, HertsCoreInterceptBuilder.builder(defaultInterceptor).build());
            } else {
                this.services.put(bindableService, interceptor);
            }
            return this;
        }

        @Override
        public HertsCoreEngineBuilder secure(ServerCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        @Override
        public HertsCoreEngineBuilder enableMetrics(HertsMetricsSetting metricsSetting) {
            if (this.hertsCoreServices.size() == 0) {
                throw new HertsCoreBuildException("Please call addService before call enableMetrics");
            }
            this.hertsMetrics = getHertsMetrics(this.hertsCoreServices, metricsSetting);
            this.hertsMetrics.register();
            this.hertsMetricsServer = new HertsMetricsServer(this.hertsMetrics);
            return this;
        }

        @Override
        public HertsCoreEngineBuilder addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor) {
            if (grpcService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }
            this.hertsTypes.add(hertsType);
            this.services.put(grpcService, interceptor);
            return this;
        }

        @Override
        public HertsCoreEngine build() {
            if (this.hertsTypes.size() == 0 || this.services.size() == 0) {
                throw new HertsCoreBuildException("Please register HertsCoreService");
            }
            if (!HertsRpcValidator.isSameHertsCoreType(this.hertsTypes)) {
                throw new HertsCoreBuildException("Please register same HertsCoreService. Not supported multiple different services");
            }
            var validateMsg = HertsRpcValidator.validateRegisteredServices(this.hertsCoreServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreBuildException(validateMsg);
            }
            if (!HertsRpcValidator.isValidStreamingRpc(this.hertsCoreServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
            return new HertsCoreBuilder(this);
        }

        private static ReflectMethod generateReflectMethod(String serviceName) {
            Class<?> thisClass;
            try {
                thisClass = Class.forName(serviceName);
            } catch (ClassNotFoundException ignore) {
                throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + serviceName);
            }

            Method[] methods = thisClass.getDeclaredMethods();
            return ReflectMethod.create(serviceName, methods);
        }

        private static List<HertsMethod> generateHertsMethod(HertsType coreType, Method[] methods, String serviceName) {
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

        private BindableService registerBidirectionalStreamingService(BidirectionalStreamingCoreServiceCore core) {
            ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
            return new BindableService() {
                private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

                @Override
                public ServerServiceDefinition bindService() {
                    List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.BidirectionalStreaming, reflectMethod.getMethods(), reflectMethod.getClassName());
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

        private BindableService registerClientStreamingService(ClientStreamingCoreServiceCore core) {
            ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
            return new BindableService() {
                private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

                @Override
                public ServerServiceDefinition bindService() {
                    List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.ClientStreaming, reflectMethod.getMethods(), reflectMethod.getClassName());
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

        private BindableService registerServerStreamingService(ServerStreamingCoreServiceCore core) {
            ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
            return new BindableService() {
                private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

                @Override
                public ServerServiceDefinition bindService() {
                    List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getClassName());
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

        private BindableService registerUnaryService(UnaryCoreServiceCore core) {
            ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getName());
            return new BindableService() {
                private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

                @Override
                public ServerServiceDefinition bindService() {
                    List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.Unary, reflectMethod.getMethods(), reflectMethod.getClassName());
                    HertsUnaryDescriptor descriptor = HertsGrpcDescriptor.generateGrpcDescriptor(reflectMethod.getClassName(), hertsMethods);
                    ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                    int index = 0;
                    for (MethodDescriptor<byte[], byte[]> methodDescriptor : descriptor.getMethodDescriptors()) {
                        HertsCoreUMethodHandler<byte[], byte[]> handler = new HertsCoreUMethodHandler<>(hertsMethods.get(index), hertsMetrics);
                        builder = builder.addMethod(methodDescriptor, ServerCalls.asyncUnaryCall(handler));
                        index++;
                    }
                    return builder.build();
                }
            };
        }
    }

    @Override
    public void start() {
        try {
            if (this.option.getPort() == 8888) {
                throw new HertsCoreBuildException("Port 8888 is reserved port number for metrics");
            }

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

            // TODO: Graceful shutdown
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                try {
                    this.hertsMetricsServer.start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            this.server = serverBuilder.build();
            this.server.start();
            logger.info("Started Herts server. gRPC type " + this.hertsTypes.get(0) + " Port " + this.option.getPort());
            server.awaitTermination();
        } catch (Exception ex) {
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
    public HertsType getHertCoreType() {
        if (this.hertsTypes.size() == 0) {
            return null;
        }
        return this.hertsTypes.get(0);
    }
}
