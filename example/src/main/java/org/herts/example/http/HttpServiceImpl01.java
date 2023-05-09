package org.herts.example.http;

import org.herts.common.exception.http.HertsHttpError400;
import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsHttpService;
import org.herts.example.common.TestData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpServiceImpl01 extends HertsHttpService<HttpService01> implements HttpService01 {
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

    @Override
    public TestData httpTest04(TestData testData) {
        logger.info("------------ Http httpTest04");
        return testData;
    }

    @Override
    public String httpTest05(List<String> a, Map<String, String> b) {
        return String.format("a=%s, b=%s", a.get(0), b.get("test10"));
    }

    @Override
    public String httpTest06(String a, boolean b, int c, Integer d, double e) {
        return String.format("a=%s, b=%s, c=%d, d=%d, e=%f  ", a, b, c, d, e);
    }

    @Override
    public String httpTest07() {
       throw new HertsHttpError400("badrequest") ;
    }
}
