package org.hertsstack.rpcclient;

import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.ChannelIsNullException;
import org.hertsstack.core.exception.RpcClientBuildException;
import org.hertsstack.core.exception.TypeInvalidException;
import org.hertsstack.core.service.HertsService;

import io.grpc.CallCredentials;
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
    private final ClientRequestInfo clientConnection;

    public HertsRpcClientBuilder(IBuilder builder) {
        this.connectedHost = builder.getConnectedHost();
        this.isSecureConnection = builder.isSecureConnection();
        this.channel = builder.getChannel();
        this.registeredIfServices = builder.getHertsRpcServices();
        this.hertsType = builder.getHertsType();
        this.clientConnection = builder.getClientConnection();
    }

    public static HertsRpcClientIBuilder builder(String connectedHost, int serverPort) {
        return new IBuilder(connectedHost, serverPort);
    }

    public static HertsRpcClientIBuilder builder(String connectedHost) {
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
    public String getClient() {
        return this.clientConnection.getClientId();
    }

    @Override
    public ManagedChannel getChannel() {
        if (this.channel == null) {
            throw new ChannelIsNullException("Please create HertService instance.");
        }
        return (ManagedChannel) channel;
    }

    @Override
    public ClientRequestInfo getClientConnection() {
        return this.clientConnection;
    }

    @Override
    public <T extends HertsService> T createHertsRpcService(Class<T> interfaceType) {
        return hertsRpcService(interfaceType, null);
    }

    @Override
    public <T extends HertsService> T createHertsRpcService(Class<T> interfaceClass, CallCredentials credentials) {
        return hertsRpcService(interfaceClass, credentials);
    }

    @Override
    public HertsService createUnknownHertsRpcService(Class<?> interfaceClass) {
        return create(interfaceClass, null);
    }

    @SuppressWarnings("unchecked")
    private <T extends HertsService> T hertsRpcService(Class<T> interfaceType, CallCredentials credentials) {
        return (T) create(interfaceType, credentials);
    }

    private HertsService generateService(InvocationHandler handler, Class<?> classType) {
        return (HertsService) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{classType},
                handler);
    }

    private HertsService create(Class<?> interfaceType, CallCredentials credentials) {
        if (!interfaceType.isInterface()) {
            throw new RpcClientBuildException(interfaceType.getSimpleName() + " is not interface. You can create client by interface");
        }

        String serviceName = interfaceType.getName();
        Class<?> target = null;
        for (Class<?> registeredService : this.registeredIfServices) {
            if (!serviceName.equals(registeredService.getName())) {
                continue;
            }
            target = registeredService;
        }

        if (target == null) {
            throw new RpcClientBuildException("Not found " + serviceName + " in registration services");
        }

        switch (this.hertsType) {
            case Unary:
                HertsRpcClientUMethodHandler unary = newHertsBlockingService(this.channel, interfaceType, this.clientConnection, credentials);
                return generateService(unary, interfaceType);

            case BidirectionalStreaming:
                HertsRpcClientBStreamingMethodHandler streaming = newHertsBidirectionalStreamingService(this.channel, interfaceType, this.clientConnection, credentials);
                return generateService(streaming, interfaceType);

            case ServerStreaming:
                HertsRpcClientSStreamingMethodHandler serverStreaming = newHertsServerStreamingService(this.channel, interfaceType, this.clientConnection, credentials);
                return generateService(serverStreaming, interfaceType);

            case ClientStreaming:
                HertsRpcClientCStreamingMethodHandler clientStreaming = newHertsClientStreamingService(this.channel, interfaceType, this.clientConnection, credentials);
                return generateService(clientStreaming, interfaceType);

            case Reactive:
                HertsRpcClientUMethodHandler reactiveStreaming = newHertsBlockingService(this.channel, interfaceType, this.clientConnection, credentials);
                return generateService(reactiveStreaming, interfaceType);

            default:
                throw new TypeInvalidException("Undefined Herts type. HertsType: " + this.hertsType);
        }

    }

    /**
     * Herts stub creation.
     * Herts Rpc Unary Method Handler
     *
     * @param channel Channel
     * @param hertsRpcService HertsService
     * @return HertsRpcClientUMethodHandler
     */
    private static HertsRpcClientUMethodHandler newHertsBlockingService(
            Channel channel, Class<?> hertsRpcService, ClientRequestInfo clientConnection, CallCredentials credentials) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientUMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };

        if (credentials == null) {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection);
        } else {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection)
                    .withCallCredentials(credentials);
        }
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
            Channel channel, Class<?> hertsRpcService, ClientRequestInfo clientConnection, CallCredentials credentials) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientBStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };

        if (credentials == null) {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection);
        } else {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection)
                    .withCallCredentials(credentials);
        }
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
            Channel channel, Class<?> hertsRpcService, ClientRequestInfo clientConnection, CallCredentials credentials) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientSStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };

        if (credentials == null) {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection);
        } else {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection)
                    .withCallCredentials(credentials);
        }
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
            Channel channel, Class<?> hertsRpcService, ClientRequestInfo clientConnection, CallCredentials credentials) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientCStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };

        if (credentials == null) {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection);
        } else {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection)
                    .withCallCredentials(credentials);
        }
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
            Channel channel, Class<?> hertsRpcService, ClientRequestInfo clientConnection, CallCredentials credentials) {

        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientRStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientRStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientRStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientRStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };

        if (credentials == null) {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection);
        } else {
            return HertsRpcClientUMethodHandler.newStub(factory, channel)
                    .withCallCredentials(clientConnection)
                    .withCallCredentials(credentials);
        }
    }
}
