package com.tomoyane.herts.httpclient;

import com.tomoyane.herts.hertscommon.service.HertsRpcService;

/**
 * Herts http client builder interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpClientBuilder {

    /**
     * Secure or not
     * @param isSecureConnection IsSecure
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder secure(boolean isSecureConnection);

    /**
     * Set port
     * @param port Porr
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder port(int port);

    /**
     * Herts implementation service
     * @param hertsRpcService HertsCoreService
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder hertsImplementationService(HertsRpcService hertsRpcService);

    /**
     * Build
     * @return HertsHttpClientBase
     */
    HertsHttpClientBase build();
}
