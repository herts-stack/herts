package org.herts.http.engine;

/**
 * Herts http engine
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
     */
    void stop() throws Exception;
}
