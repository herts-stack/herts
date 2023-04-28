package org.herts.rpcclient;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

/**
 * Herts core client builder interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcClientIBuilder {

    /**
     * Serure connection
     * @param isSecureConnection Secure
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder secure(boolean isSecureConnection);

    /**
     * Herts interface service
     * Not implementation class. Required @HertsRpc annotation
     * @param interfaceClass HertsCoreService
     * @return HertsCoreClientBuilder
     */
    <T> HertsRpcClientIBuilder registerHertsRpcInterface(Class<T> interfaceClass);

    /**
     * Channel for gRPC
     * @param channel Channel
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder channel(Channel channel);

    /**
     * Herts interceptor
     * @param interceptor ClientInterceptor
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder interceptor(ClientInterceptor interceptor);

    /**
     * gRPC option
     * @param option GrpcClientOption
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder grpcOption(GrpcClientOption option);

    /**
     * Connect to server
     * @return HertsRpcClient
     */
    HertsRpcClient connect();
}