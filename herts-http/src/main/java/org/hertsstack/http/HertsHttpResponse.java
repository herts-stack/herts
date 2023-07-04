package org.hertsstack.http;

import javax.servlet.ServletResponse;

/**
 * Herts http response
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpResponse {
    /**
     * Get respone.
     *
     * @return ServletResponse
     */
    ServletResponse getResponse();
}
