package org.herts.rpc.engine;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsCoreBuildException;
import org.herts.common.exception.HertsNotSupportParameterTypeException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.logger.HertsLogger;
import org.herts.common.descriptor.HertsUnaryDescriptor;
import org.herts.common.context.HertsMethod;
import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.descriptor.HertsStreamingDescriptor;
import org.herts.common.service.BidirectionalStreamingRpcServiceRpc;
import org.herts.common.service.ClientStreamingRpcServiceRpc;
import org.herts.common.service.ServerStreamingRpcServiceRpc;
import org.herts.common.service.UnaryRpcServiceRpc;
import org.herts.common.service.HertsRpcService;
import org.herts.rpc.HertsRpcInterceptor;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.handler.HertsRpcCStreamingMethodHandler;
import org.herts.rpc.handler.HertsRpcSStreamingMethodHandler;
import org.herts.rpc.handler.HertsRpcBMethodHandler;
import org.herts.rpc.handler.HertsRpcUMethodHandler;
import org.herts.rpc.model.ReflectMethod;
import org.herts.rpc.validator.HertsRpcValidator;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.handler.HertsMetricsHandler;
import org.herts.metrics.server.HertsMetricsServer;

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

public class HertsRpcBuilder implements HertsRpcEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsRpcBuilder.class.getSimpleName());

    private final Map<BindableService, ServerInterceptor> services;
    private final List<HertsType> hertsTypes;
    private final List<HertsRpcService> hertsRpcServices;
    private final GrpcServerOption option;
    private final ServerCredentials credentials;
    private final HertsMetricsServer hertsMetricsServer;
    private final HertsMetrics hertsMetrics;

    private Server server;

    private HertsRpcBuilder(Builder builder) {
        this.option = builder.getOption();
        this.credentials = builder.getCredentials();
        this.hertsTypes = builder.getHertsCoreTypes();
        this.services = builder.getServices();
        this.hertsRpcServices = builder.getHertsServices();
        this.hertsMetricsServer = builder.hertsMetricsServer;
        this.hertsMetrics = builder.hertsMetrics;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(GrpcServerOption option) {
        return new Builder(option);
    }

    public static class Builder implements HertsRpcEngineBuilder {
        private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
        private final List<HertsType> hertsTypes = new ArrayList<>();
        private final List<HertsRpcService> hertsRpcServices = new ArrayList<>();
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

        private static HertsMetrics getHertsMetrics(List<HertsRpcService> coreServices, HertsMetricsSetting metricsSetting) {
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
        public List<HertsRpcService> getHertsServices() {
            return hertsRpcServices;
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
        public HertsRpcEngineBuilder addService(HertsRpcService hertsRpcService, @Nullable ServerInterceptor interceptor) {
            if (hertsRpcService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }

            this.hertsRpcServices.add(hertsRpcService);
            this.hertsTypes.add(hertsRpcService.getHertsType());

            BindableService bindableService;
            switch (hertsRpcService.getHertsType()) {
                case Unary:
                    bindableService = registerUnaryService((UnaryRpcServiceRpc) hertsRpcService);
                    break;
                case BidirectionalStreaming:
                    bindableService = registerBidirectionalStreamingService((BidirectionalStreamingRpcServiceRpc) hertsRpcService);
                    break;
                case ServerStreaming:
                    bindableService = registerServerStreamingService((ServerStreamingRpcServiceRpc) hertsRpcService);
                    break;
                case ClientStreaming:
                    bindableService = registerClientStreamingService((ClientStreamingRpcServiceRpc) hertsRpcService);
                    break;
                default:
                    throw new HertsCoreBuildException("HertsCoreType is invalid");
            }

            if (interceptor == null) {
                var defaultInterceptor = new HertsRpcInterceptor() {
                    @Override
                    public void setResponseMetadata(Metadata metadata) {
                    }
                    @Override
                    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {
                    }
                };
                this.services.put(bindableService, HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
            } else {
                this.services.put(bindableService, interceptor);
            }
            return this;
        }

        @Override
        public HertsRpcEngineBuilder secure(ServerCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        @Override
        public HertsRpcEngineBuilder enableMetrics(HertsMetricsSetting metricsSetting) {
            if (this.hertsRpcServices.size() == 0) {
                throw new HertsCoreBuildException("Please call addService before call enableMetrics");
            }
            this.hertsMetrics = getHertsMetrics(this.hertsRpcServices, metricsSetting);
            this.hertsMetrics.register();
            this.hertsMetricsServer = new HertsMetricsServer(this.hertsMetrics);
            return this;
        }

        @Override
        public HertsRpcEngineBuilder addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor) {
            if (grpcService == null) {
                throw new HertsCoreBuildException("HertsService arg is null");
            }
            this.hertsTypes.add(hertsType);
            this.services.put(grpcService, interceptor);
            return this;
        }

        @Override
        public HertsRpcEngine build() {
            if (this.hertsTypes.size() == 0 || this.services.size() == 0) {
                throw new HertsCoreBuildException("Please register HertsCoreService");
            }
            if (!HertsRpcValidator.isSameHertsCoreType(this.hertsTypes)) {
                throw new HertsCoreBuildException("Please register same HertsCoreService. Not supported multiple different services");
            }
            var validateMsg = HertsRpcValidator.validateRegisteredServices(this.hertsRpcServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreBuildException(validateMsg);
            }
            if (!HertsRpcValidator.isValidStreamingRpc(this.hertsRpcServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
            return new HertsRpcBuilder(this);
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

        private BindableService registerBidirectionalStreamingService(BidirectionalStreamingRpcServiceRpc core) {
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
                        HertsRpcBMethodHandler<Object, Object> handler = new HertsRpcBMethodHandler<>(hertsMethods.get(index));
                        builder = builder.addMethod(methodDescriptor, ServerCalls.asyncBidiStreamingCall(handler));
                        index++;
                    }
                    return builder.build();
                }
            };
        }

        private BindableService registerClientStreamingService(ClientStreamingRpcServiceRpc core) {
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
                        HertsRpcCStreamingMethodHandler<Object, Object> handler = new HertsRpcCStreamingMethodHandler<>(hertsMethods.get(index));
                        builder = builder.addMethod(methodDescriptor, ServerCalls.asyncClientStreamingCall(handler));
                        index++;
                    }
                    return builder.build();
                }
            };
        }

        private BindableService registerServerStreamingService(ServerStreamingRpcServiceRpc core) {
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
                        HertsRpcSStreamingMethodHandler<Object, Object> handler = new HertsRpcSStreamingMethodHandler<>(hertsMethods.get(index));
                        builder = builder.addMethod(methodDescriptor, ServerCalls.asyncServerStreamingCall(handler));
                        index++;
                    }
                    return builder.build();
                }
            };
        }

        private BindableService registerUnaryService(UnaryRpcServiceRpc core) {
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
                        HertsRpcUMethodHandler<byte[], byte[]> handler = new HertsRpcUMethodHandler<>(hertsMethods.get(index), hertsMetrics);
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
