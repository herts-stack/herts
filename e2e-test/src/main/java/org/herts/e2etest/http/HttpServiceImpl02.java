package org.herts.e2etest.http;

import org.herts.core.logger.Logging;
import org.herts.core.service.HertsServiceHttp;

public class HttpServiceImpl02 extends HertsServiceHttp<HttpService02> implements HttpService02 {
    private static final java.util.logging.Logger logger = Logging.getLogger(HttpServiceImpl02.class.getSimpleName());

    public HttpServiceImpl02() {
    }

    @Override
    public String httpTest10() {
        logger.info("------------ Http httpTest10");
        return "Hello02";
    }
}
