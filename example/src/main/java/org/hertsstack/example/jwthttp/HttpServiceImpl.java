package org.hertsstack.example.jwthttp;

import org.hertsstack.core.service.HertsServiceHttp;
import org.hertsstack.core.service.HertsService;

public class HttpServiceImpl extends HertsServiceHttp<HertsService> implements HttpService {
    @Override
    public String hellowWorld() {
        return "hello world";
    }
}
