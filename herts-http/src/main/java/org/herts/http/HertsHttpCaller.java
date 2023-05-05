package org.herts.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Herts http caller interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpCaller {

    /**
     * POST
     * @param hertsMethod Method reflection
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Get error
     */
    void post(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * Set Writer
     * @param out PrintWriter
     * @param msg String
     */
    void setWriter(PrintWriter out, String msg);

    /**
     * Set metrics
     * @param response HttpServletResponse
     * @throws IOException Get error
     */
    void setMetricsResponse(HttpServletResponse response) throws IOException;

    /**
     * Set herts http header
     * @param response HttpServletResponse
     */
    void setHertsHeader(HttpServletResponse response);
}
