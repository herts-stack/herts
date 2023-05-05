package org.herts.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Herts http server
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpServer {

    /**
     * Do option with Servlet`
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    void doOptions(HttpServletRequest request, HttpServletResponse response);

    /**
     * Do post with Servlet
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException Failure processing
     */
    void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Get base endpoint
     * @return Endpoint
     */
    String getBaseEndpoint();

    /**
     * Get all endpoints
     * @return Endpoints
     */
    String[] getEndpoints();
}
