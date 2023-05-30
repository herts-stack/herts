package org.herts.e2etest.http;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

@HertsHttp
public interface HttpService02 extends HertsService {
    String httpTest10();
}
