package org.hertsstack.rpcclient;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.serializer.MessageJsonParsingException;
import org.hertsstack.core.exception.NotSupportParameterTypeException;
import org.hertsstack.core.exception.RpcClientBuildException;
import org.hertsstack.core.service.ReactiveStreaming;
import org.hertsstack.core.service.HertsReceiver;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * HertsRpcClientIBuilder implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class IBuilder implements HertsRpcClientIBuilder {
    private final List<Class<?>> hertsRpcServices = new ArrayList<>();
    private final List<HertsReceiver> hertsRpcReceivers = new ArrayList<>();
    private final String connectedHost;
    private final int serverPort;
    private final ClientConnection clientConnection;

    private HertsType hertsType;
    private boolean isSecureConnection;
    private Channel channel;
    private ClientInterceptor interceptor;
    private GrpcClientOption option;

    public IBuilder(String connectedHost, int serverPort) {
        this.connectedHost = connectedHost;
        this.serverPort = serverPort;
        this.clientConnection = ClientConnection.create();
    }

    @Override
    public HertsRpcClientIBuilder secure(boolean isSecureConnection) {
        this.isSecureConnection = isSecureConnection;
        return this;
    }

    @Override
    public <T> HertsRpcClientIBuilder registerHertsRpcServiceInterface(Class<T> serviceClass) {
        if (!serviceClass.isInterface()) {
            throw new RpcClientBuildException("Please register Interface with extends HertsService");
        }
        this.hertsRpcServices.add(serviceClass);
        return this;
    }

    @Override
    public HertsRpcClientIBuilder registerHertsRpcReceiver(HertsReceiver hertsReceiver) {
        this.hertsRpcReceivers.add(hertsReceiver);
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
    public HertsRpcClient connect() {
        List<HertsType> serviceHertsTypes = getRegisteredServiceHertsTypes();
        if (!RpcClientValidator.isSameHertsCoreType(serviceHertsTypes)) {
            throw new RpcClientBuildException("Please register same HertsService. Not supported multiple different services");
        }
        this.hertsType = serviceHertsTypes.get(0);

        if (this.hertsType != HertsType.Reactive) {
            validateHertsService();
        } else {
            if (this.hertsRpcReceivers.size() > 0) {
                validateHertsReceiver();
            }
        }

        if (this.option == null) {
            this.option = new GrpcClientOption();
        }

        if (this.channel == null) {
            ConnectionManager manager = new ConnectionManager(this.channel, this.option);
            this.channel = manager.connect(this.connectedHost, this.serverPort, this.isSecureConnection, this.interceptor);
            manager.reconnectListener(this::registerReceivers);
        }

        if (this.hertsType == HertsType.Reactive && this.hertsRpcReceivers.size() > 0) {
            registerReceivers(this.channel);
        }
        HertsRpcClientBuilder hertsRpcClientBuilder = new HertsRpcClientBuilder(this);
        return hertsRpcClientBuilder;
    }

    public void registerReceivers(Channel channel) {
        for (HertsReceiver receiver : this.hertsRpcReceivers) {
            try {
                InternalReactiveReceiver.create(receiver, this.clientConnection)
                        .newHertsReactiveStreamingService(channel)
                        .registerReceiver(ReactiveStreaming.class);

                Thread.sleep(500);
            } catch (MessageJsonParsingException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<HertsType> getRegisteredServiceHertsTypes() {
        List<HertsType> hertsTypes = new ArrayList<>();
        for (Class<?> c : this.hertsRpcServices) {
            try {
                HertsRpcService annotation = c.getAnnotation(HertsRpcService.class);
                hertsTypes.add(annotation.value());
            } catch (Exception ex) {
                throw new RpcClientBuildException("Could not find @HertsRpcService annotation in " + c.getName(), ex);
            }
        }
        return hertsTypes;
    }

    private void validateHertsService() {
        if (this.hertsRpcServices.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
            throw new RpcClientBuildException("Please register HertsService and host");
        }

        String validateMsg = RpcClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new RpcClientBuildException(validateMsg);
        }

        if (this.hertsType != HertsType.Unary && this.hertsType != HertsType.ServerStreaming) {
            if (!RpcClientValidator.isStreamingRpc(this.hertsRpcServices)) {
                throw new NotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
        }
    }

    private void validateHertsReceiver() {
        if (this.hertsRpcReceivers.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
            throw new RpcClientBuildException("Please register HertsService and host");
        }

        String validateMsg = RpcClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new RpcClientBuildException(validateMsg);
        }
        if (!RpcClientValidator.isAllReturnVoidBy(this.hertsRpcReceivers)) {
            throw new RpcClientBuildException("Please register void method only");
        }
    }

    public List<HertsReceiver> getHertsRpcReceivers() {
        return hertsRpcReceivers;
    }

    public List<Class<?>> getHertsRpcServices() {
        return hertsRpcServices;
    }

    public String getConnectedHost() {
        return connectedHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public HertsType getHertsType() {
        return hertsType;
    }

    public boolean isSecureConnection() {
        return isSecureConnection;
    }

    public Channel getChannel() {
        return channel;
    }

    public ClientInterceptor getInterceptor() {
        return interceptor;
    }

    public GrpcClientOption getOption() {
        return option;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }
}
