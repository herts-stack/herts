package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.service.HttpRpcServiceRpc;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class HttpServiceImpl extends HttpRpcServiceRpc implements HttpService {
    private static final Logger logger = HertsLogger.getLogger(HttpServiceImpl.class.getSimpleName());

    public HttpServiceImpl() {
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
