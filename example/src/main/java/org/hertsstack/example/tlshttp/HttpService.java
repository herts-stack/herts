package org.hertsstack.example.tlshttp;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    String helloWorld();
}
