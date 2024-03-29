package org.hertsstack.http;

import javax.servlet.ServletResponse;

class HertsHttpResponseImpl implements HertsHttpResponse {
    private ServletResponse response;

    public HertsHttpResponseImpl(ServletResponse response) {
        this.response = response;
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }
}
