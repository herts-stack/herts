package org.herts.e2etest.http;

import org.herts.core.annotation.HertsHttp;
import org.herts.core.service.HertsService;

@HertsHttp
public interface HttpService02 extends HertsService {
    String httpTest10();
}
