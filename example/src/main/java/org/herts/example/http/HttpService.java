package org.herts.example.http;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    String hellowWorld();
}
