package org.herts.rpc.engine;

import org.herts.common.context.HertsMethod;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.context.HertsType;
import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.descriptor.HertsStreamingDescriptor;
import org.herts.common.descriptor.HertsUnaryDescriptor;
import org.herts.common.exception.HertsNotSupportParameterTypeException;
import org.herts.common.exception.HertsRpcBuildException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsBidirectionalStreamingService;
import org.herts.common.service.HertsClientStreamingService;
import org.herts.common.service.HertsService;
import org.herts.common.service.HertsServerStreamingService;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.handler.HertsMetricsHandler;
import org.herts.metrics.server.HertsMetricsServer;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcInterceptor;
import org.herts.rpc.handler.HertsRpcBMethodHandler;
import org.herts.rpc.handler.HertsRpcCStreamingMethodHandler;
import org.herts.rpc.handler.HertsRpcSStreamingMethodHandler;
import org.herts.rpc.handler.HertsRpcUMethodHandler;
import org.herts.rpc.modelx.ReflectMethod;
import org.herts.rpc.validator.HertsRpcValidator;

import io.grpc.BindableService;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ServerBuilder implements HertsRpcEngineBuilder {
    private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
    private final List<HertsType> hertsTypes = new ArrayList<>();
    private final List<HertsService> hertsRpcServices = new ArrayList<>();
    private GrpcServerOption option;
    private ServerCredentials credentials;
    private HertsMetricsServer hertsMetricsServer;
    private HertsMetrics hertsMetrics;

    public ServerBuilder() {
        this.option = new GrpcServerOption();
    }

    public ServerBuilder(GrpcServerOption option) {
        this.option = option;
    }

    private static HertsMetrics getHertsMetrics(List<HertsService> coreServices, HertsMetricsSetting metricsSetting) {
        HertsMetrics hertsMetrics;
        if (metricsSetting != null) {
            hertsMetrics = HertsMetricsHandler.builder()
                    .registerHertsServices(coreServices)
                    .isErrRateEnabled(metricsSetting.isErrRateEnabled())
                    .isJvmEnabled(metricsSetting.isJvmEnabled())
                    .isLatencyEnabled(metricsSetting.isLatencyEnabled())
                    .isServerResourceEnabled(metricsSetting.isServerResourceEnabled())
                    .isRpsEnabled(metricsSetting.isRpsEnabled())
                    .build();
        } else {
            hertsMetrics = HertsMetricsHandler.builder().registerHertsServices(null).build();
        }
        return hertsMetrics;
    }

    @Override
    public List<HertsService> getHertsServices() {
        return hertsRpcServices;
    }

    @Override
    public HertsMetricsServer getHertsMetricsServer() {
        return hertsMetricsServer;
    }

    @Override
    public HertsMetrics getHertsMetrics() {
        return hertsMetrics;
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
    public HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor) {
        BindableService bindableService = createBindableService(hertsRpcService);
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
    public HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService) {
        BindableService bindableService = createBindableService(hertsRpcService);
        var defaultInterceptor = new HertsRpcInterceptor() {
            @Override
            public void setResponseMetadata(Metadata metadata) {
            }
            @Override
            public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {
            }
        };
        this.services.put(bindableService, HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
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
            throw new HertsRpcBuildException("Please call addService before call enableMetrics");
        }
        this.hertsMetrics = getHertsMetrics(this.hertsRpcServices, metricsSetting);
        this.hertsMetrics.register();
        this.hertsMetricsServer = new HertsMetricsServer(this.hertsMetrics);
        return this;
    }

    @Override
    public HertsRpcEngineBuilder addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor) {
        if (grpcService == null) {
            throw new HertsRpcBuildException("HertsService arg is null");
        }
        this.hertsTypes.add(hertsType);
        this.services.put(grpcService, interceptor);
        return this;
    }

    @Override
    public HertsRpcEngine build() {
        if (this.hertsTypes.size() == 0 || this.services.size() == 0) {
            throw new HertsRpcBuildException("Please register HertsCoreService");
        }
        if (!HertsRpcValidator.isSameHertsCoreType(this.hertsTypes)) {
            throw new HertsRpcBuildException("Please register same HertsCoreService. Not supported multiple different services");
        }
        var hertsType = this.hertsTypes.get(0);
        var validateMsg = HertsRpcValidator.validateRegisteredServices(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HertsRpcBuildException(validateMsg);
        }
        if (!HertsRpcValidator.isValidStreamingRpc(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
        }
        if (hertsType == HertsType.ServerStreaming && !HertsRpcValidator.isAllReturnVoid(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException("Support `void` return method only on ServerStreaming");
        }
        if (hertsType == HertsType.ClientStreaming && !HertsRpcValidator.isAllReturnStreamObserver(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException("Support `StreamObserver` return method only on ClientStreaming");
        }
        return new HertsRpcBuilder(this);
    }

    private BindableService createBindableService(HertsService hertsRpcService) {
        if (hertsRpcService == null) {
            throw new HertsRpcBuildException("HertsService arg is null");
        }

        this.hertsRpcServices.add(hertsRpcService);
        this.hertsTypes.add(hertsRpcService.getHertsType());

        BindableService bindableService;
        switch (hertsRpcService.getHertsType()) {
            case Unary:
                bindableService = registerUnaryService(hertsRpcService);
                break;
            case BidirectionalStreaming:
                bindableService = registerBidirectionalStreamingService((HertsBidirectionalStreamingService) hertsRpcService);
                break;
            case ServerStreaming:
                bindableService = registerServerStreamingService((HertsServerStreamingService) hertsRpcService);
                break;
            case ClientStreaming:
                bindableService = registerClientStreamingService((HertsClientStreamingService) hertsRpcService);
                break;
            default:
                throw new HertsRpcBuildException("HertsCoreType is invalid");
        }
        return bindableService;
    }

    private static ReflectMethod generateReflectMethod(String serviceName, String serviceImplName) {
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + serviceName);
        }

        Method[] methods = thisClass.getDeclaredMethods();
        return ReflectMethod.create(serviceName, serviceImplName, methods);
    }

    private static List<HertsMethod> generateHertsMethod(HertsType coreType, Method[] methods, String serviceName, String serviceImplName) {
        List<HertsMethod> hertsMethods = new ArrayList<>();
        for (Method method : methods) {
            HertsMethod hertsMethod = new HertsMethod();
            hertsMethod.setHertsCoreType(coreType);
            hertsMethod.setCoreServiceName(serviceName);
            hertsMethod.setCoreImplServiceName(serviceImplName);
            hertsMethod.setMethodName(method.getName());
            hertsMethod.setMethodReturnType(method.getReturnType());
            hertsMethod.setParameters(method.getParameterTypes());
            hertsMethods.add(hertsMethod);
        }
        return hertsMethods;
    }

    private BindableService registerBidirectionalStreamingService(HertsBidirectionalStreamingService core) {
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getInterfaces()[0].getName(), core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.BidirectionalStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());
                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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

    private BindableService registerClientStreamingService(HertsClientStreamingService core) {
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getInterfaces()[0].getName(), core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.ClientStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());
                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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

    private BindableService registerServerStreamingService(HertsServerStreamingService core) {
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getInterfaces()[0].getName(), core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());
                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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

    private BindableService registerUnaryService(HertsService core) {
        ReflectMethod reflectMethod = generateReflectMethod(core.getClass().getInterfaces()[0].getName(), core.getClass().getName());
        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());

            @Override
            public ServerServiceDefinition bindService() {
                List<HertsMethod> hertsMethods = generateHertsMethod(HertsType.Unary, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());
                HertsUnaryDescriptor descriptor = HertsGrpcDescriptor.generateGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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
