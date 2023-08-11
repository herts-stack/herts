package org.hertsstack.rpcclient;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import org.hertsstack.core.service.HertsReceiver;

/**
 * Herts core client builder interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcClientIBuilder {

    /**
     * Serure connection
     *
     * @param isSecureConnection Secure
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder secure(boolean isSecureConnection);

    /**
     * Herts interface service
     * Not implementation class. Required @HertsRpc annotation
     *
     * @param serviceClass HertsService
     * @return HertsCoreClientBuilder
     * @param <T> HertsService class type
     */
    <T> HertsRpcClientIBuilder registerHertsRpcServiceInterface(Class<T> serviceClass);

    /**
     * Herts receiver service
     *
     * @param hertsReceiver HertsReceiver
     * @return HertsRpcClientIBuilder
     */
    HertsRpcClientIBuilder registerHertsRpcReceiver(HertsReceiver hertsReceiver);

    /**
     * Herts RPC automatic reconnection.
     * Default false.
     * If set true, you don't need to implement auto reconnect.
     * Its try to reconnect to server when server is down/keepalive failure.
     *
     * @return HertsRpcClientIBuilder
     */
    HertsRpcClientIBuilder autoReconnection(boolean enableAutoReconnection);

    /**
     * Channel for gRPC
     *
     * @param channel Channel
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder channel(Channel channel);

    /**
     * Herts interceptor
     *
     * @param interceptor ClientInterceptor
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder interceptor(ClientInterceptor interceptor);

    /**
     * gRPC option
     *
     * @param option GrpcClientOption
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder grpcOption(GrpcClientOption option);

    /**
     * Connect to server
     *
     * @return HertsRpcClient
     */
    HertsRpcClient connect();
}