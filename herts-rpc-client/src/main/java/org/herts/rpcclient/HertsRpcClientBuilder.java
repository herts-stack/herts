package org.herts.rpcclient;

import org.herts.rpcclient.handler.HertsRpcClientCStreamingMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientDStreamingMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientUMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientBStreamingMethodHandler;
import org.herts.rpcclient.handler.HertsRpcClientSStreamingMethodHandler;
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

    public HertsRpcClientBuilder(IBuilder builder) {
        this.connectedHost = builder.getConnectedHost();
        this.isSecureConnection = builder.isSecureConnection();
        this.channel = builder.getChannel();
        this.registeredIfServices = builder.getHertsRpcServices();
        this.hertsType = builder.getHertsType();
    }

    public static IBuilder builder(String connectedHost, int serverPort) {
        return new IBuilder(connectedHost, serverPort);
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
                var unary = newHertsBlockingService(channel, interfaceType);
                return (T) generateService(unary, interfaceType);
            }
            case BidirectionalStreaming -> {
                var streaming = newHertsBidirectionalStreamingService(channel, interfaceType);
                return (T) generateService(streaming, interfaceType);
            }
            case ServerStreaming -> {
                var serverStreaming = newHertsServerStreamingService(channel, interfaceType);
                return (T) generateService(serverStreaming, interfaceType);
            }
            case ClientStreaming -> {
                var clientStreaming = newHertsClientStreamingService(channel, interfaceType);
                return (T) generateService(clientStreaming, interfaceType);
            }
            case Reactive -> {
                var reactiveStreaming = newHertsBlockingService(channel, interfaceType);
                return (T) generateService(reactiveStreaming, interfaceType);
            }
            default ->
                    throw new HertsTypeInvalidException("Undefined Hert core type. HertsCoreType" + this.hertsType);
        }
    }

    private HertsService generateService(InvocationHandler handler, Class<?> classType) {
        return (HertsService) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
                handler);
    }

    private static HertsRpcClientUMethodHandler newHertsBlockingService(Channel channel, Class<?> hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientUMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientUMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientUMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientUMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientBStreamingMethodHandler newHertsBidirectionalStreamingService(Channel channel, Class<?> hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientBStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientBStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientBStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientBStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientSStreamingMethodHandler newHertsServerStreamingService(Channel channel, Class<?> hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientSStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientSStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientSStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientSStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientCStreamingMethodHandler newHertsClientStreamingService(Channel channel, Class<?> hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientCStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientCStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientCStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientCStreamingMethodHandler.newStub(factory, channel);
    }

    private static HertsRpcClientDStreamingMethodHandler newHertsReactiveStreamingService(Channel channel, Class<?> hertsRpcService) {
        io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientDStreamingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsRpcClientDStreamingMethodHandler>() {
                    @java.lang.Override
                    public HertsRpcClientDStreamingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsRpcClientDStreamingMethodHandler(channel, callOptions, hertsRpcService);
                    }
                };
        return HertsRpcClientDStreamingMethodHandler.newStub(factory, channel);
    }
}
