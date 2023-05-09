package org.herts.example.http;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsHttpService;

import java.util.logging.Logger;

public class HttpServiceImpl02 extends HertsHttpService<HttpService02> implements HttpService02 {
    private static final Logger logger = HertsLogger.getLogger(HttpServiceImpl02.class.getSimpleName());

    public HttpServiceImpl02() {
    }

    @Override
    public String httpTest10() {
        logger.info("------------ Http httpTest10");
        return "Hello02";
    }
}