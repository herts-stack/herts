package com.tomoyane.herts.hertscoreclient;

import com.tomoyane.herts.hertscommon.service.HertsService;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;

public interface HertsCoreClientBuilder {
    HertsCoreClientBuilder secure(boolean isSecureConnection);

    HertsCoreClientBuilder hertsImplementationService(HertsService hertsService);

    HertsCoreClientBuilder channel(Channel channel);

    HertsCoreClientBuilder interceptor(ClientInterceptor interceptor);

    HertsCoreClientBuilder grpcOption(GrpcClientOption option);

    HertsCoreClient build();
}