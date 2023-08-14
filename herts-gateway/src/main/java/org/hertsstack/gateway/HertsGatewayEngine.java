package org.hertsstack.gateway;

/**
 * Herts gateway engine
 *
 * @author Herts Contributer
 */
public interface HertsGatewayEngine {

    /**
     * Start gateway server.
     */
    void start();

    /**
     * Stop gateway server.
     */
    void stop();
}
