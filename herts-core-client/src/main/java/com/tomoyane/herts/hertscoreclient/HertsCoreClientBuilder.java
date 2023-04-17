package com.tomoyane.herts.hertscoreclient;

import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientCStreamingMethodHandler;
import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientUMethodHandler;
import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientBStreamingMethodHandler;
import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientSStreamingMethodHandler;
import com.tomoyane.herts.hertscoreclient.validator.HertsCoreClientValidator;
import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.exception.HertsChannelIsNullException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreClientBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreTypeInvalidException;
import com.tomoyane.herts.hertscommon.exception.HertsNotSupportParameterTypeException;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

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

public class HertsCoreClientBuilder implements HertsCoreClient {
    private final String connectedHost;
    private final int serverPort;
    private final HertsType hertsType;
    private final boolean isSecureConnection;
    private final ClientInterceptor interceptor;
    private final GrpcClientOption option;
    private final List<HertsCoreService> hertsCoreServices;

    private Channel channel;

    private HertsCoreClientBuilder(Builder builder) {
        this.connectedHost = builder.connectedHost;
        this.isSecureConnection = builder.isSecureConnection;
        this.serverPort = builder.serverPort;
        this.hertsType = builder.hertsType;
        this.interceptor = builder.interceptor;
        this.channel = builder.channel;
        this.option = builder.option;
        this.hertsCoreServices = builder.hertsCoreServices;
    }

    public static Builder builder(String connectedHost, int serverPort, HertsType hertsType) {
        return new Builder(connectedHost, serverPort, hertsType);
    }

    public static class Builder implements HertsCoreRpcClientBuilder {
        private final List<HertsCoreService> hertsCoreServices = new ArrayList<>();
        private final String connectedHost;
        private final int serverPort;
        private final HertsType hertsType;

        private boolean isSecureConnection;
        private Channel channel;
        private ClientInterceptor interceptor;
        private GrpcClientOption option;

        private Builder(String connectedHost, int serverPort, HertsType hertsType) {
            this.connectedHost = connectedHost;
            this.serverPort = serverPort;
            this.hertsType = hertsType;
        }

        @Override
        public HertsCoreRpcClientBuilder secure(boolean isSecureConnection) {
            this.isSecureConnection = isSecureConnection;
            return this;
        }

        @Override
        public HertsCoreRpcClientBuilder hertsImplementationService(HertsCoreService hertsCoreService) {
            this.hertsCoreServices.add(hertsCoreService);
            return this;
        }

        @Override
        public HertsCoreRpcClientBuilder channel(Channel channel) {
            this.channel = channel;
            return this;
        }

        @Override
        public HertsCoreRpcClientBuilder interceptor(ClientInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        @Override
        public HertsCoreRpcClientBuilder grpcOption(GrpcClientOption option) {
            this.option = option;
            return this;
        }

        @Override
        public HertsCoreClient build() {
            if (this.hertsCoreServices.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
                throw new HertsCoreClientBuildException("Please register HertsService and host");
            }
            List<HertsType> hertsTypes = this.hertsCoreServices.stream().map(HertsCoreService::getHertsType).toList();
            if (!HertsCoreClientValidator.isSameHertsCoreType(hertsTypes)) {
                throw new HertsCoreClientBuildException("Please register same HertsCoreService. Not supported multiple different services");
            }

            var validateMsg = HertsCoreClientValidator.validateRegisteredServices(this.hertsCoreServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreClientBuildException(validateMsg);
            }

            if (!HertsCoreClientValidator.isValidStreamingRpc(this.hertsCoreServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
            if (this.option == null) {
                this.option = new GrpcClientOption();
            }
            return new HertsCoreClientBuilder(this);
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
    public <T extends HertsCoreService> T createHertCoreInterface(Class<T> interfaceType) {
        // If not null, using custom channel
        if (this.channel == null) {
            ManagedChannelBuilder<?> managedChannelBuilder = ManagedChannelBuilder.forAddress(this.connectedHost, this.serverPort);

            if (!this.isSecureConnection) {
                managedChannelBuilder = managedChannelBuilder.usePlaintext();
            }
            if (this.interceptor != null) {
                managedChannelBuilder = managedChannelBuilder.intercept(interceptor);
            } else {
                var defaultInterceptor = new HertCoreClientInterceptor() {
                    @Override
                    public void setRequestMetadata(Metadata metadata) {
                    }
                    @Override
                    public <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
                    }
                };
                managedChannelBuilder = managedChannelBuilder.intercept(HertCoreClientInterceptBuilder.builder(defaultInterceptor).build());
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
        HertsCoreService targetHertsCoreService = null;
        for (HertsCoreService registeredService : this.hertsCoreServices) {
            for (Class<?> registeredInterfaceName : registeredService.getClass().getInterfaces()) {
                if (!registeredInterfaceName.getName().equals(serviceName)) {
                    continue;
                }
                targetHertsCoreService = registeredService;
                break;
            }
        }

        if (targetHertsCoreService == null) {
            throw new HertsCoreClientBuildException("Not found " + serviceName + " in registration services");
        }

        switch (this.hertsType) {
            case Unary -> {
                var unary = newHertsBlockingService(channel, targetHertsCoreService);
                return (T) generateService(unary, interfaceType);
            }
            case BidirectionalStreaming -> {
                var streaming = newHertsBidirectionalStreamingService(channel, targetHertsCoreService);
                return (T) generateService(streaming, interfaceType);
            }
            case ServerStreaming -> {
                var serverStreaming = newHertsServerStreamingService(channel, targetHertsCoreService);
                return (T) generateService(serverStreaming, interfaceType);
            }
            case ClientStreaming -> {
                var clientStreaming = newHertsClientStreamingService(channel, targetHertsCoreService);
                return (T) generateService(clientStreaming, interfaceType);
            }
            default ->
                    throw new HertsCoreTypeInvalidException("Undefined Hert core type. HertsCoreType" + this.hertsType);
        }
    }

    private HertsCoreService generateService(InvocationHandler handler, Class<?> classType) {
        return (HertsCoreService) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
                handler);
    }

    private static HertsCoreClientUMethodHandler newHertsBlockingService(Channel channel, HertsCoreService hertsCoreService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientUMethodHandler(channel, callOptions, hertsCoreService);
                    }
                };
        return HertsCoreClientUMethodHandler.newStub(factory, channel);
    }

    private static HertsCoreClientBStreamingMethodHandler newHertsBidirectionalStreamingService(Channel channel, HertsCoreService hertsCoreService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientBStreamingMethodHandler(channel, callOptions, hertsCoreService);
                    }
                };
        return HertsCoreClientBStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsCoreClientSStreamingMethodHandler newHertsServerStreamingService(Channel channel, HertsCoreService hertsCoreService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientSStreamingMethodHandler(channel, callOptions, hertsCoreService);
                    }
                };
        return HertsCoreClientSStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsCoreClientCStreamingMethodHandler newHertsClientStreamingService(Channel channel, HertsCoreService hertsCoreService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientCStreamingMethodHandler(channel, callOptions, hertsCoreService);
                    }
                };
        return HertsCoreClientCStreamingMethodHandler.newStub(factory, channel);
    }
}
