package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertsclient.handler.HertsClientCStreamingMethodHandler;
import com.tomoyane.herts.hertsclient.handler.HertsClientUMethodHandler;
import com.tomoyane.herts.hertsclient.handler.HertsClientBStreamingMethodHandler;
import com.tomoyane.herts.hertsclient.handler.HertsClientSStreamingMethodHandler;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsChannelIsNullException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreTypeInvalidException;
import com.tomoyane.herts.hertscore.service.HertsService;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HertsClientBuilder implements HertsClient {
    private final String connectedHost;
    private final int serverPort;
    private final HertsCoreType hertsCoreType;
    private final boolean isSecureConnection;
    private final HertsService hertsService;
    private final ClientInterceptor interceptor;
    private final List<ConnectionOption> connectionOptions;

    private Channel channel;

    private HertsClientBuilder(Builder builder) {
        this.connectedHost = builder.connectedHost;
        this.isSecureConnection = builder.isSecureConnection;
        this.hertsService = builder.hertsService;
        this.serverPort = builder.serverPort;
        this.hertsCoreType = builder.hertsCoreType;
        this.interceptor = builder.interceptor;
        this.channel = builder.channel;
        this.connectionOptions = builder.connectionOptions;
    }

    public static class ConnectionOption {
        private final GrpcConnectionOption grpcConnectionOption;
        private final long value;
        private final TimeUnit unit;

        public ConnectionOption(GrpcConnectionOption grpcConnectionOption, long value, TimeUnit unit) {
            this.grpcConnectionOption = grpcConnectionOption;
            this.value = value;
            this.unit = unit;
        }

        public static enum GrpcConnectionOption {
            IdleTimeout,
            KeepAliveTime,
            KeepAliveTimeout,
        }

        public GrpcConnectionOption getGrpcConnectionOption() {
            return grpcConnectionOption;
        }

        public long getValue() {
            return value;
        }

        public TimeUnit getUnit() {
            return unit;
        }
    }

    public static class Builder {
        // Required parameters
        private final String connectedHost;
        private final int serverPort;
        private final HertsCoreType hertsCoreType;

        // Optional parameters
        private boolean isSecureConnection;
        private HertsService hertsService;
        private Channel channel;
        private ClientInterceptor interceptor;
        private List<ConnectionOption> connectionOptions;

        private Builder(String connectedHost, int serverPort, HertsCoreType hertsCoreType) {
            this.connectedHost = connectedHost;
            this.serverPort = serverPort;
            this.hertsCoreType = hertsCoreType;
        }

        public static Builder create(String connectedHost, int serverPort, HertsCoreType hertsCoreType) {
            return new Builder(connectedHost, serverPort, hertsCoreType);
        }

        public Builder secure(boolean isSecureConnection) {
            this.isSecureConnection = isSecureConnection;
            return this;
        }

        public Builder hertsService(HertsService hertsService) {
            this.hertsService = hertsService;
            return this;
        }

        public Builder channel(Channel channel) {
            this.channel = channel;
            return this;
        }

        public Builder interceptor(ClientInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public Builder connectionOption(List<ConnectionOption> connectionOptions) {
            this.connectionOptions = connectionOptions;
            return this;
        }

        public HertsClient build() {
            if (this.hertsService == null || this.connectedHost == null || this.connectedHost.isEmpty()) {
                throw new NullPointerException();
            }
            return new HertsClientBuilder(this);
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
    public HertsService createHertService(Class<?> classType) {
        // If not null, using custom channel
        if (this.channel == null) {
            ManagedChannelBuilder<?> managedChannelBuilder = ManagedChannelBuilder.forAddress(this.connectedHost, this.serverPort);

            if (!this.isSecureConnection) {
                managedChannelBuilder = managedChannelBuilder.usePlaintext();
            }
            if (this.interceptor != null) {
                managedChannelBuilder = managedChannelBuilder.intercept(interceptor);
            }
            if (this.connectionOptions != null) {
                for (ConnectionOption option : this.connectionOptions) {
                    if (option.grpcConnectionOption == ConnectionOption.GrpcConnectionOption.IdleTimeout) {
                        managedChannelBuilder = managedChannelBuilder.idleTimeout(option.getValue(), option.getUnit());
                    } else if (option.grpcConnectionOption == ConnectionOption.GrpcConnectionOption.KeepAliveTime) {
                        managedChannelBuilder = managedChannelBuilder.keepAliveTime(option.getValue(), option.getUnit());
                    } else if (option.grpcConnectionOption == ConnectionOption.GrpcConnectionOption.KeepAliveTimeout) {
                        managedChannelBuilder = managedChannelBuilder.keepAliveTimeout(option.getValue(), option.getUnit());
                    }
                }
            }
            this.channel = managedChannelBuilder.build();
        }

        switch (this.hertsCoreType) {
            case Unary:
                var unary = newHertsBlockingService(channel, this.hertsService);
                return generateService(unary, classType);
            case BidirectionalStreaming:
                var streaming = newHertsBidirectionalStreamingService(channel, this.hertsService);
                return generateService(streaming, classType);
            case ServerStreaming:
                var serverStreaming = newHertsServerStreamingService(channel, this.hertsService);
                return generateService(serverStreaming, classType);
            case ClientStreaming:
                var clientStreaming = newHertsClientStreamingService(channel, this.hertsService);
                return generateService(clientStreaming, classType);
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

    private static HertsClientUMethodHandler newHertsBlockingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientUMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientUMethodHandler.newStub(factory, channel);
    }

    private static HertsClientBStreamingMethodHandler newHertsBidirectionalStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientBStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientBStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsClientSStreamingMethodHandler newHertsServerStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientSStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientSStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsClientCStreamingMethodHandler newHertsClientStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientCStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientCStreamingMethodHandler.newStub(factory, channel);
    }
}
