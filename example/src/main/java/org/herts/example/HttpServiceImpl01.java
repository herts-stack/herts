package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HttpServiceService;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class HttpServiceImpl01 extends HttpServiceService<HttpService01> implements HttpService01 {
    private static final Logger logger = HertsLogger.getLogger(HttpServiceImpl01.class.getSimpleName());

    public HttpServiceImpl01() {
    }

    @Override
    public Map<String, String> httpTest01(String id, String value) {
        logger.info("------------ Http httpTest01");
        logger.info("Id = " + id + " value = " + value);
        return Collections.singletonMap("key", "value!!!");
    }

    @Override
    public boolean httpTest02() {
        logger.info("------------ Http httpTest02");
        return false;
    }

    @Override
    public void httpTest03() {
        logger.info("------------ Http httpTest03");
    }
}
