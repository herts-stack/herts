package org.herts.example.jwthttp;

import org.herts.core.service.HertsServiceHttp;
import org.herts.core.service.HertsService;

public class HttpServiceImpl extends HertsServiceHttp<HertsService> implements HttpService {
    @Override
    public String hellowWorld() {
        return "hello world";
    }
}
