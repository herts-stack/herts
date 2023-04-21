package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.service.UnaryRpcServiceRpc;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class UnaryRpcRpcServiceImpl01 extends UnaryRpcServiceRpc implements UnaryRpcRpcService01 {
    private static final Logger logger = HertsLogger.getLogger(UnaryRpcRpcServiceImpl01.class.getSimpleName());

    public UnaryRpcRpcServiceImpl01() {
    }

    public String test01(String id, String value) {
        logger.info("------------ Unary test01 RPC");
        logger.info("Id = " + id + " value = " + value);
        return "Response NAME!!!!";
    }

    public boolean test02() {
        logger.info("------------ Unary test02 RPC");
        return false;
    }

    public Map<String, String> test03() {
        logger.info("------------ Unary test03 RPC");
        return Collections.singletonMap("Key", "Value");
    }

    @Override
    public boolean test100(HelloRequest req) {
        logger.info("------------ Unary test100 RPC");
        return false;
    }
}
