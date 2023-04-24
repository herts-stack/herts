package org.herts.http;

/**
 * Herts http server interceptor
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpInterceptor {

    /**
     * Before handle intercept
     */
    void beforeHandle();

    /**
     * After handle intercept
     */
    void afterHandle();
}
