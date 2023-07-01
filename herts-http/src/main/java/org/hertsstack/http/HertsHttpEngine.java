package org.hertsstack.http;

/**
 * Herts http engine
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpEngine {

    /**
     * Start server
     */
    void start();

    /**
     * Stop server
     *
     * @throws Exception If fail shutdown
     */
    void stop() throws Exception;
}
