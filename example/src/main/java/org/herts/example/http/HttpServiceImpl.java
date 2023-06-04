package org.herts.example.http;

import org.herts.common.service.HertsHttpService;

public class HttpServiceImpl extends HertsHttpService<HttpService> implements HttpService {
    @Override
    public String hellowWorld() {
        return "hello world";
    }
}
