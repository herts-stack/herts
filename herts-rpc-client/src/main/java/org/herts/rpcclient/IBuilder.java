package org.herts.rpcclient;

import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsNotSupportParameterTypeException;
import org.herts.common.exception.HertsRpcClientBuildException;
import org.herts.rpcclient.validator.HertsRpcClientValidator;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IBuilder implements HertsRpcClientIBuilder {
    private final List<Class<?>> hertsRpcServices = new ArrayList<>();
    private final String connectedHost;
    private final int serverPort;

    private HertsType hertsType;
    private boolean isSecureConnection;
    private Channel channel;
    private ClientInterceptor interceptor;
    private GrpcClientOption option;

    public IBuilder(String connectedHost, int serverPort) {
        this.connectedHost = connectedHost;
        this.serverPort = serverPort;
    }

    @Override
    public HertsRpcClientIBuilder secure(boolean isSecureConnection) {
        this.isSecureConnection = isSecureConnection;
        return this;
    }

    @Override
    public <T> HertsRpcClientIBuilder registerHertsRpcInterface(Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new HertsRpcClientBuildException("Please register Interface with extends HertsService");
        }
        this.hertsRpcServices.add(interfaceClass);
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
        if (this.hertsRpcServices.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
            throw new HertsRpcClientBuildException("Please register HertsService and host");
        }

        List<HertsType> hertsTypes = new ArrayList<>();
        for (Class<?> c : this.hertsRpcServices) {
            try {
                var annotation = c.getAnnotation(HertsRpc.class);
                hertsTypes.add(annotation.value());
            } catch (Exception ex) {
                throw new HertsRpcClientBuildException("Could not find @HertsRpc annotation in " + c.getName(), ex);
            }
        }
        if (!HertsRpcClientValidator.isSameHertsCoreType(hertsTypes)) {
            throw new HertsRpcClientBuildException("Please register same HertsCoreService. Not supported multiple different services");
        }

        var validateMsg = HertsRpcClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HertsRpcClientBuildException(validateMsg);
        }

        this.hertsType = hertsTypes.get(0);
        if (this.hertsType != HertsType.Unary && this.hertsType != HertsType.ServerStreaming) {
            if (!HertsRpcClientValidator.isStreamingRpc(this.hertsRpcServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
        }

        if (this.option == null) {
            this.option = new GrpcClientOption();
        }

        if (this.channel == null) {
            ManagedChannelBuilder<?> managedChannelBuilder = ManagedChannelBuilder.forAddress(this.connectedHost, this.serverPort);

            if (!this.isSecureConnection) {
                managedChannelBuilder = managedChannelBuilder.usePlaintext();
            }
            if (this.interceptor != null) {
                managedChannelBuilder = managedChannelBuilder.intercept(interceptor);
            } else {
                managedChannelBuilder = managedChannelBuilder.intercept(HertsRpcClientInterceptBuilder.builder(new HertsRpcClientEmptyInterceptor()).build());
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
        return new HertsRpcClientBuilder(this);
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
}
