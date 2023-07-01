package org.hertsstack.http;

/**
 * Herts http server interceptor
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpInterceptor {

    /**
     * Before handle intercept
     *
     * @param request Herts Request class
     */
    void beforeHandle(HertsHttpRequest request);

    /**
     * After handle intercept
     */
    void afterHandle();
}
