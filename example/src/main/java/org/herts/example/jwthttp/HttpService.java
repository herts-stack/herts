package org.herts.example.jwthttp;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    String hellowWorld();
}
