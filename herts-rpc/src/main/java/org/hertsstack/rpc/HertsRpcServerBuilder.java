package org.hertsstack.rpc;

import org.hertsstack.broker.ReactiveBroker;
import org.hertsstack.brokerlocal.ConcurrentLocalBroker;
import org.hertsstack.core.modelx.RegisteredMethod;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.core.context.SharedServiceContext;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.descriptor.CustomGrpcDescriptor;
import org.hertsstack.core.descriptor.CustomGrpcStreamingDescriptor;
import org.hertsstack.core.descriptor.CustomGrpcUnaryDescriptor;
import org.hertsstack.core.exception.NotSupportParameterTypeException;
import org.hertsstack.core.exception.RpcServerBuildException;
import org.hertsstack.core.exception.ServiceNotFoundException;
import org.hertsstack.core.service.HertsServiceBidirectionalStreaming;
import org.hertsstack.core.service.HertsServiceClientStreaming;
import org.hertsstack.core.service.HertsReactiveService;
import org.hertsstack.core.service.HertsServiceReactiveStreaming;
import org.hertsstack.core.service.HertsService;
import org.hertsstack.core.service.HertsServiceServerStreaming;
import org.hertsstack.metrics.HertsMetrics;
import org.hertsstack.metrics.HertsMetricsHandler;
import org.hertsstack.metrics.HertsMetricsServer;

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

