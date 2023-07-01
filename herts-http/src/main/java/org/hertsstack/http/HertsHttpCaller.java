package org.hertsstack.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Herts http caller interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsHttpCaller {

    /**
     * POST
     *
     * @param hertsMethod Method reflection
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @throws Exception Get error
     */
    void post(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * Set metrics
     *
     * @param response HttpServletResponse
     * @throws IOException Get error
     */
    void setMetricsResponse(HttpServletResponse response) throws IOException;

    /**
     * Set herts http header
     *
     * @param response HttpServletResponse
     */
    void setHertsHeader(HttpServletResponse response);
}
