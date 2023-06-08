package org.herts.e2etest.http;

import org.herts.core.logger.HertsLogger;
import org.herts.core.service.HertsServiceHttp;

import java.util.logging.Logger;

public class HttpServiceImpl02 extends HertsServiceHttp<HttpService02> implements HttpService02 {
    private static final Logger logger = HertsLogger.getLogger(HttpServiceImpl02.class.getSimpleName());

    public HttpServiceImpl02() {
    }

    @Override
    public String httpTest10() {
        logger.info("------------ Http httpTest10");
        return "Hello02";
    }
}
