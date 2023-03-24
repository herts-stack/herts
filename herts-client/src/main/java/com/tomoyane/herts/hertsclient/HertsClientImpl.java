package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertsclient.handlers.HertsClientUnaryMethodHandler;
import com.tomoyane.herts.hertsclient.handlers.HertsClientBidirectionalStreamingMethodHandler;
import com.tomoyane.herts.hertsclient.handlers.HertsClientServerStreamingMethodHandler;
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
            case ClientStreaming, BidirectionalStreaming:
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
        }

        throw new HertsCoreTypeInvalidException("Undefined Hert core type");
    }

    private static HertsClientUnaryMethodHandler newHertsBlockingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientUnaryMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientUnaryMethodHandler>() {
                    @java.lang.Override
                    public HertsClientUnaryMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientUnaryMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientUnaryMethodHandler.newStub(factory, channel);
    }

    private static HertsClientBidirectionalStreamingMethodHandler newHertsBidirectionalStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientBidirectionalStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientBidirectionalStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientBidirectionalStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientBidirectionalStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientBidirectionalStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsClientServerStreamingMethodHandler newHertsServerStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientServerStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientServerStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientServerStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientServerStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientServerStreamingMethodHandler.newStub(factory, channel);
    }
}
