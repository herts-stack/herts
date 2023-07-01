package org.hertsstack.example.http;

import org.hertsstack.core.service.HertsServiceHttp;

public class HttpServiceImpl extends HertsServiceHttp<HttpService> implements HttpService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
