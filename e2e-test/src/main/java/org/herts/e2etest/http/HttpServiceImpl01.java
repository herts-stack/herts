package org.herts.e2etest.http;

import org.herts.core.exception.http.HttpErrorException;
import org.herts.core.logger.Logging;
import org.herts.core.service.HertsServiceHttp;
import org.herts.e2etest.common.TestData;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpServiceImpl01 extends HertsServiceHttp<HttpService01> implements HttpService01 {
    private static final java.util.logging.Logger logger = Logging.getLogger(HttpServiceImpl01.class.getSimpleName());

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
       throw new HttpErrorException(HttpErrorException.StatusCode.Status400, "badrequest") ;
    }
}
