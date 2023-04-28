package org.herts.http.http;

import jakarta.servlet.ServletRequest;

import javax.annotation.Nullable;

public interface HertsHttpRequest {

    /**
     * Get ServletRequest.
     * Depends on jakarta.servlet.ServletRequest
     * @return jakarta.servlet.ServletRequest
     */
    ServletRequest getRequest();

    /**
     * Get Header value
     * @param headerName Header name
     * @return Value
     */
    @Nullable
    String getHeader(String headerName);

    /**
     * Get remote address
     * @return Remote address
     */
    String getRemoteAddr();

    /**
     * Get local address
     * @return Local address
     */
    String getLocalAddr();

    /**
     * Get header auth type
     * @return Auth Type
     */
    String getAuthType();

    /**
     * Get Cookies
     * @return Cookies
     */
    HertsHttpRequestImpl.Cookie[] getCookies();
}
