package com.tomoyane.herts.hertsrpcclient;

import com.tomoyane.herts.hertsrpcclient.handler.HertsRpcClientCStreamingMethodHandler;
import com.tomoyane.herts.hertsrpcclient.handler.HertsRpcClientUMethodHandler;
import com.tomoyane.herts.hertsrpcclient.handler.HertsRpcClientBStreamingMethodHandler;
import com.tomoyane.herts.hertsrpcclient.handler.HertsRpcClientSStreamingMethodHandler;
import com.tomoyane.herts.hertsrpcclient.validator.HertsRpcClientValidator;
import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.exception.HertsChannelIsNullException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreClientBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreTypeInvalidException;
import com.tomoyane.herts.hertscommon.exception.HertsNotSupportParameterTypeException;
import com.tomoyane.herts.hertscommon.service.HertsRpcService;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HertsRpcClientBuilder implements HertsRpcClient {
    private final String connectedHost;
    private final int serverPort;
    private final HertsType hertsType;
    private final boolean isSecureConnection;
    private final ClientInterceptor interceptor;
    private final GrpcClientOption option;
    private final List<HertsRpcService> hertsRpcServices;

    private Channel channel;

    private HertsRpcClientBuilder(IBuilder builder) {
        this.connectedHost = builder.connectedHost;
        this.isSecureConnection = builder.isSecureConnection;
        this.serverPort = builder.serverPort;
        this.hertsType = builder.hertsType;
        this.interceptor = builder.interceptor;
        this.channel = builder.channel;
        this.option = builder.option;
        this.hertsRpcServices = builder.hertsRpcServices;
    }

    public static IBuilder builder(String connectedHost, int serverPort, HertsType hertsType) {
        return new IBuilder(connectedHost, serverPort, hertsType);
    }

    public static class IBuilder implements HertsRpcClientIBuilder {
        private final List<HertsRpcService> hertsRpcServices = new ArrayList<>();
        private final String connectedHost;
        private final int serverPort;
        private final HertsType hertsType;

        private boolean isSecureConnection;
        private Channel channel;
        private ClientInterceptor interceptor;
        private GrpcClientOption option;

        private IBuilder(String connectedHost, int serverPort, HertsType hertsType) {
            this.connectedHost = connectedHost;
            this.serverPort = serverPort;
            this.hertsType = hertsType;
        }

        @Override
        public HertsRpcClientIBuilder secure(boolean isSecureConnection) {
            this.isSecureConnection = isSecureConnection;
            return this;
        }

        @Override
        public HertsRpcClientIBuilder hertsImplementationService(HertsRpcService hertsRpcService) {
            this.hertsRpcServices.add(hertsRpcService);
            return this;
        }

        @Override
        public HertsRpcClientIBuilder channel(Channel channel) {
            this.channel = channel;
            return this;
        }

        @Override
        public HertsRpcClientIBuilder interceptor(ClientInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        @Override
        public HertsRpcClientIBuilder grpcOption(GrpcClientOption option) {
            this.option = option;
            return this;
        }

        @Override
        public HertsRpcClient build() {
            if (this.hertsRpcServices.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
                throw new HertsCoreClientBuildException("Please register HertsService and host");
            }
            List<HertsType> hertsTypes = this.hertsRpcServices.stream().map(HertsRpcService::getHertsType).toList();
            if (!HertsRpcClientValidator.isSameHertsCoreType(hertsTypes)) {
                throw new HertsCoreClientBuildException("Please register same HertsCoreService. Not supported multiple different services");
            }

            var validateMsg = HertsRpcClientValidator.validateRegisteredServices(this.hertsRpcServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreClientBuildException(validateMsg);
            }

            if (!HertsRpcClientValidator.isValidStreamingRpc(this.hertsRpcServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
            if (this.option == null) {
                this.option = new GrpcClientOption();
            }
            return new HertsRpcClientBuilder(this);
        }
    }

    @Override
    public String getConnectedHost() {
        return connectedHost;
    }

    @Override
    public boolean isSecureConnection() {
        return isSecureConnection;
    }

    @Override
    public HertsType getHertsCoreType() {
        return hertsType;
    }

    @Override
    public ManagedChannel getChannel() {
        if (this.channel == null) {
            throw new HertsChannelIsNullException("Please create HertService instance.");
        }
        return (ManagedChannel) channel;
    }

    @Override
    public <T extends HertsRpcService> T createHertCoreInterface(Class<T> interfaceType) {
        // If not null, using custom channel
        if (this.channel == null) {
            ManagedChannelBuilder<?> managedChannelBuilder = ManagedChannelBuilder.forAddress(this.connectedHost, this.serverPort);

            if (!this.isSecureConnection) {
                managedChannelBuilder = managedChannelBuilder.usePlaintext();
            }
            if (this.interceptor != null) {
                managedChannelBuilder = managedChannelBuilder.intercept(interceptor);
            } else {
                var defaultInterceptor = new HertsRpcClientInterceptor() {
                    @Override
                    public void setRequestMetadata(Metadata metadata) {
                    }
                    @Override
                    public <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
                    }
                };
                managedChannelBuilder = managedChannelBuilder.intercept(HertsRpcClientInterceptBuilder.builder(defaultInterceptor).build());
            }
            if (this.option.getKeepaliveMilliSec() != null) {
                managedChannelBuilder = managedChannelBuilder.keepAliveTime(this.option.getKeepaliveMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getKeepaliveTimeoutMilliSec() != null) {
                managedChannelBuilder = managedChannelBuilder.keepAliveTimeout(this.option.getKeepaliveTimeoutMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getIdleTimeoutMilliSec() != null) {
                managedChannelBuilder = managedChannelBuilder.idleTimeout(this.option.getIdleTimeoutMilliSec(), TimeUnit.MILLISECONDS);
            }
            this.channel = managedChannelBuilder.build();
        }

        var serviceName = interfaceType.getName();
        HertsRpcService targetHertsRpcService = null;
        for (HertsRpcService registeredService : this.hertsRpcServices) {
            for (Class<?> registeredInterfaceName : registeredService.getClass().getInterfaces()) {
                if (!registeredInterfaceName.getName().equals(serviceName)) {
                    continue;
                }
                targetHertsRpcService = registeredService;
                break;
            }
        }

        if (targetHertsRpcService == null) {
            throw new HertsCoreClientBuildException("Not found " + serviceName + " in registration services");
        }

        switch (this.hertsType) {
            case Unary -> {
                var unary = newHertsBlockingService(channel, targetHertsRpcService);
                return (T) generateService(unary, interfaceType);
            }
            case BidirectionalStreaming -> {
                var streaming = newHertsBidirectionalStreamingService(channel, targetHertsRpcService);
                return (T) generateService(streaming, interfaceType);
            }
            case ServerStreaming -> {
                var serverStreaming = newHertsServerStreamingService(channel, targetHertsRpcService);
                return (T) generateService(serverStreaming, interfaceType);
            }
            case ClientStreaming -> {
                var clientStreaming = newHertsClientStreamingService(channel, targetHertsRpcService);
                return (T) generateService(clientStreaming, interfaceType);
            }
            default ->
                    throw new HertsCoreTypeInvalidException("Undefined Hert core type. HertsCoreType" + this.hertsType);
        }
    }

    private HertsRpcService generateService(InvocationHandler handler, Class<?> classType) {
        return (HertsRpcService) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
                handler);
    }

    private static HertsRpcClientUMethodHandler newHertsBlockingService(Channel channel, HertsRpcService hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientUMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientUMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientBStreamingMethodHandler newHertsBidirectionalStreamingService(Channel channel, HertsRpcService hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientBStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientBStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientSStreamingMethodHandler newHertsServerStreamingService(Channel channel, HertsRpcService hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientSStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientSStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientCStreamingMethodHandler newHertsClientStreamingService(Channel channel, HertsRpcService hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientCStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientCStreamingMethodHandler.newStub(factory, channel);
    }
}
