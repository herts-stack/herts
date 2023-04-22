package com.tomoyane.herts.hertsrpcclient;

import com.tomoyane.herts.hertscommon.service.HertsRpcService;
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
     * Herts implementation service
     * Not interface class
     * @param hertsRpcService HertsCoreService
     * @return HertsCoreClientBuilder
     */
    HertsRpcClientIBuilder hertsImplementationService(HertsRpcService hertsRpcService);

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
     * Build
     * @return HertsCoreClient
     */
    HertsRpcClient build();
}