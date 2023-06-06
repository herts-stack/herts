package org.herts.http.http;

import javax.servlet.ServletResponse;

public class HertsHttpResponseImpl implements HertsHttpResponse {
    private ServletResponse response;

    public HertsHttpResponseImpl(ServletResponse response) {
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }
}
