package org.hertsstack.http;

import javax.servlet.ServletResponse;

/**
 * Herts http response
 *
 * @author Herts Contributer
 */
public interface HertsHttpResponse {
    /**
     * Get respone.
     *
     * @return ServletResponse
     */
    ServletResponse getResponse();
}