/**
 * Herts server builder
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcServerBuilder implements RpcServer {
    private final Map<BindableService, ServerInterceptor> services = new HashMap<>();
    private final List<HertsType> hertsTypes = new ArrayList<>();
    private final List<HertsService> hertsRpcServices = new ArrayList<>();

    private ReactiveBroker reactiveBroker = ConcurrentLocalBroker.getInstance();
    private String connectionInfo;
    private GrpcServerOption option;
    private ServerCredentials credentials;
    private HertsMetricsServer hertsMetricsServer;
    private HertsMetrics hertsMetrics;
    private HertsRpcServerShutdownHook hook;

    public HertsRpcServerBuilder() {
        this.option = new GrpcServerOption();
    }

    public HertsRpcServerBuilder(GrpcServerOption option) {
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
    public RpcServer registerHertsReactiveRpcService(HertsReactiveService hertsReactiveService, @Nullable ServerInterceptor interceptor) {
        if (hertsReactiveService.getClass().getInterfaces().length == 0) {
            throw new RpcServerBuildException("You need to define interface on " + hertsReactiveService.getClass().getName());
        }
        this.hertsRpcServices.add(hertsReactiveService);

        BindableService bindableReceiver = createBindableReceiver(hertsReactiveService);
        BindableService bindableService = createBindableService(hertsReactiveService);
        HertsEmptyRpcInterceptor defaultInterceptor = HertsEmptyRpcInterceptor.create();
        this.services.put(bindableReceiver, HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        if (interceptor == null) {
            this.services.put(bindableService, HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        } else {
            this.services.put(bindableService, interceptor);
        }
        return this;
    }

    @Override
    public RpcServer registerHertsReactiveRpcService(HertsReactiveService hertsReactiveService) {
        if (hertsReactiveService.getClass().getInterfaces().length == 0) {
            throw new RpcServerBuildException("You need to define interface on " + hertsReactiveService.getClass().getName());
        }
        this.hertsRpcServices.add(hertsReactiveService);

        HertsEmptyRpcInterceptor defaultInterceptor = HertsEmptyRpcInterceptor.create();
        this.services.put(createBindableReceiver(hertsReactiveService), HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        this.services.put(createBindableService(hertsReactiveService), HertsRpcInterceptBuilder.builder(defaultInterceptor).build());
        return this;
    }

    @Override
    public RpcServer registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor) {
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
    public RpcServer registerHertsRpcService(HertsService hertsRpcService) {
        this.hertsRpcServices.add(hertsRpcService);
        BindableService bindableService = createBindableService(hertsRpcService);
        this.services.put(bindableService, HertsRpcInterceptBuilder.builder(HertsEmptyRpcInterceptor.create()).build());
        return this;
    }

    @Override
    public RpcServer addShutdownHook(HertsRpcServerShutdownHook hook) {
        this.hook = hook;
        return this;
    }

    @Override
    public RpcServer loadBalancingBroker(ReactiveBroker broker) {
        this.reactiveBroker = broker;
        return this;
    }

    @Override
    public RpcServer secure(ServerCredentials credentials) {
        this.credentials = credentials;
        return this;
    }

    @Override
    public RpcServer enableMetrics(HertsMetricsSetting metricsSetting) {
        if (this.hertsRpcServices.size() == 0) {
            throw new RpcServerBuildException("Please call addService before call enableMetrics");
        }
        this.hertsMetrics = getHertsMetrics(this.hertsRpcServices, metricsSetting);
        this.hertsMetrics.register();
        this.hertsMetricsServer = new HertsMetricsServer(this.hertsMetrics);
        return this;
    }

    @Override
    public RpcServer addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor) {
        if (grpcService == null) {
            throw new RpcServerBuildException("HertsService arg is null");
        }
        this.hertsTypes.add(hertsType);
        this.services.put(grpcService, interceptor);
        return this;
    }

    @Override
    public HertsRpcServerEngine build() {
        if (this.hertsTypes.size() == 0 || this.services.size() == 0) {
            throw new RpcServerBuildException("Please register HertsCoreService");
        }
        if (!RpcValidator.isSameHertsCoreType(this.hertsTypes)) {
            throw new RpcServerBuildException(
                    "Please register same HertsCoreService. Not supported multiple different services");
        }

        HertsType hertsType = this.hertsTypes.get(0);
        String validateMsg = RpcValidator.validateRegisteredServices(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new RpcServerBuildException(validateMsg);
        }
        if (!RpcValidator.isValidStreamingRpc(this.hertsRpcServices)) {
            throw new NotSupportParameterTypeException(
                    "Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
        }
        if (hertsType == HertsType.ServerStreaming && !RpcValidator.isAllReturnVoid(this.hertsRpcServices)) {
            throw new NotSupportParameterTypeException(
                    "Support `void` return method only on ServerStreaming");
        }
        if ((hertsType == HertsType.ClientStreaming || hertsType == HertsType.BidirectionalStreaming)
                && !RpcValidator.isAllReturnStreamObserver(this.hertsRpcServices)) {
            throw new NotSupportParameterTypeException(
                    "Support `StreamObserver` return method if use ClientStreaming or BidirectionalStreaming");
        }
        if (hertsType == HertsType.Reactive && !RpcValidator.isAllReceiverVoid(this.hertsRpcServices)) {
            throw new NotSupportParameterTypeException(
                    "Receiver supports void method only");
        }

        if (hertsType == HertsType.Reactive) {
            for (HertsService hertsService : this.hertsRpcServices) {
                HertsServiceReactiveStreaming reactiveStreamingService = (HertsServiceReactiveStreaming) hertsService;
                reactiveStreamingService.setBroker(this.reactiveBroker);
            }
        }

        ServerBuildInfo buildInfo = new ServerBuildInfo(this.services, this.hertsTypes, this.option, this.credentials, this.hertsMetricsServer, this.hook);
        return new HertsRpcServerEngineBuilder(buildInfo);
    }

    private BindableService createBindableReceiver(HertsReactiveService hertsReactiveService) {
        Class<?> hertsReactiveStreamingService = hertsReactiveService.getClass().getSuperclass();
        Class<?> hertsReactiveStreamingServiceIf = hertsReactiveStreamingService.getInterfaces()[0];

        ReflectMethod reflectMethod = generateReflectMethod(
                hertsReactiveStreamingServiceIf.getName(), hertsReactiveStreamingService.getName(), true);
        reflectMethod.printMethodName();

        return new BindableService() {
            private ServerServiceDefinition bindableService = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.bindableService != null) {
                    return this.bindableService;
                }

                List<RegisteredMethod> hertsMethods = generateHertsMethod(
                        HertsType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                CustomGrpcStreamingDescriptor descriptor = CustomGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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
            throw new RpcServerBuildException("HertsService arg is null");
        }

        this.hertsTypes.add(hertsRpcService.getHertsType());
        BindableService bindableService;
        switch (hertsRpcService.getHertsType()) {
            case Unary:
                bindableService = registerUnaryService(hertsRpcService);
                break;
            case BidirectionalStreaming:
                bindableService = registerBidirectionalStreamingService((HertsServiceBidirectionalStreaming) hertsRpcService);
                break;
            case ServerStreaming:
                bindableService = registerServerStreamingService((HertsServiceServerStreaming) hertsRpcService);
                break;
            case ClientStreaming:
                bindableService = registerClientStreamingService((HertsServiceClientStreaming) hertsRpcService);
                break;
            case Reactive:
                bindableService = registerUnaryService(hertsRpcService);
                break;
            default:
                throw new RpcServerBuildException("HertsCoreType is invalid");
        }
        return bindableService;
    }

    private static ReflectMethod generateReflectMethod(String serviceName, String serviceImplName, boolean isReceiver) {
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new ServiceNotFoundException("Unknown class name. Allowed class is " + serviceName);
        }

        Method[] targetMethods;
        Method[] definedMethods = thisClass.getDeclaredMethods();
        if (!isReceiver) {
            targetMethods = definedMethods;
        } else {
            targetMethods = new Method[1];
            for (Method method : definedMethods) {
                if (method.getName().equals(SharedServiceContext.Rpc.RECEIVER_METHOD_NAME)) {
                    targetMethods[0] = method;
                    break;
                }
            }
            if (targetMethods[0] == null) {
                throw new ServiceNotFoundException("Unrecognized Receiver class");
            }
        }
        return ReflectMethod.create(serviceName, serviceImplName, targetMethods);
    }

    private static List<RegisteredMethod> generateHertsMethod(HertsType coreType, Method[] methods, String serviceName, String serviceImplName) {
        List<RegisteredMethod> hertsMethods = new ArrayList<>();
        for (Method method : methods) {
            RegisteredMethod hertsMethod = new RegisteredMethod();
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

    private BindableService registerBidirectionalStreamingService(HertsServiceBidirectionalStreaming core) {
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);
        reflectMethod.printMethodName();

        return new BindableService() {
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }

                List<RegisteredMethod> hertsMethods = generateHertsMethod(
                        HertsType.BidirectionalStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                CustomGrpcStreamingDescriptor descriptor = CustomGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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

    private BindableService registerClientStreamingService(HertsServiceClientStreaming core) {
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);
        reflectMethod.printMethodName();

        return new BindableService() {
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }

                List<RegisteredMethod> hertsMethods = generateHertsMethod(
                        HertsType.ClientStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                CustomGrpcStreamingDescriptor descriptor = CustomGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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

    private BindableService registerServerStreamingService(HertsServiceServerStreaming core) {
        ReflectMethod reflectMethod = generateReflectMethod(
                core.getClass().getInterfaces()[0].getName(), core.getClass().getName(), false);

        reflectMethod.printMethodName();

        return new BindableService() {
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }
                List<RegisteredMethod> hertsMethods = generateHertsMethod(
                        HertsType.ServerStreaming, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                CustomGrpcStreamingDescriptor descriptor = CustomGrpcDescriptor.generateStreamingGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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
            private ServerServiceDefinition serverServiceDefinition = null;

            @Override
            public ServerServiceDefinition bindService() {
                if (this.serverServiceDefinition != null) {
                    return this.serverServiceDefinition;
                }

                List<RegisteredMethod> hertsMethods = generateHertsMethod(
                        HertsType.Unary, reflectMethod.getMethods(), reflectMethod.getServiceName(), reflectMethod.getServiceImplName());

                CustomGrpcUnaryDescriptor descriptor = CustomGrpcDescriptor.generateGrpcDescriptor(reflectMethod.getServiceName(), hertsMethods);
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
