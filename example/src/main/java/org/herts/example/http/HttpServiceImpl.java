package org.herts.example.http;

import org.herts.core.service.HertsServiceHttp;

public class HttpServiceImpl extends HertsServiceHttp<HttpService> implements HttpService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
