package org.hertsstack.httpclient;

import java.util.List;

/**
 * Herts http client builder interface
 *
 * @author Herts Contributer
 */
public interface HertsHttpClientBuilder {

    /**
     * Secure or not
     *
     * @param isSecureConnection IsSecure
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder secure(boolean isSecureConnection);

    /**
     * Set port
     *
     * @param port Porr
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder port(int port);

    /**
     * Herts interface service
     * Not implementation class. Required @HertsHttp annotation
     *
     * @param interfaceClass Interface class
     * @return HertsHttpClientBuilder
     * @param <T> HertsService interface
     */
    <T> HertsHttpClientBuilder registerHertsService(Class<T> interfaceClass);

    /**
     * Gateway or not
     *
     * @param isGateway IsGateway
     * @return HertsHttpClientBuilder
     */
    HertsHttpClientBuilder gatewayApi(boolean isGateway);

    /**
     * Get HertsRpcService of list
     *
     * @return HertsRpcService
     */
    List<Class<?>> getHertsRpcServices();

    /**
     * Get server host
     *
     * @return Host
     */
    String getHost();

    /**
     * Get Server port
     *
     * @return Server port
     */
    int getServerPort();

    /**
     * Get secure or not
     *
     * @return Result
     */
    boolean isSecureConnection();

    /**
     * Get gateway or not
     *
     * @return Result
     */
    boolean isGateway();

    /**
     * Build
     *
     * @return HertsHttpClient
     */
    HertsHttpClient build();
}
