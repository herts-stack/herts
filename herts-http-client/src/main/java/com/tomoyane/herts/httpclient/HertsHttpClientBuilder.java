package com.tomoyane.herts.httpclient;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

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
     * @param hertsCoreService HertsCoreService
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder hertsImplementationService(HertsCoreService hertsCoreService);

    /**
     * Build
     * @return HertsHttpClientBase
     */
    HertsHttpClientBase build();
}
