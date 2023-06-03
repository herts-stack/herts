package org.herts.rpcclient;

import org.herts.rpcclient.handler.HertsRpcClientCStreamingMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientRStreamingMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientUMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientBStreamingMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientSStreamingMethodHandler;
import org.herts.rpcclient.modelx.ClientConnection;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsChannelIsNullException;
import org.herts.common.exception.HertsRpcClientBuildException;
import org.herts.common.exception.HertsTypeInvalidException;
import org.herts.common.service.HertsService;

import io.grpc.Channel;
import io.grpc.ManagedChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class HertsRpcClientBuilder implements HertsRpcClient {
    private final String connectedHost;
    private final HertsType hertsType;
    private final boolean isSecureConnection;
    private final List<Class<?>> registeredIfServices;
    private final Channel channel;
    private final ClientConnection clientConnection;

    public HertsRpcClientBuilder(IBuilder builder) {
        this.connectedHost = builder.getConnectedHost();
        this.isSecureConnection = builder.isSecureConnection();
        this.channel = builder.getChannel();
        this.registeredIfServices = builder.getHertsRpcServices();
        this.hertsType = builder.getHertsType();
        this.clientConnection = builder.getClientConnection();
    }

    public static IBuilder builder(String connectedHost, int serverPort) {
        return new IBuilder(connectedHost, serverPort);
    }

    public static IBuilder builder(String connectedHost) {
        int defaultPort = 9000;
        return new IBuilder(connectedHost, defaultPort);
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
    public ClientConnection getClientConnection() {
        return this.clientConnection;
    }

    @Override
    public <T extends HertsService> T createHertsRpcService(Class<T> interfaceType) {
        if (!interfaceType.isInterface()) {
            throw new HertsRpcClientBuildException(interfaceType.getSimpleName() + " is not interface. You can create client by interface");
        }

        var serviceName = interfaceType.getName();
        Class<?> target = null;
        for (Class<?> registeredService : this.registeredIfServices) {
            if (!serviceName.equals(registeredService.getName())) {
                continue;
            }
            target = registeredService;
        }

        if (target == null) {
            throw new HertsRpcClientBuildException("Not found " + serviceName + " in registration services");
        }

        switch (this.hertsType) {
            case Unary -> {
                var unary = newHertsBlockingService(this.channel, interfaceType, this.clientConnection);
                return (T) generateService(unary, interfaceType);
            }
            case BidirectionalStreaming -> {
                var streaming = newHertsBidirectionalStreamingService(this.channel, interfaceType, this.clientConnection);
                return (T) generateService(streaming, interfaceType);
            }
            case ServerStreaming -> {
                var serverStreaming = newHertsServerStreamingService(this.channel, interfaceType, this.clientConnection);
                return (T) generateService(serverStreaming, interfaceType);
            }
            case ClientStreaming -> {
                var clientStreaming = newHertsClientStreamingService(this.channel, interfaceType, this.clientConnection);
                return (T) generateService(clientStreaming, interfaceType);
            }
            case Reactive -> {
                var reactiveStreaming = newHertsBlockingService(this.channel, interfaceType, this.clientConnection);
                return (T) generateService(reactiveStreaming, interfaceType);
            }
            default -> throw new HertsTypeInvalidException("Undefined Hert core type. HertsCoreType" + this.hertsType);
        }
    }

    private HertsService generateService(InvocationHandler handler, Class<?> classType) {
        return (HertsService) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{classType},
                handler);
    }

    /**
     * Herts stub creation.
     * Herts Rpc Unary Method Handler
     *
     * @param channel Channel
     * @param hertsRpcService HertsService
     * @return HertsRpcClientUMethodHandler
     */
    private static HertsRpcClientUMethodHandler newHertsBlockingService(Channel channel, Class<?> hertsRpcService, ClientConnection clientConnection) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientUMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientUMethodHandler.newStub(factory, channel).withCallCredentials(clientConnection);
    }

    /**
     * Herts stub creation.
     * Herts Rpc Bidirectional Streaming Method Handler
     *
     * @param channel Channel
     * @param hertsRpcService HertsService
     * @return HertsRpcClientBStreamingMethodHandler
     */
    private static HertsRpcClientBStreamingMethodHandler newHertsBidirectionalStreamingService(
            Channel channel, Class<?> hertsRpcService, ClientConnection clientConnection) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientBStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientBStreamingMethodHandler.newStub(factory, channel).withCallCredentials(clientConnection);
    }

    /**
     * Herts stub creation.
     * Herts Rpc Server Streaming Method Handler
     *
     * @param channel Channel
     * @param hertsRpcService HertsService
     * @return HertsRpcClientSStreamingMethodHandler
     */
    private static HertsRpcClientSStreamingMethodHandler newHertsServerStreamingService(
            Channel channel, Class<?> hertsRpcService, ClientConnection clientConnection) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientSStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientSStreamingMethodHandler.newStub(factory, channel).withCallCredentials(clientConnection);
    }

    /**
     * Herts stub creation.
     * Herts Rpc Client Streaming Method Handler
     *
     * @param channel Channel
     * @param hertsRpcService HertsService
     * @return HertsRpcClientRStreamingMethodHandler
     */
    private static HertsRpcClientCStreamingMethodHandler newHertsClientStreamingService(
            Channel channel, Class<?> hertsRpcService, ClientConnection clientConnection) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientCStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientCStreamingMethodHandler.newStub(factory, channel).withCallCredentials(clientConnection);
    }

    /**
     * Herts stub creation.
     * Herts Rpc Client Reactive Streaming Method Handler
     *
     * @param channel Channel
     * @param hertsRpcService HertsService
     * @return HertsRpcClientRStreamingMethodHandler
     */
    private static HertsRpcClientRStreamingMethodHandler newHertsReactiveStreamingService(
            Channel channel, Class<?> hertsRpcService, ClientConnection clientConnection) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientRStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientRStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientRStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientRStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientRStreamingMethodHandler.newStub(factory, channel).withCallCredentials(clientConnection);
    }
}
