package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertscommon.service.HertsService;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.List;

public interface HertsClientBuilder {
    HertsClientBuilder secure(boolean isSecureConnection);

    HertsClientBuilder hertsImplementationService(HertsService hertsService);

    HertsClientBuilder channel(Channel channel);

    HertsClientBuilder interceptor(ClientInterceptor interceptor);

    HertsClientBuilder connectionOption(List<HertsClientBuilderImpl.ConnectionOption> connectionOptions);

    HertsClient build();
}