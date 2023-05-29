package org.herts.rpc.engine;

import org.herts.common.modelx.HertsMethod;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.context.HertsSystemContext;
import org.herts.common.context.HertsType;
import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.descriptor.HertsStreamingDescriptor;
import org.herts.common.descriptor.HertsUnaryDescriptor;
import org.herts.common.exception.HertsNotSupportParameterTypeException;
import org.herts.common.exception.HertsRpcBuildException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.loadbalancing.BrokerTypeBuilder;
import org.herts.common.loadbalancing.LoadBalancingType;
import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsBidirectionalStreamingService;
import org.herts.common.service.HertsClientStreamingService;
import org.herts.common.reactive.HertsReactiveService;
import org.herts.common.reactive.HertsReactiveStreamingService;
import org.herts.common.service.HertsService;
import org.herts.common.service.HertsServerStreamingService;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.handler.HertsMetricsHandler;
import org.herts.metrics.server.HertsMetricsServer;
import org.herts.rpc.HertsEmptyRpcInterceptor;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerShutdownHook;
import org.herts.rpc.handler.HertsRpcBMethodHandler;
import org.herts.rpc.handler.HertsRpcCStreamingMethodHandler;
import org.herts.rpc.handler.HertsRpcSStreamingMethodHandler;
import org.herts.rpc.handler.HertsRpcUMethodHandler;
import org.herts.rpc.modelx.ReflectMethod;
import org.herts.rpc.modelx.ServerBuildInfo;
import org.herts.rpc.validator.HertsRpcValidator;

import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
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

