package org.herts.example.jwthttp;

import org.herts.common.service.HertsHttpService;
import org.herts.common.service.HertsService;

public class HttpServiceImpl extends HertsHttpService<HertsService> implements HttpService {
    @Override
    public String hellowWorld() {
        return "hello world";
    }
}
