package com.tomoyane.herts.httpclient;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

public interface HertsHttpClientBuilder {
    HertsHttpClientBuilder secure(boolean isSecureConnection);

    HertsHttpClientBuilder port(int port);
    HertsHttpClientBuilder hertsImplementationService(HertsCoreService hertsCoreService);

    HertsHttpClient build();
}
