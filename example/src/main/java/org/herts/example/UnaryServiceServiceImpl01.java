package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsUnaryService;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class UnaryServiceServiceImpl01 extends HertsUnaryService<UnaryRpcRpcService01> implements UnaryRpcRpcService01 {
    private static final Logger logger = HertsLogger.getLogger(UnaryServiceServiceImpl01.class.getSimpleName());

    public UnaryServiceServiceImpl01() {
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

    public boolean test100(HelloRequest req) {
        logger.info("------------ Unary test100 RPC");
        return false;
    }
}