package org.herts.example.http;

import org.herts.core.annotation.HertsHttp;
import org.herts.core.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    String helloWorld();
}
