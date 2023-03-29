package com.tomoyane.herts.hertscoreclient;

import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientCStreamingMethodHandler;
import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientUMethodHandler;
import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientBStreamingMethodHandler;
import com.tomoyane.herts.hertscoreclient.handler.HertsCoreClientSStreamingMethodHandler;
import com.tomoyane.herts.hertscoreclient.validator.HertsCoreClientValidator;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsChannelIsNullException;
import com.tomoyane.herts.hertscommon.exception.HertsClientBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreTypeInvalidException;
import com.tomoyane.herts.hertscommon.exception.HertsNotSupportParameterTypeException;
import com.tomoyane.herts.hertscommon.service.HertsService;

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

public class HertsCoreClientBuilderImpl implements HertsCoreClient {
    private final String connectedHost;
    private final int serverPort;
    private final HertsCoreType hertsCoreType;
    private final boolean isSecureConnection;
    private final ClientInterceptor interceptor;
    private final GrpcClientOption option;
    private final List<HertsService> hertsServices;

    private Channel channel;

    private HertsCoreClientBuilderImpl(Builder builder) {
        this.connectedHost = builder.connectedHost;
        this.isSecureConnection = builder.isSecureConnection;
        this.serverPort = builder.serverPort;
        this.hertsCoreType = builder.hertsCoreType;
        this.interceptor = builder.interceptor;
        this.channel = builder.channel;
        this.option = builder.option;
        this.hertsServices = builder.hertsServices;
    }

    public static class Builder implements HertsCoreClientBuilder {
        private final List<HertsService> hertsServices = new ArrayList<>();
        private final String connectedHost;
        private final int serverPort;
        private final HertsCoreType hertsCoreType;

        private boolean isSecureConnection;
        private Channel channel;
        private ClientInterceptor interceptor;
        private GrpcClientOption option;

        private Builder(String connectedHost, int serverPort, HertsCoreType hertsCoreType) {
            this.connectedHost = connectedHost;
            this.serverPort = serverPort;
            this.hertsCoreType = hertsCoreType;
        }

        public static HertsCoreClientBuilder create(String connectedHost, int serverPort, HertsCoreType hertsCoreType) {
            return new Builder(connectedHost, serverPort, hertsCoreType);
        }

        @Override
        public HertsCoreClientBuilder secure(boolean isSecureConnection) {
            this.isSecureConnection = isSecureConnection;
            return this;
        }

        @Override
        public HertsCoreClientBuilder hertsImplementationService(HertsService hertsService) {
            this.hertsServices.add(hertsService);
            return this;
        }

        @Override
        public HertsCoreClientBuilder channel(Channel channel) {
            this.channel = channel;
            return this;
        }

        @Override
        public HertsCoreClientBuilder interceptor(ClientInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        @Override
        public HertsCoreClientBuilder grpcOption(GrpcClientOption option) {
            this.option = option;
            return this;
        }

        @Override
        public HertsCoreClient build() {
            if (this.hertsServices.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
                throw new HertsClientBuildException("Please register HertsService and host");
            }
            List<HertsCoreType> hertsCoreTypes = this.hertsServices.stream().map(HertsService::getHertsCoreType).toList();
            if (!HertsCoreClientValidator.isSameHertsCoreType(hertsCoreTypes)) {
                throw new HertsClientBuildException("Please register same HertsCoreService. Not supported multiple different services");
            }

            var validateMsg = HertsCoreClientValidator.validateRegisteredServices(this.hertsServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsClientBuildException(validateMsg);
            }

            if (!HertsCoreClientValidator.isValidStreamingRpc(this.hertsServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
            if (this.option == null) {
                this.option = new GrpcClientOption();
            }
            return new HertsCoreClientBuilderImpl(this);
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
    public HertsCoreType getHertsCoreType() {
        return hertsCoreType;
    }

    @Override
    public ManagedChannel getChannel() {
        if (this.channel == null) {
            throw new HertsChannelIsNullException("Please create HertService instance.");
        }
        return (ManagedChannel) channel;
    }

    @Override
    public <T extends HertsService> T createHertService(Class<T> classType) {
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
                managedChannelBuilder = managedChannelBuilder.intercept(HertCoreClientInterceptorBuilderImpl.Builder.create(defaultInterceptor).build());
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

        var serviceName = classType.getName();
        HertsService targetHertsService = null;
        for (HertsService service : this.hertsServices) {
            for (Class<?> interfaceName : service.getClass().getInterfaces()) {
                if (!interfaceName.getName().equals(serviceName)) {
                    continue;
                }
                targetHertsService = service;
                break;
            }
        }
        if (targetHertsService == null) {
            throw new HertsClientBuildException("Not found " + serviceName + " in registration services");
        }

        switch (this.hertsCoreType) {
            case Unary:
                var unary = newHertsBlockingService(channel, targetHertsService);
                return (T) generateService(unary, classType);
            case BidirectionalStreaming:
                var streaming = newHertsBidirectionalStreamingService(channel, targetHertsService);
                return (T) generateService(streaming, classType);
            case ServerStreaming:
                var serverStreaming = newHertsServerStreamingService(channel, targetHertsService);
                return (T) generateService(serverStreaming, classType);
            case ClientStreaming:
                var clientStreaming = newHertsClientStreamingService(channel, targetHertsService);
                return (T) generateService(clientStreaming, classType);
            default:
                throw new HertsCoreTypeInvalidException("Undefined Hert core type. HertsCoreType" + this.hertsCoreType);
        }
    }

    private HertsService generateService(InvocationHandler handler, Class<?> classType) {
        return (HertsService) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
                handler);
    }

    private static HertsCoreClientUMethodHandler newHertsBlockingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientUMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsCoreClientUMethodHandler.newStub(factory, channel);
    }

    private static HertsCoreClientBStreamingMethodHandler newHertsBidirectionalStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientBStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsCoreClientBStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsCoreClientSStreamingMethodHandler newHertsServerStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientSStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsCoreClientSStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsCoreClientCStreamingMethodHandler newHertsClientStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsCoreClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsCoreClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsCoreClientCStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsCoreClientCStreamingMethodHandler.newStub(factory, channel);
    }
}
