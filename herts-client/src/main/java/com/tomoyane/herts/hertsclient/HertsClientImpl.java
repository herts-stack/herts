package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertsclient.handlers.HertsClientCStreamingMethodHandler;
import com.tomoyane.herts.hertsclient.handlers.HertsClientUMethodHandler;
import com.tomoyane.herts.hertsclient.handlers.HertsClientBStreamingMethodHandler;
import com.tomoyane.herts.hertsclient.handlers.HertsClientSStreamingMethodHandler;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsChannelIsNullException;
import com.tomoyane.herts.hertscommon.exception.HertsCoreTypeInvalidException;
import com.tomoyane.herts.hertscore.service.HertsService;

import io.grpc.*;

import java.lang.reflect.Proxy;

public class HertsClientImpl implements HertsClient {
    private final String connectedHost;
    private final String serverPort;
    private final HertsCoreType hertsCoreType;
    private final boolean isSecureConnection;
    private final HertsService hertsService;

    private ManagedChannel channel;

    private HertsClientImpl(Builder builder) {
        this.connectedHost = builder.connectedHost;
        this.isSecureConnection = builder.isSecureConnection;
        this.hertsService = builder.hertsService;
        this.serverPort = builder.serverPort;
        this.hertsCoreType = builder.hertsCoreType;
    }

    public static class Builder {
        private final String connectedHost;
        private final String serverPort;
        private final HertsCoreType hertsCoreType;
        private boolean isSecureConnection;
        private HertsService hertsService;

        public Builder(String connectedHost, String serverPort, HertsCoreType hertsCoreType) {
            this.connectedHost = connectedHost;
            this.serverPort = serverPort;
            this.hertsCoreType = hertsCoreType;
        }

        public Builder secure(boolean isSecureConnection) {
            this.isSecureConnection = isSecureConnection;
            return this;
        }

        public Builder hertsService(HertsService hertsService) {
            this.hertsService = hertsService;
            return this;
        }

        public HertsClientImpl build() {
            if (this.hertsService == null || this.serverPort == null || this.serverPort.isEmpty() ||
                    this.connectedHost == null || this.connectedHost.isEmpty()) {
                throw new NullPointerException();
            }
            return new HertsClientImpl(this);
        }
    }

    public String getConnectedHost() {
        return connectedHost;
    }

    public boolean isSecureConnection() {
        return isSecureConnection;
    }

    public HertsCoreType getHertsCoreType() {
        return hertsCoreType;
    }

    public ManagedChannel getChannel() {
        if (this.channel == null) {
            throw new HertsChannelIsNullException("Please create HertService instance.");
        }
        return channel;
    }

    public HertsService createHertService(Class<?> classType) {
        ChannelCredentials credentials;
        // TODO: change it
        if (this.isSecureConnection) {
            credentials = InsecureChannelCredentials.create();
        } else {
            credentials = InsecureChannelCredentials.create();
        }

        this.channel = Grpc
                .newChannelBuilder(this.connectedHost + ":" + this.serverPort, credentials)
                .build();

        switch (this.hertsCoreType) {
            case Unary:
                var unary = newHertsBlockingService(channel, this.hertsService);
                return (HertsService) Proxy.newProxyInstance(
                        classType.getClassLoader(),
                        new Class<?>[]{ classType },
                        unary);
            case BidirectionalStreaming:
                var streaming = newHertsBidirectionalStreamingService(channel, this.hertsService);
                return (HertsService) Proxy.newProxyInstance(
                        classType.getClassLoader(),
                        new Class<?>[]{ classType },
                        streaming);
            case ServerStreaming:
                var serverStreaming = newHertsServerStreamingService(channel, this.hertsService);
                return (HertsService) Proxy.newProxyInstance(
                        classType.getClassLoader(),
                        new Class<?>[]{ classType },
                        serverStreaming);
            case ClientStreaming:
                var clientStreaming = newHertsClientStreamingService(channel, this.hertsService);
                return (HertsService) Proxy.newProxyInstance(
                        classType.getClassLoader(),
                        new Class<?>[]{ classType },
                        clientStreaming);
        }

        throw new HertsCoreTypeInvalidException("Undefined Hert core type");
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
