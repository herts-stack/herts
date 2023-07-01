package org.hertsstack.example.jwthttp;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    String hellowWorld();
}
