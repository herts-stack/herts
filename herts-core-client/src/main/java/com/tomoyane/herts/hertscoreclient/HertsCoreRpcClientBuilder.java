package com.tomoyane.herts.hertscoreclient;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;

/**
 * Herts core client builder interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsCoreRpcClientBuilder {

    /**
     * Serure connection
     * @param isSecureConnection Secure
     * @return HertsCoreClientBuilder
     */
    HertsCoreRpcClientBuilder secure(boolean isSecureConnection);

    /**
     * Herts implementation service
     * Not interface class
     * @param hertsCoreService HertsCoreService
     * @return HertsCoreClientBuilder
     */
    HertsCoreRpcClientBuilder hertsImplementationService(HertsCoreService hertsCoreService);

    /**
     * Channel for gRPC
     * @param channel Channel
     * @return HertsCoreClientBuilder
     */
    HertsCoreRpcClientBuilder channel(Channel channel);

    /**
     * Herts interceptor
     * @param interceptor ClientInterceptor
     * @return HertsCoreClientBuilder
     */
    HertsCoreRpcClientBuilder interceptor(ClientInterceptor interceptor);

    /**
     * gRPC option
     * @param option GrpcClientOption
     * @return HertsCoreClientBuilder
     */
    HertsCoreRpcClientBuilder grpcOption(GrpcClientOption option);

    /**
     * Build
     * @return HertsCoreClient
     */
    HertsCoreClient build();
}