package org.hertsstack.gateway;

/**
 * Herts gateway engine
 *
 * @author Herts Contributer
 * @version 1.0.0
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
