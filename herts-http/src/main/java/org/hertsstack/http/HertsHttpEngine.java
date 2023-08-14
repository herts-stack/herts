package org.hertsstack.http;

/**
 * Herts http engine
 *
 * @author Herts Contributer
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
