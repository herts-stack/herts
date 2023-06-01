package org.herts.example.tlshttp;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

@HertsHttp
public interface HttpService extends HertsService {
    void hello();
}
