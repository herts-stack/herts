package org.herts.http;

import javax.servlet.ServletResponse;

class HertsHttpResponseImpl implements HertsHttpResponse {
    private ServletResponse response;

    public HertsHttpResponseImpl(ServletResponse response) {
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }
}
