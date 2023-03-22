package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertsclient.handlers.HertsClientBlockingMethodHandler;
import com.tomoyane.herts.hertsclient.handlers.HertsClientStreamingMethodHandler;
import com.tomoyane.herts.hertscommon.enums.HertsCoreType;
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
            case ClientStreaming, ServerStreaming, BidirectionalStreaming:
                var streaming = newHertsStreamingService(channel, this.hertsService);
                return (HertsService) Proxy.newProxyInstance(
                        classType.getClassLoader(),
                        new Class<?>[]{ classType },
                        streaming);
        }

        throw new HertsCoreTypeInvalidException("Undefined Hert core type");
    }

    private static HertsClientBlockingMethodHandler newHertsBlockingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientBlockingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientBlockingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientBlockingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientBlockingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientBlockingMethodHandler.newStub(factory, channel);
    }

    private static HertsClientStreamingMethodHandler newHertsStreamingService(Channel channel, HertsService hertsService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientStreamingMethodHandler(channel, callOptions, hertsService);
                    }
                };
        return HertsClientStreamingMethodHandler.newStub(factory, channel);
    }
}
