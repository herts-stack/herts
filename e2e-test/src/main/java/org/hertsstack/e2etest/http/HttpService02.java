package org.hertsstack.e2etest.http;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

@HertsHttp
public interface HttpService02 extends HertsService {
    String httpTest10();
}
