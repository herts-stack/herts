package org.herts.httpclient;

import java.util.List;

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
     * Not implementation class. Required @HertsHttp annotation
     * @param interfaceClass Interface class
     * @return HertsHttpClientBuilder
     */
    <T> HertsHttpClientBuilder registerHertService(Class<T> interfaceClass);

    /**
     * Get HertsRpcService of list
     * @return HertsRpcService
     */
    List<Class<?>> getHertsRpcServices();

    /**
     * Get server host
     * @return Host
     */
    String getHost();

    /**
     * Get Server port
     * @return Server port
     */
    int getServerPort();

    /**
     * Get secure or not
     * @return Result
     */
    boolean isSecureConnection();

    /**
     * Build
     * @return HertsHttpClientBase
     */
    HertsHttpClientBase build();
}
