package org.herts.rpcclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsNotSupportParameterTypeException;
import org.herts.common.exception.HertsRpcClientBuildException;
import org.herts.common.service.HertsReactiveStreamingInternal;
import org.herts.common.service.HertsReceiver;
import org.herts.rpcclient.receiver.InternalReceiveStreaming;
import org.herts.rpcclient.validator.HertsRpcClientValidator;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IBuilder implements HertsRpcClientIBuilder {
    private final List<Class<?>> hertsRpcServices = new ArrayList<>();
    private final List<HertsReceiver> hertsRpcReceivers = new ArrayList<>();
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
    public <T> HertsRpcClientIBuilder registerHertsRpcServiceInterface(Class<T> serviceClass) {
        if (!serviceClass.isInterface()) {
            throw new HertsRpcClientBuildException("Please register Interface with extends HertsService");
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
        var serviceHertsTypes = getRegisteredServiceHertsTypes();
        if (!HertsRpcClientValidator.isSameHertsCoreType(serviceHertsTypes)) {
            throw new HertsRpcClientBuildException("Please register same HertsService. Not supported multiple different services");
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

        if (this.hertsType == HertsType.Reactive && this.hertsRpcReceivers.size() > 0) {
            for (HertsReceiver receiver : this.hertsRpcReceivers) {
                try {
                    new InternalReceiveStreaming(receiver).newHertsClientStreamingService(this.channel).registerReceiver(HertsReactiveStreamingInternal.class);
                    Thread.sleep(500);
                } catch (JsonProcessingException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new HertsRpcClientBuilder(this);
    }

    private List<HertsType> getRegisteredServiceHertsTypes() {
        List<HertsType> hertsTypes = new ArrayList<>();
        for (Class<?> c : this.hertsRpcServices) {
            try {
                var annotation = c.getAnnotation(HertsRpcService.class);
                hertsTypes.add(annotation.value());
            } catch (Exception ex) {
                throw new HertsRpcClientBuildException("Could not find @HertsRpcService annotation in " + c.getName(), ex);
            }
        }
        return hertsTypes;
    }

    private void validateHertsService() {
        if (this.hertsRpcServices.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
            throw new HertsRpcClientBuildException("Please register HertsService and host");
        }

        var validateMsg = HertsRpcClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HertsRpcClientBuildException(validateMsg);
        }

        if (this.hertsType != HertsType.Unary && this.hertsType != HertsType.ServerStreaming) {
            if (!HertsRpcClientValidator.isStreamingRpc(this.hertsRpcServices)) {
                throw new HertsNotSupportParameterTypeException("Support StreamObserver<T> parameter only of BidirectionalStreaming and ClientStreaming. Please remove other method parameter.");
            }
        }
    }

    private void validateHertsReceiver() {
        if (this.hertsRpcReceivers.size() == 0 || this.connectedHost == null || this.connectedHost.isEmpty()) {
            throw new HertsRpcClientBuildException("Please register HertsService and host");
        }

        var validateMsg = HertsRpcClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HertsRpcClientBuildException(validateMsg);
        }
        if (!HertsRpcClientValidator.isAllReturnVoidBy(this.hertsRpcReceivers)) {
            throw new HertsRpcClientBuildException("Please register void method only");
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
}
