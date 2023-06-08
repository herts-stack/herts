package org.herts.example.jwthttp;

import org.herts.core.annotation.HertsHttp;
import org.herts.core.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    String hellowWorld();
}
