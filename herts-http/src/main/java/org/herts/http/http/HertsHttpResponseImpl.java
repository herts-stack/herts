package org.herts.http.http;

import jakarta.servlet.ServletResponse;

public class HertsHttpResponseImpl implements HertsHttpResponse {
    private ServletResponse response;

    public HertsHttpResponseImpl(ServletResponse response) {
    }

    public ServletResponse getResponse() {
        return response;
    }
}
