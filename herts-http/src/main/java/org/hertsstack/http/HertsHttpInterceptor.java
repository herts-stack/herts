package org.hertsstack.http;

/**
 * Herts http server interceptor
 *
 * @author Herts Contributer
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
     *
     * @param response Herts Response class
     */
    void afterHandle(HertsHttpResponse response);
}