/**
 * Herts server builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ServerBuilder implements HertsRpcEngineBuilder {
    private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
    private final List<HertsType> hertsTypes = new ArrayList<>();
    private final List<HertsService> hertsRpcServices = new ArrayList<>();
    private LoadBalancingType loadBalancingType = LoadBalancingType.LocalGroupRepository;
    private String connectionInfo;
    private GrpcServerOption option;
    private ServerCredentials credentials;
    private HertsMetricsServer hertsMetricsServer;
    private HertsMetrics hertsMetrics;
    private HertsRpcServerShutdownHook hook;

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
    public HertsRpcEngineBuilder registerHertsRpcService(HertsReactiveService hertsReactiveService, @Nullable ServerInterceptor interceptor) {
        if (hertsReactiveService.getClass().getInterfaces().length == 0) {
            throw new HertsRpcBuildException("You need to define interface on " + hertsReactiveService.getClass().getName());
        }
        this.hertsRpcServices.add(hertsReactiveService);

        BindableService bindableReceiver = createBindableReceiver(hertsReactiveService);
        BindableService bindableService = createBindableService(hertsReactiveService);
        var defaultInterceptor = HertsEmptyRpcInterceptor.create();
        this.services.put(bindableReceiver, HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        if (interceptor == null) {
            this.services.put(bindableService, HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        } else {
            this.services.put(bindableService, interceptor);
        }
        return this;
    }

    @Override
    public HertsRpcEngineBuilder registerHertsRpcService(HertsReactiveService hertsReactiveService) {
        if (hertsReactiveService.getClass().getInterfaces().length == 0) {
            throw new HertsRpcBuildException("You need to define interface on " + hertsReactiveService.getClass().getName());
        }
        this.hertsRpcServices.add(hertsReactiveService);

        var defaultInterceptor = HertsEmptyRpcInterceptor.create();
        this.services.put(createBindableReceiver(hertsReactiveService), HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        this.services.put(createBindableService(hertsReactiveService), HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        return this;
    }

    @Override
    public HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor) {
        this.hertsRpcServices.add(hertsRpcService);
        BindableService bindableService = createBindableService(hertsRpcService);
        if (interceptor == null) {
            this.services.put(bindableService, HertsRpcInterceptBuilder.builder(HertsEmptyRpcInterceptor.create()).build());
        } else {
            this.services.put(bindableService, interceptor);
        }
        return this;
    }

    @Override
    public HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService) {
        this.hertsRpcServices.add(hertsRpcService);
        BindableService bindableService = createBindableService(hertsRpcService);
        this.services.put(bindableService, HertsRpcInterceptBuilder.builder(HertsEmptyRpcInterceptor.create()).build());
        return this;
    }

    @Override
    public HertsRpcEngineBuilder addShutdownHook(HertsRpcServerShutdownHook hook) {
        this.hook = hook;
        return this;
    }

    @Override
    public HertsRpcEngineBuilder loadBalancingType(LoadBalancingType loadBalancingType, @Nullable String connectionInfo) {
        this.loadBalancingType = loadBalancingType;
        this.connectionInfo = connectionInfo;
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
            throw new HertsRpcBuildException(
                    "Please register same HertsCoreService. Not supported multiple different services");
        }

        var hertsType = this.hertsTypes.get(0);
        var validateMsg = HertsRpcValidator.validateRegisteredServices(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HertsRpcBuildException(validateMsg);
        }
        if (!HertsRpcValidator.isValidStreamingRpc(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException(
                    "Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
        }
        if (hertsType == HertsType.ServerStreaming && !HertsRpcValidator.isAllReturnVoid(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException(
                    "Support `void` return method only on ServerStreaming");
        }
        if ((hertsType == HertsType.ClientStreaming || hertsType == HertsType.BidirectionalStreaming)
                && !HertsRpcValidator.isAllReturnStreamObserver(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException(
                    "Support `StreamObserver` return method if use ClientStreaming or BidirectionalStreaming");
        }
        if (hertsType == HertsType.Reactive && !HertsRpcValidator.isAllReceiverVoid(this.hertsRpcServices)) {
            throw new HertsNotSupportParameterTypeException(
                    "Receiver supports void method only");
        }

        var broker = BrokerTypeBuilder.builder()
                .loadBalancingType(this.loadBalancingType)
                .connectionInfo(this.connectionInfo)
                .build();

        if (hertsType == HertsType.Reactive) {
            for (HertsService hertsService : this.hertsRpcServices) {
                HertsReactiveStreamingService reactiveStreamingService = (HertsReactiveStreamingService) hertsService;
                reactiveStreamingService.getBroadCaster().setBroker(broker);
            }
        }

        var buildInfo = new ServerBuildInfo(this.services, this.hertsTypes, this.option, this.credentials, this.hertsMetricsServer, this.hook);
        return new HertsRpcBuilder(buildInfo);
    }

    private BindableService createBindableReceiver(HertsReactiveService hertsReactiveService) {
        Class<?> hertsReactiveStreamingService = hertsReactiveService.getClass().getSuperclass();
        Class<?> hertsReactiveStreamingServiceIf = hertsReactiveStreamingService.getInterfaces()[0];

        ReflectMethod reflectMethod = generateReflectMethod(
                hertsReactiveStreamingServiceIf.getName(), hertsReactiveStreamingService.getName(), true);
        reflectMethod.printMethodName();

        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());
            private ServerServiceDefinition bindableService = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.bindableService != null) {
                    return this.bindableService;
                }

                List<HertsMethod> hertsMethods = generateHertsMethod(
                        HertsType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsRpcSStreamingMethodHandler<Object, Object> handler = new HertsRpcSStreamingMethodHandler<>(hertsMethods.get(index), null, hertsReactiveService);
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncServerStreamingCall(handler));
                    index++;
                }

                this.bindableService = builder.build();
                return builder.build();
            }
        };
    }

    private BindableService createBindableService(HertsService hertsRpcService) {
        if (hertsRpcService == null) {
            throw new HertsRpcBuildException("HertsService arg is null");
        }

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
            case Reactive:
                bindableService = registerUnaryService(hertsRpcService);
                break;
            default:
                throw new HertsRpcBuildException("HertsCoreType is invalid");
        }
        return bindableService;
    }

    private static ReflectMethod generateReflectMethod(String serviceName, String serviceImplName, boolean isReceiver) {
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + serviceName);
        }

        Method[] targetMethods;
        Method[] definedMethods = thisClass.getDeclaredMethods();
        if (!isReceiver) {
            targetMethods = definedMethods;
        } else {
            targetMethods = new Method[1];
            for (Method method : definedMethods) {
                if (method.getName().equals(HertsSystemContext.Rpc.RECEIVER_METHOD_NAME)) {
                    targetMethods[0] = method;
                    break;
                }
            }
            if (targetMethods[0] == null) {
                throw new HertsServiceNotFoundException("Unrecognized Receiver class");
            }
        }
        return ReflectMethod.create(serviceName, serviceImplName, targetMethods);
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
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);
        reflectMethod.printMethodName();

        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }

                List<HertsMethod> hertsMethods = generateHertsMethod(
                        HertsType.BidirectionalStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsRpcBMethodHandler<Object, Object> handler = new HertsRpcBMethodHandler<>(hertsMethods.get(index), hertsMetrics, core);
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncBidiStreamingCall(handler));
                    index++;
                }

                this.serverServiceDefinition = builder.build();
                return this.serverServiceDefinition;
            }
        };
    }

    private BindableService registerClientStreamingService(HertsClientStreamingService core) {
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);
        reflectMethod.printMethodName();

        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }

                List<HertsMethod> hertsMethods = generateHertsMethod(
                        HertsType.ClientStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsRpcCStreamingMethodHandler<Object, Object> handler = new HertsRpcCStreamingMethodHandler<>(hertsMethods.get(index), hertsMetrics, core);
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncClientStreamingCall(handler));
                    index++;
                }

                this.serverServiceDefinition = builder.build();
                return this.serverServiceDefinition;
            }
        };
    }

    private BindableService registerServerStreamingService(HertsServerStreamingService core) {
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);

        reflectMethod.printMethodName();

        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }
                List<HertsMethod> hertsMethods = generateHertsMethod(
                        HertsType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                HertsStreamingDescriptor descriptor = HertsGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<Object, Object> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsRpcSStreamingMethodHandler<Object, Object> handler = new HertsRpcSStreamingMethodHandler<>(hertsMethods.get(index), hertsMetrics, core);
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncServerStreamingCall(handler));
                    index++;
                }

                this.serverServiceDefinition = builder.build();
                return this.serverServiceDefinition;
            }
        };
    }

    private BindableService registerUnaryService(HertsService core) {
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);
        reflectMethod.printMethodName();

        return new BindableService() {
            private static final Logger logger = HertsLogger.getLogger(BindableService.class.getSimpleName());
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }

                List<HertsMethod> hertsMethods = generateHertsMethod(
                        HertsType.Unary, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                HertsUnaryDescriptor descriptor = HertsGrpcDescriptor.generateGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
                ServerServiceDefinition.Builder builder = io.grpc.ServerServiceDefinition.builder(descriptor.getServiceDescriptor());

                int index = 0;
                for (MethodDescriptor<byte[], byte[]> methodDescriptor : descriptor.getMethodDescriptors()) {
                    HertsRpcUMethodHandler<byte[], byte[]> handler = new HertsRpcUMethodHandler<>(hertsMethods.get(index), hertsMetrics, core);
                    builder = builder.addMethod(methodDescriptor, ServerCalls.asyncUnaryCall(handler));
                    index++;
                }

                this.serverServiceDefinition = builder.build();
                return this.serverServiceDefinition;
            }
        };
    }
}
